package btree.exceptions;
import chainexception.*;

public class IndexFullDeleteException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -1865853012798149567L;
public IndexFullDeleteException() {super();}
  public IndexFullDeleteException(String s) {super(null,s);}
  public IndexFullDeleteException(Exception e, String s) {super(e,s);}

}
