package btree.exceptions;
import chainexception.*;

public class IteratorException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -7694022752385349936L;
public IteratorException() {super();}
  public IteratorException(String s) {super(null,s);}
  public IteratorException(Exception e, String s) {super(e,s);}

}
