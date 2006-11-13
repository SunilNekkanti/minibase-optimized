package btree.exceptions;
import chainexception.*;

public class  GetFileEntryException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8568120076250352484L;
public GetFileEntryException() {super();}
  public GetFileEntryException(String s) {super(null,s);}
  public GetFileEntryException(Exception e, String s) {super(e,s);}

}
