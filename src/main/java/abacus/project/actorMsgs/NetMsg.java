package abacus.project.actorMsgs;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import abacus.project.ietf.extensions.NodeExtension;
import abacus.project.ietf.network.NodeListType;
/**
 * network configuration message exchanged between the netconf server and the network controller/module
 * @author BMA
 *
 */
public class NetMsg implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public enum  target{NODE, MEDIA_CHANNEL, TRS};
	private final MsgType msgType;
	private final target tg;
	private final String configJson;
	/**
	 * 
	 * @param flag write flag
	 * @param configuration message json string  
	 */
	public NetMsg( String configJson, target tg, MsgType msgType){
		this.msgType = msgType;
		this.tg = tg;
		this.configJson = configJson;
	}
	
	public MsgType getMsgType(){
		return this.msgType;
	}
	public target getTarget(){
		return this.tg;
	}
	public String getMsgPayload(){
		return this.configJson;
	}
	/**
	 * use only for node config Jsons
	 * @return the node ID or null if json is not a node config
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String getNodeId() throws JsonParseException, JsonMappingException, IOException{
		if (this.tg.equals(target.NODE)||this.tg.equals(target.TRS)){
			ObjectMapper mp = new ObjectMapper();
			NodeExtension node = new NodeExtension (mp.readValue(this.configJson, NodeListType.class), null);
			return node.getNodeId();
		} else return "";
		
		
	}
	
}
