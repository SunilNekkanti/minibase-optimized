package diskmgr.exceptions;
import chainexception.*;


public class FileNameTooLongException extends ChainException {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 7581894841186168845L;

public FileNameTooLongException(Exception ex, String name)
    { 
      super(ex, name); 
    }
  
}




