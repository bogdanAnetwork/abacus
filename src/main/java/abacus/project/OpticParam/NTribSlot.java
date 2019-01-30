package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import abacus.project.acc400Dev.intern.bitMask;
import akka.util.ByteString;
/**
 * optical transport network tributary slots
 * @author BMA
 *
 */
public enum NTribSlot implements ParamOption{
	//independent lane tributary slot options
	N_0_0("0000", "N0"),
	N_0_1("0001", "N0"),
	N_1_0("0010", "N1"),
	N_1_1("0011", "N1"),
	//coupled lanes tributary slot options
	N_0("0100", "N0"),
	N_1("0101", "N0"),
	N_2("0110", "N0"),
	N_3("0111", "N0"),
	//unrecognized slot assignment; not supported 
	NotSupported("", "");
	private String val;
	private String port;
	NTribSlot(String val, String port){
		this.val=val;
		this.port = port;
	}
	@Override
	public ByteString getData(Parameters param) {
		switch(param){
		case C0toNTx:
		case C0toNRx:
			return Utils.BintoBS("00000000", "0000"+val);
		case C1toNTx:
		case C1toNRx:
			return Utils.BintoBS("00000000", val+"0000");
		case C2toNTx:
		case C2toNRx:
			return Utils.BintoBS("0000"+val, "00000000");
		case C3toNTx:
		case C3toNRx:
			return Utils.BintoBS(val+"0000", "00000000");
		default: 
			return ByteString.empty();
		}
	}
	public static NTribSlot GetOption(ByteString mdata, bitMask msk){
		switch(msk){
		case C0:
			for(NTribSlot it:NTribSlot.values())
				if(mdata.equals(it.getData(Parameters.C0toNTx)))
					return it;
		case C1:
			for(NTribSlot it:NTribSlot.values())
				if(mdata.equals(it.getData(Parameters.C1toNTx)))
					return it;
		case C2:
			for(NTribSlot it:NTribSlot.values())
				if(mdata.equals(it.getData(Parameters.C2toNTx)))
					return it;
		case C3:
			for(NTribSlot it:NTribSlot.values())
				if(mdata.equals(it.getData(Parameters.C3toNTx)))
					return it;
		default:
			return NotSupported;
		}
	}
	/**
	 * @return the port
	 */
	public String getPort(){
		return this.port;
	}
	public int getPortAsInt(){
		if (this.port == "N0"){return 0; }
		else return  1;
	}
	
	@Override
	public String getParameterAsString() {
		return "TributarySlot";
	}
}
