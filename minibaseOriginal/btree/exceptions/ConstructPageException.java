package btree.exceptions;
import chainexception.*;

public class ConstructPageException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -7369010209449518732L;
public ConstructPageException() {super();}
  public ConstructPageException(String s) {super(null,s);}
  public ConstructPageException(Exception e, String s) {super(e,s);}

}
