package abacus.project.actorMsgs;

import akka.util.ByteString;
import com.google.common.io.BaseEncoding;
/**
 * acc400 device sub-command message used to control power and port state of the decive
 * @author BMA
 *
 */
public enum EbSubCmd {
	/**
	 * power on the UUT supply
	 */
	PW_ON{
		@Override
		public ByteString subAddr(){
			return ByteString.fromArray(BaseEncoding.base16().decode("0101"));
		}
	},
	/**
	 * power off the UUT supply
	 */
	PW_OFF{
		@Override
		public ByteString subAddr(){
			return ByteString.fromArray(BaseEncoding.base16().decode("0302"));
		}
	},
	/**
	 * get the MSA asserted pins
	 */
	GET_PINS{
		@Override
		public ByteString subAddr(){
			return ByteString.fromArray(BaseEncoding.base16().decode("0402"));
		}
	},
	/**
	 *Assert input PINS (used to assert low-power mode) 
	 */
	
	AS_PIN{
		@Override
		public ByteString subAddr(){
			return ByteString.fromArray(BaseEncoding.base16().decode("0403"));
		}
	},
	/**
	 * Dessert input PINS (used to de-assert low-power mode)
	 */
	DE_AS_PIN{
		@Override
		public ByteString subAddr(){
			return ByteString.fromArray(BaseEncoding.base16().decode("0202"));
		}
	},
	EMPTY{
			@Override
			public ByteString subAddr(){
				return ByteString.empty();
		}
	};
	private EbSubCmd(){};
	public abstract ByteString subAddr();
}
