package abacus.project.modules;

import java.time.Instant;
import akka.actor.UntypedActorWithStash;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.LoggerFactory;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akka.io.Tcp;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.japi.Procedure;
import akka.util.ByteString;
import scala.concurrent.duration.FiniteDuration;

import com.google.common.collect.ImmutableMap;

import abacus.project.OpticParam.DeviceState;
import abacus.project.OpticParam.ParamOption;
import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import abacus.project.actorMsgs.DevConfMsg;
import abacus.project.actorMsgs.EbSubCmd;
import abacus.project.actorMsgs.MsgType;
import abacus.project.actorMsgs.TCPMsg;
/**
 * Device driver module
 * @author BMA
 *
 */
public class DevDriver extends UntypedActorWithStash {
	private final Logger log = LoggerFactory.getLogger(DevDriver.class);
	//default requires state convention is low power
	private DeviceState reqState = DeviceState.LoPw;
	// TCP socket address; default 34111 port
	private final InetSocketAddress remote;
	// the connection to the managing actor of the transceiver module
	private ActorRef netDriver;
	private ActorRef tcpInterface;
	// size of the immutable map received
	private Integer map_size = 0;
	private Cancellable retryScheduler;
	// immutable map of the current device message
	private ImmutableMap<Parameters, ParamOption> imap;
	// predefined TCP Messages
	// check state of the device
	private final TCPMsg chk_st = new TCPMsg(MsgType.R, Parameters.STATE);
	// power on device
	private final TCPMsg pwOn = new TCPMsg(MsgType.B_CMD, EbSubCmd.PW_ON, EB_PW, false);
	private final TCPMsg assertB = new TCPMsg(MsgType.B_CMD, EbSubCmd.AS_PIN, PinAs.GEN.val, false);
	private final TCPMsg assertL0 = new TCPMsg(MsgType.B_CMD, EbSubCmd.AS_PIN, PinAs.LN0.val, false);
	private final TCPMsg assertL1 = new TCPMsg(MsgType.B_CMD, EbSubCmd.AS_PIN, PinAs.LN1.val, false);
	private final TCPMsg deAssertB = new TCPMsg(MsgType.B_CMD, EbSubCmd.DE_AS_PIN, CMB, false);
	private final TCPMsg deAssertL0 = new TCPMsg(MsgType.B_CMD, EbSubCmd.DE_AS_PIN, PinAs.LN0.val, false);
	private final TCPMsg deAssertL1 = new TCPMsg(MsgType.B_CMD, EbSubCmd.DE_AS_PIN, PinAs.LN1.val, false);
	// received message queue
	private Deque<TCPMsg> msgQueue = new LinkedList<TCPMsg>();

	// enumeration containing the data Pins to assert
	private static enum PinAs {
		GEN(Utils.BintoBS("00000000", "00000100")), LN0(Utils.BintoBS("00000000", "00000001")), LN1(
				Utils.BintoBS("00000000", "10000000")), ALL(Utils.BintoBS("00000000", "10000101"));
		private final ByteString val;

		PinAs(ByteString val) {
			this.val = val;
		}

		public ByteString getData() {
			return this.val;
		}
	};

	// evaluation board power on/off pin
	private static final ByteString EB_PW = Utils.BintoBS("00000000", "00000001");
	// combination of: low power, lane 0 and 1 pins
	private static final ByteString CMB = Utils.BintoBS("00000000", "00000100");

	/********************
	 * PROPERTIES START *
	 ********************/

	/**
	 * Create the properties through this method for better type safety.
	 * 
	 * @param remote
	 *            the TCP listening port on the transceiver
	 * @param netDriver
	 *            reference to the network manager actor
	 * @return the properties
	 */

	public static Props props(InetSocketAddress remote) {
		return Props.create(DevDriver.class, () -> new DevDriver(remote));
	}

	/******************
	 * PROPERTIES END *
	 ******************/

