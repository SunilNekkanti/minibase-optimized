package btree.exceptions;
import chainexception.ChainException;

public class ScanDeleteException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -1225015886389388930L;
public ScanDeleteException() {super();}
  public ScanDeleteException(String s) {super(null,s);}
  public ScanDeleteException(Exception e, String s) {super(e,s);}

}
