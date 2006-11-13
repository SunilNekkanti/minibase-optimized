package btree.exceptions;
import chainexception.*;

public class LeafInsertRecException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 2170429865159838302L;
public LeafInsertRecException() {super();}
  public LeafInsertRecException(String s) {super(null,s);}
  public LeafInsertRecException(Exception e, String s) {super(e,s);}

}
