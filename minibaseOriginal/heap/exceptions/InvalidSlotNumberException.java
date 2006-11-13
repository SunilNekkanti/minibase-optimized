package heap.exceptions;
import chainexception.*;


public class InvalidSlotNumberException extends ChainException {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2935101143467879005L;

public InvalidSlotNumberException ()
  {
     super();
  }

  public InvalidSlotNumberException (Exception ex, String name)
  {
    super(ex, name);
  }



}
