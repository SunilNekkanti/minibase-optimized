package diskmgr.exceptions;
import chainexception.*;

public class InvalidPageNumberException extends ChainException {
  
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 4124925693036452943L;

public InvalidPageNumberException(Exception ex, String name) 
    { 
      super(ex, name); 
    }
}




