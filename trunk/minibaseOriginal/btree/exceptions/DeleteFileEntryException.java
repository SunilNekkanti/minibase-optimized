package btree.exceptions;
import chainexception.*;

public class  DeleteFileEntryException  extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -6443592206728048455L;
public DeleteFileEntryException() {super();}
  public DeleteFileEntryException(String s) {super(null,s);}
  public DeleteFileEntryException(Exception e, String s) {super(e,s);}

}
