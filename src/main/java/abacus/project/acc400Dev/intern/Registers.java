package abacus.project.acc400Dev.intern;

import akka.util.ByteString;

import com.google.common.io.BaseEncoding;

/**
 * Enumeration containing the registers needed to configure the specific test device
 * @author BMA
 *
 */
public enum Registers{
	//expmle registers
	//Can Assert channels (0 and 1) and Low Power
	Assert("BE32"),
	//contains data on Channel number(1 - 100) and spacing for Line 0
	SpChan0("BE23"),
	//contains data on Channel number(1 - 100) and spacing for Line 1
	SpChan1("BE22"),
	//contains the write ready information (1=ready; 0=not ready)
	Lane_Cp("AB27"),
	//contains data on modulation and FEC options for line 0
	ModFec0("BE22"),
	//contains data on modulation and FEC options for line 1
	ModFec1("BE22"),	
	//contains the write ready information (1=ready; 0=not ready)
	WR_RDY("BE22"),
	//TX client to network port mappings 
	CtoNTx("BE22"),
	//RX client to network port mappings
	CtoNRx("BE32"),
	//Device state 
	DEV_STATE("BE32"),
	//empty value
	NONE("");
	private ByteString val;
	Registers(String val){
		this.val=Utils.HextoBS(val);
	};
	//return the 
	public ByteString getRegAddr() {
		return val;
	}
}