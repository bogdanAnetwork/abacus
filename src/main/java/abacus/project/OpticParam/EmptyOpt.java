package abacus.project.OpticParam;

import java.io.Serializable;

import abacus.project.acc400Dev.intern.Parameters;
import akka.util.ByteString;
/**
 * (hack) empty parameter option as a null wrapper for immutable maps in DevConfMsg
 * @author BMA
 *
 */
public class EmptyOpt implements ParamOption, Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Override
	public ByteString getData(Parameters param) {
		return ByteString.empty();
	}
	//TODO: check if null is appropriate
	@Override
	public String getParameterAsString() {
		return "";
	}
	
}
