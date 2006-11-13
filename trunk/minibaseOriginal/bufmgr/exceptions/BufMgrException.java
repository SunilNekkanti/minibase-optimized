package bufmgr.exceptions;
import chainexception.*;

public class BufMgrException extends ChainException{

  /**
	 * 
	 */
	private static final long serialVersionUID = -7580320871141384532L;

public BufMgrException(Exception e, String name)
  { super(e, name); }
 
}

