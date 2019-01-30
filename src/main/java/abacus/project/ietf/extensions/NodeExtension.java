package abacus.project.ietf.extensions;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import abacus.project.OpticParam.ChNr;
import abacus.project.OpticParam.ChSp;
import abacus.project.OpticParam.DeviceState;
import abacus.project.OpticParam.FecOptions;
import abacus.project.OpticParam.ModOptions;
import abacus.project.OpticParam.NTribSlot;
import abacus.project.OpticParam.ParamOption;
import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.ietf.flexiGrid.ted.ConnectionsListType;
import abacus.project.ietf.flexiGrid.ted.Fec;
import abacus.project.ietf.flexiGrid.ted.FlexiGridNodeType;
import abacus.project.ietf.flexiGrid.ted.InterfaceType;
import abacus.project.ietf.flexiGrid.ted.InterfacesListType;
import abacus.project.ietf.flexiGrid.ted.Modulation;
import abacus.project.ietf.flexiGrid.ted.NodeConfig;
import abacus.project.ietf.flexiGrid.ted.NodeState;
import abacus.project.ietf.network.NodeListType;
import abacus.project.ietf.network.SupportingNodeListType;
import abacus.project.ietf.network.topology.TerminationPointListType;
import akka.actor.ActorRef;
/**
 * extending the ietf Node class with extra parameters and methods
 * @author BMA
 *
 */
public class NodeExtension extends NodeListType {
    public DeviceState state;
    public ActorRef devActorRef;
    private boolean cPortAssigned = false;
    // additional interface configuration for transceiver node types
    public List<DevInterface> extrIntfs = new ArrayList<DevInterface>();
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NodeExtension(String nodeId, List<SupportingNodeListType> supportingNode,
            List<TerminationPointListType> terminationPoint, NodeConfig nodeConfig, NodeState nodeState,
            FlexiGridNodeType nodeType, DeviceState state, ActorRef devActorRef) {
        super(nodeId, supportingNode, terminationPoint, nodeConfig, nodeState, nodeType);
        this.state = state;
        this.devActorRef = devActorRef;
    }

    public NodeExtension(NodeListType super_node, ActorRef devActorRef) {
        this(super_node.getNodeId(), super_node.getSupportingNode(), super_node.getTerminationPoint(),
                super_node.getNodeConfig(), super_node.getNodeState(), super_node.getNodeType(), DeviceState.NA,
                devActorRef);

    }

