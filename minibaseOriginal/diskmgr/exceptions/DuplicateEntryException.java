package diskmgr.exceptions;
import chainexception.*;

public class DuplicateEntryException extends ChainException {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 2235221438700951431L;

public DuplicateEntryException(Exception e, String name)
    {
      super(e, name); 
    }
}

