package abacus.project.ietf.extensions;

import abacus.project.OpticParam.ChNr;
import abacus.project.OpticParam.ChSp;
import abacus.project.OpticParam.FecOptions;
import abacus.project.OpticParam.ModOptions;
/**
 * Device interface class 
 * @author BMA
 *
 */
public class DevInterface{
	public int ID;
	public ChNr chNr;
	public ChSp chSp;

	/**
	 * interface constructor, name and type are final
	 * @param type string must be "input_port" or "output_port" else will not be handled
	 */
	public DevInterface(int ID, ChSp chSp, ChNr chNr){
		this.ID = ID;
		this.chNr = chNr;
		this.chSp = chSp;
	}
	
	public void setChanSp(ChSp chSp){
			this.chSp = chSp;
	}
	/**
	 * setting the channel number
	 * @param chNr
	 */
	public void setChanNr(ChNr chNr){
			this.chNr = chNr;
	}
	/**
	 * 
	 * @return the interface ID 
	 */
	public int getID(){
		return this.ID;
	}
	public ChNr getChNr(){
		return this.chNr;
	}
	public ChSp getChSp(){
		return this.chSp;
	}
	
}

