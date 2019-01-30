package abacus.project.acc400Dev.intern;

import abacus.project.OpticParam.ChNr;
import abacus.project.OpticParam.ChSp;
import abacus.project.OpticParam.DeviceState;
import abacus.project.OpticParam.FecOptions;
import abacus.project.OpticParam.LaneCp;
import abacus.project.OpticParam.ModOptions;
import abacus.project.OpticParam.NTribSlot;
import abacus.project.OpticParam.ParamOption;
import akka.util.ByteString;


/**
 *Parameters enumeration; each parameter contains the register associated, the bitmask and assert bit requirement
 *as well as a method for returning the parameter option from a byteSring using a mask 
 * @author BMA
 *
 */
public enum Parameters{
	// state of device
	STATE(Registers.DEV_STATE, "trs", bitMask.DvSt, false),
	//Modulation lane 0 and 1
	MOD_0(Registers.ModFec0, "N0", bitMask.MOD, true),
	MOD_1(Registers.ModFec1, "N1", bitMask.MOD, true),
	//FEC lane 0 and 1
	FEC_0(Registers.ModFec0, "N0",bitMask.FEC, true),
	FEC_1(Registers.ModFec1, "N1", bitMask.FEC, true),
	// lane 0 channel spacing
	ChSp_0(Registers.SpChan0, "N0", bitMask.ChSp, true),
	// lane 1 channel spacing
	ChSp_1(Registers.SpChan1, "N1",bitMask.ChSp, true),
	ChNr_0(Registers.SpChan0, "N0",bitMask.ChNr, true),
	// lane 1 channel number
	ChNr_1(Registers.SpChan0, "N1",bitMask.ChNr, true),
	// lane coupling
	LnCp(Registers.Lane_Cp, "trs", bitMask.LnCp, true),
	//client ports (0-3) to network tributary mappings (Rx-Tx)
	C3toNRx(Registers.CtoNRx, "C3", bitMask.C3, false),
	C2toNRx(Registers.CtoNRx, "C2", bitMask.C2, false),
	C1toNRx(Registers.CtoNRx, "C1", bitMask.C1, false),
	C0toNRx(Registers.CtoNRx, "C0", bitMask.C0, false),
	C3toNTx(Registers.CtoNTx, "C3", bitMask.C3, false),
	C2toNTx(Registers.CtoNTx, "C2", bitMask.C2, false),
	C1toNTx(Registers.CtoNTx, "C1", bitMask.C1, false),
	C0toNTx(Registers.CtoNTx, "C0", bitMask.C0, false),
	EMPTY(Registers.NONE, "trs", bitMask.NONE, false);
	// low power requirement for configuring the appropriate parameter
	private final boolean lo_pw;
	// parameters subject (general state or port)
	private final String sbj;
	// the bit mask applied for the parameter data
	private final bitMask mask;
	// the register associated for the parameter
	private final Registers reg;
	
	/**
	 * constructor
	 * @param reg register to write to
	 * @param mask applied to the relevant bits
	 * @param lo_pw low power requirement; true or false
	 */
	Parameters(Registers reg, String sbj, bitMask mask, boolean lo_pw) {
		this.reg = reg;
		this.sbj = sbj;
		this.lo_pw = lo_pw;
		this.mask = mask;
	}
	
	/**
	 * @return the low power requirement
	 */
	public boolean getLowPw() {
		return this.lo_pw;
	}

	/**
	 * @return the bit mask
	 */
	public bitMask getmask() {
		return this.mask;
	}
	/**
	 * 
	 * @return the subject of the parameter (port or general)
	 */
	public String getSbj(){
		return this.sbj;
	}
	/**
	 * @return the register
	 */
	public Registers getreg() {
		return this.reg;
	}
	/**
	 * Factory for returning the parameter option from the data given
	 * @param data to be converted in parameter option
	 * @return the equivalent parameter option
	 */
	public ParamOption getOption(ByteString data){
		switch(this){
		case STATE:
			return DeviceState.getState(masked(data, bitMask.DvSt));
		case MOD_0:
		case MOD_1:
			return ModOptions.getModulation(masked(data, bitMask.MOD));
		case FEC_0:
		case FEC_1:
			return FecOptions.getFec(masked(data, bitMask.FEC));
		case ChSp_0:
		case ChSp_1:
			return ChSp.getSpacing(masked(data, bitMask.ChSp));
		case LnCp:
			return LaneCp.getLaneSt(masked(data, bitMask.LnCp));
		case ChNr_1:
		case ChNr_0:
			return ChNr.getChNr(masked(data, bitMask.ChNr));
		case C3toNRx:
		case C3toNTx:
			return NTribSlot.GetOption(masked(data, bitMask.C3), bitMask.C3);
		case C2toNRx:
		case C2toNTx:
			return NTribSlot.GetOption(masked(data, bitMask.C2), bitMask.C2);
		case C1toNTx:
		case C1toNRx:
			return NTribSlot.GetOption(masked(data, bitMask.C1), bitMask.C1);
		case C0toNRx:
		case C0toNTx:
			return NTribSlot.GetOption(masked(data, bitMask.C0), bitMask.C0);	
		default:
			return null;
		}
	}
	/**
	 * factory for getting the correct interface
	 * @return integer of 0 or 1 representing the correct lane; -1 in case of general node parameter
	 */
	public int getInterface(){
		switch(this){
		case C0toNRx:
		case C0toNTx:
			return 2;
		case C1toNRx:
		case C1toNTx:
			return 3;
		case C2toNRx:
		case C2toNTx:
			return 4;
		case C3toNRx:
		case C3toNTx:
			return 5;
		case MOD_0:
		case FEC_0:
		case ChSp_0:
		case ChNr_0:
			return 0;
		case MOD_1:
		case FEC_1:
		case ChSp_1:
		case ChNr_1:
			return 1;
		default:
			return -1;
		}
	}
	/**
	 *  masks the data with the given mask and returns it in ByteString format
	 * @param data which will be masked
	 * @param msk that will be applied
	 * @return the masked bit data in bytestring format
	 */
	private static ByteString masked(ByteString data, bitMask msk) {
		byte[] out = new byte[data.toArray().length];
		for (int i = 0; i < data.toArray().length; i++)
			out[i] = (byte) (data.toArray()[i] & msk.getmaskBS().toArray()[i]);
		return ByteString.fromArray(out);
	}
}
