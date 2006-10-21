package diskmgr.exceptions;
import chainexception.*;

public class InvalidRunSizeException extends ChainException {
  
  public InvalidRunSizeException(Exception e, String name)
    { 
      super(e, name); 
    }
}




