package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import akka.util.ByteString;
/**
 * optical lane coupling options
 * @author BMA
 *
 */
public enum LaneCp implements ParamOption{
	//independent lanes
	IND(Utils.BintoBS("00000000", "00000000")),
	//coupled lanes
	COUP( Utils.BintoBS("00000000", "00000001"));
	
	private final ByteString val;
	LaneCp(ByteString val){
		this.val=val;
	}
	public ByteString getData(Parameters prm){
		//TODO: add a check method for correct parameter
		return val;
	}
	public static LaneCp getLaneSt(ByteString data){
		if (data.equals(Utils.BintoBS("00000000", "00000000")))
			return LaneCp.IND;
		else return LaneCp.COUP;
	}
	@Override
	public String getParameterAsString() {
		return "LaneCp";
	}
	
}
