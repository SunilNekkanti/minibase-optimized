package btree.exceptions;
import chainexception.*;

public class KeyTooLongException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8279992556048837159L;
public KeyTooLongException() {super();}
  public KeyTooLongException(String s) {super(null,s);}
  public KeyTooLongException(Exception e, String s) {super(e,s);}

}