	/**********************
	 * CONSTRUCTORS START *
	 **********************/
	/**
	 * Constructor for TcpConnection
	 * 
	 * @param remote
	 *            the TCP listening port on the transceiver
	 * @param netDriver
	 *            reference to the transceiver manager actor
	 */
	DevDriver(InetSocketAddress remote) {
		log.debug("socket : {}", remote);
		this.remote = remote;
		// start the tcp interface actor
		this.tcpInterface = context().actorOf(Acc400TCPInterface.props(remote, self()),
				"tcpInterface:".concat(remote.toString().substring(1)));
		log.debug("DevDrv has been created");
	}

	/**********************
	 * CONSTRUCTORS STOP *
	 **********************/

	/**
	 * initial state of device; attempting power on
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if (message instanceof CommandFailed)
			log.debug("Connection Failed: {}", message.toString());
		else if (message instanceof Connected) {
			log.debug("Device driver {} became connected;", self().path().name());
			startChkStateScheduler();

			// unstashing all messages and becomming connected
			unstashAll();
			log.debug("Device driver, changing state to CONNECTED");
			getContext().become(connected);
		} else if (message instanceof TCPMsg) {
		    //get the reference to the remote network driver
		    netDriver = getSender();
			//TODO:consider removing this case because it's absurd to receive a TCP msg before connected
			log.debug("Device driver {} received TCP message in idle state : {}", self().path().name(),
					((TCPMsg) message).getMType());
			// assign TCPMsg instance
			parseTCPMsg((TCPMsg) message);
		} else {
	        //get the reference to the remote network driver
            netDriver = getSender();
			log.debug("Device driver {} received message in idle state, stashing :{}", self().path().name(),
					message);
			
			stash();
		}
	}

	/**
	 * driver in connected state
	 */
	Procedure<Object> connected = new Procedure<Object>() {
		@Override
		public void apply(Object message) {
			// only parse TCP reply message and EbSubCmd and stash the rest
			if (message instanceof TCPMsg) {
				log.debug("Device driver {} In connected state: received message from TCP interface: {}", self().path().name(),
						message.toString());
				// parse the TCP reply message
				parseTCPMsg((TCPMsg) message);
			} else if (message instanceof EbSubCmd) {
				log.debug("Device driver {} In connected state: received message from network manager: {}", self().path().name(),
						message.toString());
	            //get the reference to the remote network driver
	            netDriver = getSender();
				// parse the evaluation board subcommand message from the
				// netDriver
				parseEbSubCmdMsg((EbSubCmd) message);
			} else {
		        //get the reference to the remote network driver
	            netDriver = getSender();
				log.debug("Device driver {} not yet in stable state; stash received message: {}", self().path().name(),
						message);
				stash();
			}
			// TODO:add new message conditions if applicable
		}
	};

	/**
	 * execute driver state change to match the device current state
	 * 
	 * @param state:
	 *            device state
	 */
	private void parseTCPMsg(TCPMsg msg) {
		// two possible cases are considered: TCP state message and board
		// control msg
		if (msg.getParameter().equals(Parameters.STATE)) {
			if (DeviceState.getState(msg.getData())==reqState){
				// stop check state scheduler since the required state has been reached
				retryScheduler.cancel();
				// tell network manager the device is in the required state
				if (netDriver!=null){
					//the netDriver might be null if the connection has not been established yet
					netDriver.tell(reqState, self());					
				}
				//unstash the rest of the messages 
				unstashAll();
				switch (DeviceState.getState(msg.getData())) {
				case LoPw:
					log.debug("Device driver, changing state to Low Power");
					getContext().become(low_power);
					break;
				case RDY:
					log.debug("Device driver, changing state to Ready");
					getContext().become(ready);
					break;
				}
			} else if (DeviceState.getState(msg.getData())==DeviceState.NA){
				// the device is not powered on; has no state: ffff
				// powering on and checking the state again
				log.debug("Device driver read No State, powering on");
				tcpInterface.tell(pwOn, self());
			}
		} else if (msg.getMType().equals(MsgType.B_CMD))
			// a board control message reply triggers a state change
			startChkStateScheduler();
		// TODO:add new message conditions if applicable
	}

