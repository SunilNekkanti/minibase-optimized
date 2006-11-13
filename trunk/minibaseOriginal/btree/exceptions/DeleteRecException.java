package btree.exceptions;
import chainexception.*;

public class DeleteRecException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 8774423245821525977L;
public DeleteRecException() {super();}
  public DeleteRecException(String s) {super(null,s);}
  public DeleteRecException(Exception e, String s) {super(e,s);}

}
