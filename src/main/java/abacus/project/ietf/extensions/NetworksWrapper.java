package abacus.project.ietf.extensions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import abacus.project.ietf.network.Networks;
/**
 * wrapper class for ietf Networks adding extra methods 
 * @author BMA
 *
 */
public class NetworksWrapper {
	private final Networks networks;
	private final List<NetworkListTypeWrapper> networkListTypeWrapper;

	public NetworksWrapper(Networks nets){
		this.networks = nets;
		List<NetworkListTypeWrapper> nLTW = new ArrayList<NetworkListTypeWrapper>();
		this.networks.getNetwork().forEach(nw ->{
			nLTW.add(new NetworkListTypeWrapper(nw));
		});
		this.networkListTypeWrapper = nLTW;
	}
	
	public Networks getNetworks(){
		return this.networks;
	}
	
	public List<NetworkListTypeWrapper> getWrappedNetworks(){
		return this.networkListTypeWrapper;
	}
	public String SerializeNetworks() throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(networks);
	}

}
