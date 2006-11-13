package btree.exceptions;
import chainexception.*;

public class KeyNotMatchException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1488582557412041501L;
public KeyNotMatchException() {super();}
  public KeyNotMatchException(String s) {super(null,s);}
  public KeyNotMatchException(Exception e, String s) {super(e,s);}

}
