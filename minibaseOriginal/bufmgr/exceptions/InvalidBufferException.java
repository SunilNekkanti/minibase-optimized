package bufmgr.exceptions;
import chainexception.*;

public class InvalidBufferException extends ChainException{
  
  
  /**
	 * 
	 */
	private static final long serialVersionUID = -4767330672030957050L;

public InvalidBufferException(Exception e, String name)
    { 
      super(e, name); 
    }

}




