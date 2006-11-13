package bufmgr.exceptions;
import chainexception.*;

public class BufferPoolExceededException extends ChainException{

  /**
	 * 
	 */
	private static final long serialVersionUID = 7243078291851132453L;

public BufferPoolExceededException(Exception e, String name)
  { super(e, name); }
 
}
