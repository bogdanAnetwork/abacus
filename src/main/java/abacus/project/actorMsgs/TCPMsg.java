package abacus.project.actorMsgs;

import abacus.project.acc400Dev.intern.Parameters;
import abacus.project.acc400Dev.intern.Registers;
import akka.util.ByteString;
/**
 * TCP based message exchanged between the device driver and the interface module 
 * @author BMA
 *
 */
public final class TCPMsg {
	private final ByteString data;
	private final MsgType mtype;
	private final Parameters param;
	private final boolean ok;
	private final EbSubCmd subcmd;
	/**
	 * configuration read request message constructor
	 * 
	 * @param mtype
	 * 			the message type (read, write, board command)
	 * @param param
	 * 		the parameter containing the register info and mask
	 */	
	public TCPMsg(MsgType mtype, Parameters param) {
		this(mtype, param, ByteString.empty(), true);
	}
	/**
	 * standard configuration message constructor
	 * 
	 * @param mtype
	 * 			the message type (read, write, board command)
	 * @param param
	 * 		the parameter containing the register info and mask
	 * @param data
	 *            data in the register
	 * @param ok  
	 * 			the state of the operation
	 * 
	 * @param subcmd
	 * 			evaluation board subcommand is null
	 */	
	public TCPMsg(MsgType mtype, Parameters param, ByteString data, boolean ok) {
		this.mtype = mtype;
		this.data = data;
		this.ok = ok;
		this.param=param;
		this.subcmd = EbSubCmd.EMPTY;

	}
	/**
	 * evaluation board control message constructor
	 * 
	 * @param mtype
	 * 			the message type (read, write, board command)
	 * @param param
	 * 		the parameter containing the register info and mask
	 * @param data
	 *            data in the register
	 * @param ok  
	 * 			the state of the operation
	 * 
	 * @param subcmd
	 * 			evaluation board subcommand 
	 */	
	public TCPMsg(MsgType mtype, EbSubCmd subcmd, ByteString data, boolean ok){
		this.mtype = mtype;
		this.subcmd = subcmd;
		this.data = data;
		this.ok = ok;
		this.param=Parameters.EMPTY;
	} 
	/**
	 * @return the register address
	 */
	public Registers getRegister() {
		return this.param.getreg();
	}

	/**
	 * @return the data in the register
	 */
	public ByteString getData() {
		return this.data;
	}

	/**
	 * @return the register address
	 */
	public ByteString getRegisterAdr() {
		return this.param.getreg().getRegAddr();
	}
	public Parameters getParameter() {
		return this.param;
	}
	/**
	 * @return the message type, R/WR/B_CMD
	 */
	public MsgType getMType() {
		return this.mtype;
	}
	/**
	 * @return the status of operation -- 1=write; 0=read
	 */
	public boolean getStatus() {
		return this.ok;
	}
	/**
	 * @return the evaluation board subcommand
	 */	
	public EbSubCmd getSubCmd() {
		return subcmd;
	}
	public ByteString getSubCmdAdr() {
		return subcmd.subAddr();
	}

	@Override
	public String toString(){
		StringBuilder str =  new StringBuilder();
		return str.append("Message type :").append(this.getMType()).append("; Parameter :").append(this.param).toString();
	}
}
