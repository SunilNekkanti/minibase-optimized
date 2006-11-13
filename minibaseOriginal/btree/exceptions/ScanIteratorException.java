package btree.exceptions;
import chainexception.*;

public class ScanIteratorException extends ChainException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8861304385649673222L;
public ScanIteratorException() {super();}
  public ScanIteratorException(String s) {super(null,s);}
  public ScanIteratorException(Exception e, String s) {super(e,s);}

}
