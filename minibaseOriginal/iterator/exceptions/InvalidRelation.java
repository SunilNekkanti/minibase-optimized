package iterator.exceptions;
import chainexception.ChainException;

public class InvalidRelation extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2817390822792502722L;
	public InvalidRelation(String s){super(null,s);}
	public InvalidRelation(Exception prev, String s){ super(prev,s);}
}
