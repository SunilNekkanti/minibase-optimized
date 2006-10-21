package iterator.exceptions;
import chainexception.ChainException;

public class TupleUtilsException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9206850805223753575L;
	public TupleUtilsException(String s){super(null,s);}
	public TupleUtilsException(Exception prev, String s){ super(prev,s);}
}