	/**
	 * parse evaluation board subcommand message from network manager
	 * 
	 * @param dmsg
	 */
	private void parseEbSubCmdMsg(EbSubCmd ebmsg) {
		// if message is evaluation board subcommand then send the
		// message to the tcp interface
		switch (ebmsg) {
		case AS_PIN:
			//change the required state to LoPw
			reqState = DeviceState.LoPw;
			// tell the network manager the device is already in
			// low-power mode
			tcpInterface.tell(assertB, self());
			tcpInterface.tell(assertL0, self());
			log.debug("Device driver, received request to change state to Low Power mode");
			break;
		case DE_AS_PIN:
			reqState = DeviceState.RDY;
			// send deassert (running) message to TCP interface
			log.debug("Device driver, received request to change state to Ready mode");
			tcpInterface.tell(deAssertB, self());
			tcpInterface.tell(deAssertL0, self());
//			tcpInterface.tell(deAssertL1, self());
			// TODO:what then?
			//we wait for the reply to change the state
		default:
			break;
		}
	}
	/**
	 * writing state class defninition
	 * @author BMA
	 *
	 * @param <T>
	 */
	class Write<T> implements Procedure<T> {
		String prevState;
		// reading
		private DevConfMsg dmsg = new DevConfMsg(imap, MsgType.R);
		private int preReads;
		
		Write(String prevState) {
			this.prevState = prevState;
		}

		@Override
		public void apply(Object message) throws Exception {
			// refine the if-else evaluation below
			if (message instanceof DevConfMsg) {
				dmsg = (DevConfMsg) message;
				// store the number of parameters in the message in order to
				// keep count of the replies
				preReads = map_size = dmsg.getIMap().size();
				// separate actions required for board control message type
				switch (dmsg.getMsgtype()) {
				case B_CMD:
					// TODO: handle board control message; not expected
					log.debug("Device Driver received unhandled board cmd msg form net driver");
					break;
				default:
					// read the data in each register from the map before, in
					// any situation, read or write as well
					for (Map.Entry<Parameters, ParamOption> t : dmsg.getIMap().entrySet()) {
						TCPMsg nu = new TCPMsg(MsgType.R, t.getKey());
						tcpInterface.tell(nu, self());
					}
					break;
				}
			} else if (message instanceof TCPMsg) {
				if (((TCPMsg) message).getParameter()==Parameters.STATE){
					//ignore STATE message exchanges in write-state
				}
				else {
					// received a TCP reply message
					TCPMsg msg = (TCPMsg) message;
					switch (msg.getMType()) {
					// received TCP message of type 'read'
					case R:
						if (preReads == 0){
							log.trace("Device driver {} Received message from tcp interface actor: {} -- {}", self().path().name(), msg.getParameter(), msg.getParameter().getOption(msg.getData()));
							// store reply and check if it's the last one in the
							// stored map
							msgQueue.addLast(msg);
							if (map_size.equals(msgQueue.size())) {
								// if it's the last message send the reply to the
								// network manager
								sendNetMng(msgQueue);
								unstashAll();
								if (prevState == "low_power") {
									getContext().become(low_power);
								} else if (prevState == "ready"){
									getContext().become(ready);
								}
							}
						} else {
							//decrement the number of pre-reads required
							preReads = preReads - 1;
							// apply bitmask and execute write
							Parameters prm = msg.getParameter();
							ByteString opt = dmsg.getIMap().get(prm).getData(prm);
							// combine the write option with the read data by using
							// the corresponding bit mask
							ByteString wrData = maskData(msg.getData(), prm.getmask().getmaskBS(),
									opt);
							TCPMsg write = new TCPMsg(MsgType.WR, prm, wrData, true);
							tcpInterface.tell(write, self());
						}
						break;
					// received TCP message of type 'write'
					case WR:
						//always execute another read after the procedure to check if the write was accepted
						tcpInterface.tell(new TCPMsg(MsgType.R, msg.getParameter()), self());
						break;
					case B_CMD:
						log.warn("Device driver {} Received board command message while R/WR from tcp interface actor: {} -- {}",
								self().path().name(), msg.getSubCmd(), msg.getParameter().getOption(msg.getData()));
						break;
					default:
						break;
					}
				}
				
			} else
				stash();
		}
	}	
	/**
	 *  "Reading" state for sending and receiving FPGA format read messages
	 * @author BMA
	 *
	 * @param <T> custom procedure implementation to store the previous state
	 */
	class Read<T> implements Procedure<T> {
		String prevState;
		// reading
		private DevConfMsg dmsg = new DevConfMsg(imap, MsgType.R);

