/* File hferr.java  */

package heap.exceptions;
import chainexception.*;

public class HFDiskMgrException extends ChainException{


  public HFDiskMgrException()
  {
     super();
  
  }

  public HFDiskMgrException(Exception ex, String name)
  {
    super(ex, name);
  }



}
