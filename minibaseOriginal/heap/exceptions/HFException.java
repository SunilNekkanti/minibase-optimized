/* File hferr.java  */

package heap.exceptions;
import chainexception.*;

public class HFException extends ChainException{


  /**
	 * 
	 */
	private static final long serialVersionUID = 1633427656267223398L;

public HFException()
  {
     super();
  
  }

  public HFException(Exception ex, String name)
  {
    super(ex, name);
  }



}
