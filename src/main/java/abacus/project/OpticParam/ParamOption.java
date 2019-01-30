package abacus.project.OpticParam;
import abacus.project.acc400Dev.intern.Parameters;
import akka.util.ByteString;
/**
 * interface for implementing optical parameter options
 */
/**
 * 
 * @author bogdan
 *
 *Interface for parameter options
 *returns a bytestring of each option
 *
 */
public interface ParamOption<T> {

	public ByteString getData(Parameters param);
	public String getParameterAsString();
}
