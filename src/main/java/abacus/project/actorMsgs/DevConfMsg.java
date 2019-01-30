package abacus.project.actorMsgs;

import java.io.Serializable;

import com.google.common.collect.ImmutableMap;

import abacus.project.OpticParam.ParamOption;
import abacus.project.acc400Dev.intern.Parameters;

/**
 * Device configuration message passed between the Net manager actor and the device manager actor 
 * @author BMA
 *
 */
public class DevConfMsg implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final ImmutableMap<Parameters, ParamOption> imap;
	private final MsgType mtype;
	private String nodeID=null;
	/**
	 * @param imp
	 * 			immutable map containing the messages
	 * @param type
	 * 		type of message: read or write accepted
	 */
	public DevConfMsg(ImmutableMap<Parameters, ParamOption> imap,MsgType mtype){
		this.imap=imap;
		this.mtype=mtype;
	}
	//method for getting the immutable map
	public ImmutableMap<Parameters, ParamOption> getIMap(){
	return imap;	
	}
	
	//method for returning the message type
	public MsgType getMsgtype(){
		return mtype;
	}
	//TODO:remove the node ID after testing performance
    public void setNodeID(String nodeID){
        this.nodeID = nodeID;
    }
    public String getNodeID(){
        return this.nodeID;
    }
}
