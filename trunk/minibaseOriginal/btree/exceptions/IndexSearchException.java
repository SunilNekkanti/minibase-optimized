package btree.exceptions;
import chainexception.*;

public class IndexSearchException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -7957697932170878369L;
public IndexSearchException() {super();}
  public IndexSearchException(String s) {super(null,s);}
  public IndexSearchException(Exception e, String s) {super(e,s);}

}
