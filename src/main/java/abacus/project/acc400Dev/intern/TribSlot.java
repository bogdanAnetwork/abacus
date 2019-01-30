package abacus.project.acc400Dev.intern;
/**
 * class for holding the tributary slots and the ports associated for the device
 * @author BMA
 *
 */
public class TribSlot{
	public enum Slots {S0, S1, S2, S3};
	public String port;
	public Slots slot;
	
	/**
	 * constructor for tributary slots
	 * @param port
	 * @param slot
	 */
	TribSlot(String port, Slots slot){
		this.port=port;
		this.slot=slot;	
	}
	/**
	 * 
	 * @return the slot 
	 */
	public Slots getSlot(){
		return this.slot;
	}
	/**
	 * 
	 * @return the network port associated
	 */
	public String getPort(){
		return this.port;
	}
}
