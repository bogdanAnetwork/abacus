package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import akka.util.ByteString;
/**
 * optical channel spacing
 * @author BMA
 *
 */
public enum ChSp implements ParamOption{
	//12.5GHz spacing
	_12_5GHZ(Utils.BintoBS("10000000", "00000000")),
	//25GHz spacing
	_25GHZ(Utils.BintoBS("01100000", "00000000")),
	//50GHz spacing
	_50GHZ(Utils.BintoBS("00100000", "00000000")),
	//unrecognized option; not supported 
	NotSupported(ByteString.empty());
	private final ByteString val;
	ChSp(ByteString val){
		this.val=val;
	}
	@Override
	public ByteString getData(Parameters prm){
		return val;
	}
	//Identify and return the enum constant from the given data ByteString
	public static ChSp getSpacing(ByteString data){
			for (ChSp it:ChSp.values())
				if(data.equals(it.getData(Parameters.ChSp_0)))
					return it;	
			//if no option was matched send unsuppored
			return NotSupported;
	}
	//TODO:must fix return format
	@Override
	public String getParameterAsString() {
		return "ChSp";
	}
	
}
