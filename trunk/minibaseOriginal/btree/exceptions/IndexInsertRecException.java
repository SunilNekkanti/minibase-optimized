package btree.exceptions;
import chainexception.*;

public class  IndexInsertRecException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 2575849456720729470L;
public IndexInsertRecException() {super();}
  public IndexInsertRecException(String s) {super(null,s);}
  public IndexInsertRecException(Exception e, String s) {super(e,s);}

}
