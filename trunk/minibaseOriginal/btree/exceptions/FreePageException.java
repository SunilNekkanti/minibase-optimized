package btree.exceptions;
import chainexception.*;

public class  FreePageException  extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 2612394544816421694L;
public FreePageException() {super();}
  public FreePageException(String s) {super(null,s);}
  public FreePageException(Exception e, String s) {super(e,s);}

}
