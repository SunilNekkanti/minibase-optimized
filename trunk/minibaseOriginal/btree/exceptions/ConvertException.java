package btree.exceptions;
import chainexception.*;

public class ConvertException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -7786281641437656544L;
public ConvertException() {super();}
  public ConvertException(String s) {super(null,s);}
  public ConvertException(Exception e, String s) {super(e,s);}
}
