package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import akka.util.ByteString;
/**
 * network device state
 * @author BMA
 *
 */
public enum DeviceState implements ParamOption {
	// powered down state
	NA(Utils.BintoBS("-1", "-1")),
	// low power: configuration state
	LoPw(Utils.BintoBS("00000000", "00000010")),
	// running state
	RDY(Utils.BintoBS("00000000", "00100000")),
	// Transition state
	TRS(Utils.BintoBS("00000000", "00000000"));
	private final ByteString val;
	DeviceState(ByteString val){
		this.val=val;
	}
	public ByteString getData(Parameters param){
		//TODO: add a check method for the parameter
		return val;
	}
	//Identify and return the enum constant from the given data ByteString; if not found return null;
	public static DeviceState getState(ByteString data){
		for (DeviceState it:DeviceState.values())
			if(data.equals(it.getData(Parameters.STATE)))
				return it;
		//if state is unknown then consider it as transition state
		return TRS;
	}
	@Override
	public String getParameterAsString() {
		return "STATE";
	}
	
}
