/* File hferr.java  */

package heap.exceptions;
import chainexception.*;

public class HFDiskMgrException extends ChainException{


  /**
	 * 
	 */
	private static final long serialVersionUID = 4146863555507371725L;

public HFDiskMgrException()
  {
     super();
  
  }

  public HFDiskMgrException(Exception ex, String name)
  {
    super(ex, name);
  }



}
