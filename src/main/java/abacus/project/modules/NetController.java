package abacus.project.modules;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import abacus.project.OpticParam.ChNr;
import abacus.project.OpticParam.ChSp;
import abacus.project.OpticParam.DeviceState;
import abacus.project.OpticParam.EmptyOpt;
import abacus.project.OpticParam.ParamOption;
import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.actorMsgs.DevConfMsg;
import abacus.project.actorMsgs.MsgType;
import abacus.project.actorMsgs.NetMsg;
import abacus.project.actorMsgs.NetMsg.target;
import abacus.project.ietf.extensions.MediaChannelWrapper;
import abacus.project.ietf.extensions.NetworkListTypeWrapper;
import abacus.project.ietf.extensions.NetworksWrapper;
import abacus.project.ietf.extensions.NodeExtension;
import abacus.project.ietf.flexiGrid.mediaChannel.MediaChannelListType;
import abacus.project.ietf.network.Networks;
import abacus.project.ietf.network.NodeListType;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * wdm flexigrid network controller, old network driver 
 * @author BMA
 *
 */
public class NetController extends AbstractActor {
    private PartialFunction<Object, BoxedUnit> trsConfig;
    private PartialFunction<Object, BoxedUnit> nodeConfig;
    private PartialFunction<Object, BoxedUnit> mediaChannelConfig;
    private PartialFunction<Object, BoxedUnit> idle;
    // logging factory
    private final Logger log = LoggerFactory.getLogger("netDriver");
    // managing actor reference
    private ActorRef netconfSrv;
    // configuration status of the node cards
    private HashMap<String, NodeExtension> storedNodes = new HashMap<String, NodeExtension>();
    private LinkedList<NetMsg> stash = new LinkedList<NetMsg>();
    private LinkedList<MediaChannelWrapper> mcStash = new LinkedList<MediaChannelWrapper>();
    private HashMap<String, ImmutableMap<Parameters, ParamOption>> imapRec = new HashMap<String, ImmutableMap<Parameters, ParamOption>>();
    // initialization message counter
    private int msgCnt = 0;
    // messages waiting for reply counter
    private int waitNr = 0;
    // the media channel wrapped structure stored for current config
    MediaChannelWrapper mcw = new MediaChannelWrapper(null);
    // object mapper
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Create the properties through this method for better type safety.
     * 
     * @param flex400G_App
     *            reference to flexible 400G network application
     * @return the properties
     */

    public static Props props(String confFile) {
        return Props.create(NetController.class, () -> new NetController(confFile));
    }

