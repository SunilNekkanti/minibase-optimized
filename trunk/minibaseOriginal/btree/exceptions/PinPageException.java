package btree.exceptions;
import chainexception.*;

public class PinPageException   extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -14957718779509566L;
public PinPageException() {super();}
  public PinPageException(String s) {super(null,s);}
  public PinPageException(Exception e, String s) {super(e,s);}

}
