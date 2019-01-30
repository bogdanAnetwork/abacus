package abacus.project.ietf.extensions;

import java.util.ArrayList;
import java.util.List;

import abacus.project.ietf.network.NetworkListType;
/**
 * wrapper class for ietf network List Type adding extra methods 
 * @author BMA
 *
 */
public class NetworkListTypeWrapper {
	private final NetworkListType networkListType;
	private final List<NodeExtension> nodeExtension;
	
	public NetworkListTypeWrapper(NetworkListType nets){
		this.networkListType = nets;
		List<NodeExtension> ndExt = new ArrayList<NodeExtension>();
		this.networkListType.getNode().forEach(nd ->{
			ndExt.add(new NodeExtension(nd, null));
		});
		this.nodeExtension = ndExt; 
	}
	public NetworkListType getNetworkListType(){
		return this.networkListType;
	}
	public List<NodeExtension> getNodeExtensions(){
		return this.nodeExtension;
	}
	
}
