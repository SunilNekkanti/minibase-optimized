package btree.exceptions;
import chainexception.*;

public class  LeafRedistributeException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 8464528771089427461L;
public LeafRedistributeException() {super();}
  public LeafRedistributeException(String s) {super(null,s);}
  public LeafRedistributeException(Exception e, String s) {super(e,s);}

}
