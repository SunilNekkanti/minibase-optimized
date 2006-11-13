package btree.exceptions;
import chainexception.*;

public class InsertRecException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -2956639394146165382L;
public InsertRecException() {super();}
  public InsertRecException(String s) {super(null,s);}
  public InsertRecException(Exception e, String s) {super(e,s);}

}