    /**
     * 
     * @param net
     *            : the network configuration;
     */
    public NetController(String confFile) throws IOException {
        //TODO: replace network topology file with device and link discovery protocols when ready
        log.debug("Reading the network topology file...");
        NetworksWrapper nw = new NetworksWrapper(
                mapper.readValue(this.getClass().getResource(confFile), Networks.class));

        log.debug("Starting the device driver modules...");
        for (NetworkListTypeWrapper nt : nw.getWrappedNetworks()) {
            for (NodeExtension node : nt.getNodeExtensions()) {
                // send the config-read command to the card
                log.debug("Starting module {} and reading the configuration...", node.getNodeId());
                // store the device driver actor reference in the node
                // element
                InetSocketAddress sk = new InetSocketAddress(node.getNodeId().split(":")[0],
                        Integer.parseInt(node.getNodeId().split(":")[1]));
                    
                ActorRef nodeRef = getContext().actorOf(Props.create(DevDriver.class, sk), node.getNodeId()); 
                // store the device reference for later identification
                node.setDevActorRef(nodeRef);
                //read node config
                
                readDeviceConfig(node.getDevActorRef());
                storedNodes.put(node.getNodeId(), node);
                }
        }
        
        // START the receive-message behavior
        receive(ReceiveBuilder.match(NetMsg.class, message -> {
            // stash all the messages received until the initial state has been
            // updated
            log.info("Starting the receive-message behavior");  

            netconfSrv = sender();
            log.debug("Net Driver received unhandled NetConfMsg, stashing ... : {}", message.getTarget());
            stash.addLast(message);
        }).match(DevConfMsg.class, message -> {
            log.debug("received configuration message from device {}: {}" + sender().path().name() + message.getIMap());
            log.trace("Dev  {} /{}",msgCnt, storedNodes.size());
            // set the configuration received to the corresponding node
            updateNodeConfig(message);
            msgCnt = msgCnt + 1;
            // when all messages have been received, unstash msg and get to the
            // idle state state
           
        }).match(DeviceState.class, msg -> {
            // TODO:update state in the corresponding node if necessary
            log.debug("received state message from {}: {}" + sender().toString() + msg);
        }).matchAny(msg -> {
            // received unhandled message
            log.debug("received unknown message: {}" + msg.toString());
        }).build());
        // STOP the receive-message behavior
        
        // START THE IDLE STATE
        idle = ReceiveBuilder.match(NetMsg.class, msg -> {
        	log.debug("Transitioned to idle...");
        	netconfSrv = sender();
            // resend the msg to self before changing state
            self().tell(msg, self());
            switch (msg.getTarget()) {
            // become mediaChannelConfig
            // before changing state initialize the wait msg counter to zero
            case MEDIA_CHANNEL:
                log.debug("Transitioning to Media Channel configuration state");
                waitNr = 0;
                getContext().become(mediaChannelConfig);
                break;
            case NODE:
            	log.debug("Transitioning to Node configuration state");
                waitNr = 0;
                getContext().become(nodeConfig);
                break;
            default:
                break;
            }
        }).build();
        // END OF IDLE STATE

        // START:TRS CONFIGURATION STATE//
        nodeConfig = ReceiveBuilder.match(NetMsg.class, msg -> {
            // whenever you receive a message
            if (!sender().equals(self())) {
                netconfSrv = sender();
            }
            // checking the message type received: node-config or
            // media-channel-config
            // state cannot change as long as counter waitNr >0

                // get the device actor reference and send a configuration
                // message
                log.debug("Received node config msg: {}" + msg.getMsgPayload());
                NodeExtension node = new NodeExtension(mapper.readValue(msg.getMsgPayload(), NodeListType.class), null);
                // send actual config msg
                sendDevConfMsg(node.getImapConfig(), ((NetMsg) msg).getMsgType(),
                        node.getNodeId());
                // each time we send a DevConfMsg, the waitNr increments
                waitNr = waitNr + 1;
                log.debug("extracted imap from node to send to config : {}" + node.getImapConfig());
            }).match(DevConfMsg.class, msg -> {
            log.debug("received Device conf msg: {} from {}" + msg.getIMap(), sender());
            NodeExtension new_nd = updateNodeConfig(msg);
            log.debug("sending back reply to NetconfSrv: {}" + new_nd.SerializeSuperNode());
            netconfSrv.tell(new NetMsg(new_nd.SerializeSuperNode(), target.NODE, msg.getMsgtype()), self());
            getContext().become(idle);
        }).match(DeviceState.class, msg -> {
            // TODO: update state
            log.debug("Network Driver: got a device state message" + msg);
        }).build();
        // END : TRS CONFIGURATION STATE//

        // START : MEDIA CHANNEL CONFIGURATION STATE//
        mediaChannelConfig = ReceiveBuilder.match(NetMsg.class, msg -> {
            // whenever you receive a message keep track of the sender reference netconfserv
            if (!sender().equals(self())) {
                netconfSrv = sender();
            }
            MediaChannelListType mcElement = mapper.readValue(((NetMsg) msg).getMsgPayload(),
            		MediaChannelListType.class);
                // only one media channel is expected per message
                mcw = new MediaChannelWrapper(mcElement);
                // store the number of DevConfMsgs that will be waited for
                waitNr = mcw.getImaps().size();
                // extract the imap config from the NetMsg and send it to all
                // devices in the list
                HashMap<String, ImmutableMap<Parameters, ParamOption>> z = mcw.getImaps();
                log.debug("Configuring MC with the following info {}", z);
                for (String nodeId : mcw.getImaps().keySet()) {
                    sendDevConfMsg(mcw.getImaps().get(nodeId), msg.getMsgType(), nodeId);
                }
        }).match(DevConfMsg.class, msg -> {
//            log.debug("Received device config reply :{}", msg.getIMap());
            imapRec.put(sender().path().name(), msg.getIMap());
            waitNr = waitNr - 1;
            // check if all messages have been received
            if (waitNr == 0) {
                // if correct message then send back reply to the NETCONF server
            checkMediaChannelRecConfig(imapRec);
            // serialize the reply
            netconfSrv.tell(new NetMsg(mcw.serializeMC(), target.MEDIA_CHANNEL, msg.getMsgtype()), self());
            // check if the stash empty and resend the message stored to
            // self for processing
            if (!stash.isEmpty()) {
                self().tell(stash.pollFirst(), self());
            }
            // get back into idle state
            getContext().become(idle);
            }
        }).build();
    }

