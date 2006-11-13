package heap.exceptions;
import chainexception.*;

public class SpaceNotAvailableException extends ChainException{


  /**
	 * 
	 */
	private static final long serialVersionUID = -3883901080726230087L;

public SpaceNotAvailableException()
  {
     super();
  
  }

  public SpaceNotAvailableException(Exception ex, String name)
  {
    super(ex, name);
  }



}
