package heap.exceptions;
import chainexception.*;

public class InvalidTupleSizeException extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = -8005589005698303727L;

public InvalidTupleSizeException()
   {
      super();
   }
   
   public InvalidTupleSizeException(Exception ex, String name)
   {
      super(ex, name); 
   }

}

