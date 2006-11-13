package btree.exceptions;
import chainexception.*;

public class NodeNotMatchException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -4568098329823961442L;
public NodeNotMatchException() {super();}
  public NodeNotMatchException(String s) {super(null,s);}
  public NodeNotMatchException(Exception e, String s) {super(e,s);}

}
