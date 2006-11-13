package diskmgr.exceptions;
import chainexception.*;

public class OutOfSpaceException extends ChainException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -5299095849512952809L;

public OutOfSpaceException(Exception e, String name)
    { 
      super(e, name); 
    }
}