    /**
     * check if the config received is consistent across all nodes in the media
     * channel
     * 
     * @param imapRec
     * @return true if the config checks out, or false in case the devices were
     *         not configured correct
     */
    private boolean checkMediaChannelRecConfig(HashMap<String, ImmutableMap<Parameters, ParamOption>> imapRec) {
        ParamOption chNr = mcw.getChannelNumber();
        ParamOption chSp = mcw.getChannelSpacing();
        // check to see if the N and M parameters are consistent throughout the
        // channel
        for (String nodeId : imapRec.keySet()) {
            for (Parameters prm : imapRec.get(nodeId).keySet()) {
                if (imapRec.get(nodeId).get(prm) instanceof ChSp) {
                    if (!imapRec.get(nodeId).get(prm).equals(chSp)) {
                        return false;
                    }
                }
                if (imapRec.get(nodeId).get(prm) instanceof ChNr) {
                    if (!imapRec.get(nodeId).get(prm).equals(chNr)) {
                        log.debug("M and N parameters for the MC received are not consistent!!!");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * updating the node stored config data
     * 
     * @param msg
     * @return the updated node
     * @throws JsonProcessingException 
     */
    private NodeExtension updateNodeConfig(DevConfMsg msg) throws JsonProcessingException {

        NodeExtension newNode = storedNodes.get(sender().path().name()).getNewNode(msg.getIMap());
        System.out.println(sender().toString());
        // create the new node by applying the imap new config to the old
        // stored node
        log.debug("Updating node config : {}" , newNode.getNodeId());
        //add the new node and replace the old one
        storedNodes.put(newNode.getNodeId(), newNode);
        return newNode;
    }

    // read the node configuration
    private void readDeviceConfig(ActorRef device) {
        Map<Parameters, ParamOption> mp = new HashMap<Parameters, ParamOption>();
        // TODO: fix message structure to get rid of EmptyOption object
        // immutable map does not allow null values required by config-read
        // requests
        EmptyOpt none = new EmptyOpt();
        for (Parameters prm : Parameters.values()) {
            if (!prm.equals(Parameters.EMPTY) && (!prm.equals(Parameters.STATE))) {
                mp.put(prm, none);
            }
        }

        ImmutableMap<Parameters, ParamOption> ms = ImmutableMap.copyOf(mp);
        device.tell(new DevConfMsg(ms, MsgType.R), self());
    }
    /**
     * send a device configuration message to the device driver
     * @param imap map of configuration parameters
     * @param type message type
     * @param nodeID  node ID
     */
    private void sendDevConfMsg(ImmutableMap<Parameters, ParamOption> imap, MsgType type, String nodeID) {
               DevConfMsg msg = new DevConfMsg(imap, type);
               msg.setNodeID(nodeID);
               storedNodes.get(nodeID).devActorRef.tell(msg, self());
    }
    /**
     * checking if the message received is the last in the stash
     * @param msgCnt message count
     */
    private void checkForEnd(int msgCnt, long startTime){
        log.trace("received message {} and waiting for {}", msgCnt, storedNodes.size());
        if (msgCnt == storedNodes.size()) {
            while (!stash.isEmpty()) {
                self().tell(stash.pollFirst(), self());
            }
            getContext().become(idle);
        }
    }

}

