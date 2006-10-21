package diskmgr.exceptions;
import chainexception.*;


public class FileNameTooLongException extends ChainException {
  
  public FileNameTooLongException(Exception ex, String name)
    { 
      super(ex, name); 
    }
  
}




