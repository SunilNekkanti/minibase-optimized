package heap.exceptions;
import chainexception.*;

public class InvalidUpdateException extends ChainException{


  /**
	 * 
	 */
	private static final long serialVersionUID = -2009665777182788023L;

public InvalidUpdateException ()
  {
     super();
  }

  public InvalidUpdateException (Exception ex, String name)
  {
    super(ex, name);
  }



}
