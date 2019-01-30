package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Utils;
import akka.util.ByteString;
/**
 * optical modulation options
 * @author BMA
 *
 */
	public enum ModOptions implements ParamOption<ModOptions>{
		DP_QPSK_CP( Utils.BintoBS("00000011", "00000000")),
		DP_QPSK_I( Utils.BintoBS("00000001", "00000000")), 
		DP_8QAM_CP( Utils.BintoBS("00000010", "00000000")),  
		DP_16QAM_I( Utils.BintoBS("00000000", "00000000")),
		DP_16QAM_CP( Utils.BintoBS("00000100", "00000000")),
		//unrecognized option; not supported 
		NotSupported(ByteString.empty());
		private final ByteString val;
		ModOptions(ByteString val){
			this.val=val;
		}
		@Override
		public ByteString getData(Parameters prm){
			//TODO: add a check for the parameter received
			return val;
		}
		//Identify and return the enum constant from the given data ByteString
		public static ModOptions getModulation(ByteString data){
			for (ModOptions it:ModOptions.values())
				if(data.equals(it.getData(Parameters.MOD_0)))
					return it;
			//in case of unrecognized send back unsupported
			return NotSupported;
		}
		@Override
		public String getParameterAsString() {
			return "MOD";
		}
	}

