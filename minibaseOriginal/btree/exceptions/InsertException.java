package btree.exceptions;
import chainexception.*;

public class  InsertException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6123291830593692793L;
public InsertException() {super();}
  public InsertException(String s) {super(null,s);}
  public InsertException(Exception e, String s) {super(e,s);}

}
