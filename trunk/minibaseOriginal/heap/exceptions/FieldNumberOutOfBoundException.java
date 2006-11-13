package heap.exceptions;
import chainexception.*;

public class FieldNumberOutOfBoundException extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = -5385179634718406966L;

public FieldNumberOutOfBoundException()
   {
      super();
   }
   
   public FieldNumberOutOfBoundException (Exception ex, String name)
   {
      super(ex, name); 
   }

}

