package diskmgr.exceptions;
import chainexception.*;


public class FileEntryNotFoundException extends ChainException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -8900719559543453002L;

public FileEntryNotFoundException(Exception e, String name)
  { 
    super(e, name); 
  }

  


}




