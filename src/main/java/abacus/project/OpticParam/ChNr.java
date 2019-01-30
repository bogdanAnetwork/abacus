package abacus.project.OpticParam;

import abacus.project.acc400Dev.intern.Parameters;
import akka.util.ByteString;
/**
 * optical channel number 
 * @author BMA
 *
 */
public class ChNr extends Number implements ParamOption{
	/**
	 * The value of the {@code ChanOptions}.
	 * @serial
	 */
	private static final long serialVersionUID = 1999999999L;
	
	private final int value; 
	private final int max_chan=100;
	private final int min_chan=0;


	@Override
	public ByteString getData(Parameters prm) {
		//TODO: check the correct translation from int to bytestring
		return ByteString.fromInts(0, this.value);
	}
	public ChNr(int value){
		if (value>max_chan)
			throw new NumberFormatException("channel " + value +" greater than maximum 100 available");
		else if (value<min_chan)
			throw new NumberFormatException("channel " + value +" smaller than minimum 1 available");
		else this.value = value;
			
	}
	@Override
	/**
	 * Returns the value of this {@code ChanOptions} as an
	 * {@code int}.
	 */
       public int intValue() {
           return value;
       }
	@Override
	 /**
	  * Returns the value of this {@code Integer} as a
	  * {@code long}.
	  */
	public long longValue() {
		return (long)value;
	}

	@Override
	 /**
	  * Returns the value of this {@code Integer} as a
	  * {@code float}.
	  */
	public float floatValue() {
		return (float)value;
	}

	@Override
	 /**
	  * Returns the value of this {@code Integer} as a
	  * {@code double}.
	  */
	public double doubleValue() {
		return (double)value;
	}
	//return a new ChNr object matching the ByteString data
	public static ChNr getChNr(ByteString data){
		if (data.size()<=1) 
			return new ChNr(0);
		return new ChNr(data.drop(1).head());
	}
	@Override
	/**
	 * @return the option
	 * TODO:not a correct way
	 */
	public String getParameterAsString(){
		return "ChNr";
	}
	@Override
    /**
     * @return the option
     * TODO:not a correct way
     */
	public String toString(){
        return String.valueOf(value);
	}
}
