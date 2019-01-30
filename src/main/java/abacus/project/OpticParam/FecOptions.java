package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import akka.util.ByteString;
/**
 * Forward Error Correction Options
 * @author BMA
 *
 */
	public enum FecOptions implements ParamOption{
		//15% SDFEC
		_15_SD(Utils.BintoBS("00000000", "00000000")),
		//overhead AC100 MSA-100GLH compatible SDFEC
		_15_OV(Utils.BintoBS("00000000", "00000001")),
		//25% FEC
		_25( Utils.BintoBS("00000000", "00000010")),
		//unrecognized option; not supported 
		NotSupported(ByteString.empty());
		private final ByteString val;
		FecOptions(ByteString val){
			this.val=val;
		}
		@Override
		public ByteString getData(Parameters prm){
			//TODO: add a checking step for the parameter received
			return val;
		}
		//Identify and return the enum constant from the given data ByteString; if not found return NotSupported;
		public static FecOptions getFec(ByteString data){
			for (FecOptions it:FecOptions.values())
				if(data.equals(it.getData(Parameters.FEC_0)))
					return it;
			//in case of unrecognized option send back not supported 
			return NotSupported;
		}
		@Override
		public String getParameterAsString(){
			return "FEC";
			
		}
	}