    public String SerializeSuperNode() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(new NodeListType(this.getNodeId(), this.getSupportingNode(),
                this.getTerminationPoint(), this.getNodeConfig(), this.getNodeState(), this.getNodeType()));
    }

    /**
     * static method that returns an immutable copy of the current node with the
     * imap config updated
     * 
     * @param imap
     *            the configuration parameters to be applied
     * @return a new node with the updated configuration
     */

    public NodeExtension getNewNode(ImmutableMap<Parameters, ParamOption> imap) {
        Set<ConnectionsListType> con = new HashSet<ConnectionsListType>();
        HashMap<Integer, TmpIntf> tmp_intfs = new HashMap<Integer, TmpIntf>();
        IntStream.range(0, 6).forEach(i -> {
            tmp_intfs.put(i, new TmpIntf(i));
            extrIntfs.add(new DevInterface(i, null, null));
        });
        for (Entry<Parameters, ParamOption> e : imap.entrySet()) {

            switch (e.getValue().getParameterAsString()) {
            case "FEC":
                // e.getKey().getInterface();
                // Fec fec = Fec.valueOf(e.getValue().toString());
                tmp_intfs.get(e.getKey().getInterface()).setFec(Fec.valueOf(e.getValue().toString()));
                break;
            case "MOD":
                // Modulation mod = Modulation.valueOf(e.getValue().toString());
                tmp_intfs.get(e.getKey().getInterface()).setMod(Modulation.valueOf(e.getValue().toString()));
                ;
                break;
            case "STATE":
                this.state = DeviceState.valueOf(e.getValue().toString());
                break;
            case "TributarySlot":
                con.add(new ConnectionsListType(e.getKey().getInterface(), ((NTribSlot) e.getValue()).getPortAsInt()));
                break;
            // Channel spacing and channel number are additional interface
            // parameters
            case "ChSp":
                extrIntfs.get(e.getKey().getInterface()).setChanSp((ChSp) e.getValue());
                break;
            case "ChNr":
                extrIntfs.get(e.getKey().getInterface()).setChanNr((ChNr) e.getValue());
                break;
            default:
                break;
            }
        }
        // add all the old unchanged connections to the new map
        this.getNodeConfig().getConnections().forEach(old_con -> {
            cPortAssigned = false;
            con.forEach(new_con -> {
                if (new_con.getInputPortId() == old_con.getInputPortId()) {
                    cPortAssigned = true;
                }
            });
            if (!cPortAssigned) {
                con.add(new ConnectionsListType(old_con.getInputPortId(), old_con.getOutputPortId()));
            }

        });
        List<InterfacesListType> new_Intf = new ArrayList<InterfacesListType>();
        for (InterfacesListType intf : this.getNodeConfig().getInterfaces()) {
            InterfacesListType x = new InterfacesListType(intf.getName(), intf.getPortNumber(), intf.getInputPort(),
                    intf.getOutputPort(), intf.getDescription(), intf.getType(), intf.getClientInterface(),
                    intf.getNetworkInterface(), intf.getAvailableModulation(),
                    tmp_intfs.get(intf.getPortNumber()).getMod(), intf.getAvailableFEC(), intf.getOutputPort(),
                    tmp_intfs.get(intf.getPortNumber()).getFec());
            new_Intf.add(x);
        }
        NodeConfig new_cfg = new NodeConfig(new_Intf, new ArrayList(con));
        NodeState new_state = new NodeState(new_Intf, new ArrayList(con));
        return new NodeExtension(this.getNodeId(), this.getSupportingNode(), this.getTerminationPoint(), new_cfg,
                new_state, this.getNodeType(), this.state, this.devActorRef);
    }

    public ImmutableMap<Parameters, ParamOption> getImapConfig() {
        HashMap<Parameters, ParamOption> hm = new HashMap<Parameters, ParamOption>();
        // state is not manipulated through parameters but through eval board
        // command
        // hm.put(Parameters.STATE, this.state);
        for (InterfacesListType intf : this.getNodeConfig().getInterfaces()) {
            if (intf.getType() == InterfaceType.NETWORK_INTERFACE) {
                // identify which of the two network lanes is; by default lane 0
                // TODO: generalize method for other flexigrid nodes
                Parameters pr_mod = Parameters.MOD_0;
                Parameters pr_fec = Parameters.FEC_0;
                if (intf.getPortNumber() == 1) {
                    pr_mod = Parameters.MOD_1;
                    pr_fec = Parameters.FEC_1;
                }
                // get the modulation
                switch (intf.getModulationType()) {
                case DP_16QAM_CP:
                    hm.put(pr_mod, ModOptions.DP_16QAM_CP);
                    break;
                case DP_16QAM_I:
                    hm.put(pr_mod, ModOptions.DP_16QAM_I);
                    break;
                case DP_QPSK_CP:
                    hm.put(pr_mod, ModOptions.DP_QPSK_CP);
                    break;
                case DP_QPSK_I:
                    hm.put(pr_mod, ModOptions.DP_QPSK_I);
                    break;
                case DP_8QAM_CP:
                    hm.put(pr_mod, ModOptions.DP_8QAM_CP);
                    break;
                default:
                    break;
                }
                // get the FEC
                switch (intf.getFECType()) {
                case _15_OV:
                    hm.put(pr_fec, FecOptions._15_OV);
                    break;
                case _15_SD:
                    hm.put(pr_fec, FecOptions._15_SD);
                    break;
                case _25:
                    hm.put(pr_fec, FecOptions._25);
                    break;
                default:
                    break;
                }
            }
        }
        // TODO:fix this dirty assignment if find a solution
        if (extrIntfs.size() > 0) {
            hm.put(Parameters.ChSp_0, extrIntfs.get(0).getChSp());
            hm.put(Parameters.ChSp_1, extrIntfs.get(1).getChSp());
            hm.put(Parameters.ChNr_0, extrIntfs.get(0).getChNr());
            hm.put(Parameters.ChNr_1, extrIntfs.get(1).getChNr());
        }

        return ImmutableMap.copyOf(hm);
    }

    /**
     * sets the state of the device
     * 
     * @param state
     *            stored state of the device
     */
    public void changeState(DeviceState state) {
        this.state = state;
    }

    /**
     * gets the state of the device
     * 
     * @param state
     *            stored state of device
     */
    public DeviceState getState() {
        return this.state;
    }

    /**
     * 
     * @return the device actor reference
     */
    public ActorRef getDevActorRef() {
        return this.devActorRef;
    }

    /**
     * sets the device actor reference
     * 
     * @param devActorRef
     */
    public void setDevActorRef(ActorRef devActorRef) {
        this.devActorRef = devActorRef;
    }

    /**
     * 
     * @return the additional interface config holders
     */
    public List<DevInterface> getExtrIntfs() {
        return this.extrIntfs;
    }
    private class TmpIntf {
    	private Fec fec;
    	private Modulation mod;
    	private int nr;
    	public TmpIntf(int nr){
    		this.fec = null;
    		this.mod = null;
    		this.nr = nr;
    	}
    	public TmpIntf(Fec fec, Modulation mod, int nr){
    		this.fec = fec;
    		this.mod = mod;
    		this.nr = nr;
    	}
    	public void setFec(Fec fec){
    		this.fec =  fec;
    	}
    	public void setMod(Modulation mod){
    		this.mod = mod;
    	}
    	public Fec getFec(){
    		return this.fec;
    	}
    	public Modulation getMod(){
    		return this.mod;
    	}
    	public int getNr(){
    		return this.nr;
    	}
    }
}
