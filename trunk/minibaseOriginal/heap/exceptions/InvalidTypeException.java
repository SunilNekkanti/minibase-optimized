package heap.exceptions;
import chainexception.*;


public class InvalidTypeException extends ChainException {


  /**
	 * 
	 */
	private static final long serialVersionUID = -18140849084811686L;

public InvalidTypeException ()
  {
     super();
  }

  public InvalidTypeException (Exception ex, String name)
  {
    super(ex, name);
  }



}
