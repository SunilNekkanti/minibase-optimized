package heap.exceptions;
import chainexception.*;

public class FileAlreadyDeletedException extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = 4821013835931040057L;

public FileAlreadyDeletedException()
   {
      super();
   }
   
   public FileAlreadyDeletedException(Exception ex, String name)
   {
      super(ex, name); 
   }

}