		Read(String prevState) {
			this.prevState = prevState;
			log.debug("S1 starting to read config");
		}

		@Override
		public void apply(Object message) throws Exception {
			
			// refine the if-else evaluation below
			if (message instanceof DevConfMsg) {
				dmsg = (DevConfMsg) message;
				// store the number of parameters in the message in order to
				// keep count of the replies
				map_size = dmsg.getIMap().size();
				log.debug("S2 got the imap");
				// separate actions required for board control message type
				switch (dmsg.getMsgtype()) {
				case B_CMD:
					// TODO: handle board control message; not expected
					log.debug("Device Driver received unhandled board cmd msg form net driver");
					break;
				default:
					// read the data in each register from the map before, in
					// any situation, read or write as well
					for (Map.Entry<Parameters, ParamOption> t : dmsg.getIMap().entrySet()) {
						TCPMsg nu = new TCPMsg(MsgType.R, t.getKey());
						tcpInterface.tell(nu, self());
						log.debug("S3 sending msg to device");
					}
					break;
				}
			} else if (message instanceof TCPMsg) {
			if (((TCPMsg) message).getParameter()==Parameters.STATE){
				//ignore STATE messages from device in reading state
			}
			else {
				// received a TCP reply message
				TCPMsg msg = (TCPMsg) message;
				switch (msg.getMType()) {
				// received TCP message of type 'read'
				case R:
					log.debug("T2_1Intermediate {}", Instant.now().toEpochMilli());
					// store reply and check if it's the last one in the
					// stored map
					msgQueue.addLast(msg);
					if (map_size.equals(msgQueue.size())) {
						// if it's the last message send the reply to the
						// network manager
						sendNetMng(msgQueue);
						unstashAll();
						if (prevState == "low_power") {
							getContext().become(low_power);
						} else if (prevState == "ready"){
							getContext().become(ready);
						}
					}
					break;
				// received TCP message of type 'write'
				case B_CMD:
					log.warn("Device driver {} Received board command message while R/WR from tcp interface actor: {} -- {}",
							self().path().name(), msg.getSubCmd(), msg.getParameter().getOption(msg.getData()));
					break;
				default:
					break;
				}
			}
			} else
				//stash unhandled msg
				stash();
		}
	}
	
	/**
	 * method for combining the read data with the parameter option by applying
	 * the bit mask
	 * 
	 * @param data
	 *            which was read
	 * @param mask
	 *            corresponding to the parameter option
	 * @param option
	 *            of the stored parameter for writing
	 */
	private ByteString maskData(ByteString data, ByteString mask, ByteString option) {
		ByteString wrData = ByteString.empty();
		for (int i = 0; i < (data.toArray().length) ; i++)
			wrData = wrData
					.concat((ByteString.fromInts(data.toArray()[i] & (~mask.toArray()[i]) ^ option.toArray()[i])));
		return wrData;
	}

