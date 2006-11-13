package btree.exceptions;
import chainexception.*;

public class RedistributeException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -6201640205598790416L;
public RedistributeException() {super();}
  public RedistributeException(String s) {super(null,s);}
  public RedistributeException(Exception e, String s) {super(e,s);}

}
