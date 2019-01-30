package abacus.project.acc400Dev.intern;

import akka.util.ByteString;
/**
 * bit mask used for device configuration parameters
 * @author BMA
 *
 */
public enum bitMask{
	//device state 
	DvSt(Utils.BintoBS("00000001","11111111")),
	//modulation
	MOD(Utils.BintoBS("00001111","00000000")),
	//Forward Error Correction
	FEC(Utils.BintoBS("00000000","00000111")),
	//Channel Spacing
	ChSp(Utils.BintoBS("11100000","00000000")),
	//Channel Number
	ChNr(Utils.BintoBS("00000011","11111111")),
	//Line Coupling
	LnCp(Utils.BintoBS("00000000","00000011")),
	//Client to Network mappings
	//client 3
	C3(Utils.BintoBS("11110000","00000000")),
	//client 2
	C2(Utils.BintoBS("00001111","00000000")),
	//client 1
	C1(Utils.BintoBS("00000000","11110000")),
	//client 1
	C0(Utils.BintoBS("00000000","00001111")),
	//empty mask ****
	NONE(Utils.BintoBS("0", "0"));
	private ByteString mask;
	bitMask(ByteString mask){
		this.mask=mask;
	}
	//return the mask in byteString
	public ByteString getmaskBS(){
		return this.mask;
	}
}	