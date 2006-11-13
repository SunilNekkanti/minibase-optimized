package btree.exceptions;
import chainexception.*;

public class RecordNotFoundException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -2697510927812388439L;
public RecordNotFoundException() {super();}
  public RecordNotFoundException(String s) {super(null,s);}
  public RecordNotFoundException(Exception e, String s) {super(e,s);}

}
