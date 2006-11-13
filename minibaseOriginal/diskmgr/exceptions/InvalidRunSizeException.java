package diskmgr.exceptions;
import chainexception.*;

public class InvalidRunSizeException extends ChainException {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = -7062687939388964557L;

public InvalidRunSizeException(Exception e, String name)
    { 
      super(e, name); 
    }
}