	/**
	 * Low power mode; open configuration state
	 */
	Procedure<Object> low_power = new Procedure<Object>() {
		@Override
		public void apply(Object message) {
			// checking which kind of message was received: device
			// configuration, evaluation board subcommand or TCP reply(status or
			// board cmd reply)
			if (message instanceof DevConfMsg) {
				log.debug("Device driver {} In low power state received DevConfMsg: {}", self().path().name(), ((DevConfMsg) message).getIMap());
				// if message is device configuration type then become
				// read/write
				self().tell(message, self());
				log.debug("{} read/write device Configuring device from LOW POWER state", self().path().name());
				if (((DevConfMsg) message).getMsgtype()==MsgType.R){
					getContext().become(new Read("low_power"));	
				} else {
				    log.debug("Start Writing config: {}", ((DevConfMsg) message).getIMap());
					getContext().become(new Write("low_power"));
				}
				//updating the netDriver Reference whenever getting a message, not safe, think of revising
				netDriver = sender();
			} else if (message instanceof EbSubCmd) {
				log.debug("Device driver {} In low power state received EbSubCmd: {}", self().path().name(), ((EbSubCmd) message).toString());
				// if message is evaluation board subcommand then send the
				// message to the tcp interface
				parseEbSubCmdMsg((EbSubCmd) message);
				//updating the netDriver Reference whenever getting a message, not safe, think of revising
				netDriver = sender();
			} else if (message instanceof TCPMsg) {
				log.debug("Device driver {} In low power state received TCPMsg: {}", self().path().name(), ((TCPMsg) message).toString());
				// received TCP message
				log.debug("FROM sender {}",sender());
				parseTCPMsg((TCPMsg) message);
			} else
				stash();
		}
	};

	Procedure<Object> ready = new Procedure<Object>() {
		@Override
		public void apply(Object message) {
			// checking which kind of message was received: device
			// configuration, evaluation board subcommand or TCP reply(status or
			// board cmd reply)
			if (message instanceof DevConfMsg) {
				log.debug("Device driver {} In READY state received from NetDriver: {}", self().path().name(), ((DevConfMsg) message).getIMap());
				// if message is device configuration type then become
				// read/write
				self().tell(message, self());
				log.debug("{} reading/writing device config from READY state", self().path().name());
				if (((DevConfMsg) message).getMsgtype()==MsgType.R){
					getContext().become(new Read("low_power"));	
				} else {
					getContext().become(new Write("low_power"));
				}
			} else if (message instanceof EbSubCmd) {
				log.debug("Device driver {} In READY state received EbSubCmd from NetDriver: {}", self().path().name(), ((EbSubCmd) message).toString());
				// if message is evaluation board subcommand then send the
				// message to the tcp interface
				parseEbSubCmdMsg((EbSubCmd) message);
			} else if (message instanceof TCPMsg) {
				log.debug("Device driver {} In READY state received from NetDriver: {}", self().path().name(), ((TCPMsg) message).toString());
				// received TCP message
				parseTCPMsg((TCPMsg) message);
			} else
				stash();
		}
	};

	/**
	 * scheduler for checking the device status
	 */
	private void startChkStateScheduler() {
		if (retryScheduler == null || retryScheduler.isCancelled())
			retryScheduler = getContext().system().scheduler().schedule(FiniteDuration.Zero(),
					FiniteDuration.apply(1, "seconds"), tcpInterface, chk_st, getContext().dispatcher(), self());
	}

	/**
	 * building the configuration reply procedure and sending it to the network
	 * driver
	 */
	private void sendNetMng(Deque<TCPMsg> msgQueue) {
		// create a mutable hashmap from the TCP reply message queue
		Map<Parameters, ParamOption> mp = new HashMap();
		for (TCPMsg msg : msgQueue) {
			msg.getData();
			mp.put(msg.getParameter(), msg.getParameter().getOption(msg.getData()));			
		}
		// create the immutable map
		ImmutableMap<Parameters, ParamOption> ms = ImmutableMap.copyOf(mp);
		// create the reply device-message
		// TODO: sort the type of reply message. for now we consider it read
		DevConfMsg rep = new DevConfMsg(ms, MsgType.R);
		// clear the messages from the queue
		msgQueue.clear();
		log.debug("Dev Driver sending to net driver reply: {}", ms);
		System.out.println(netDriver);
		netDriver.tell(rep, self());
	}
}