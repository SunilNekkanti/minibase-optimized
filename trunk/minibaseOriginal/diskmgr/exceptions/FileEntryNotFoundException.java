package diskmgr.exceptions;
import chainexception.*;


public class FileEntryNotFoundException extends ChainException {

  public FileEntryNotFoundException(Exception e, String name)
  { 
    super(e, name); 
  }

  


}




