package iterator.exceptions;
import chainexception.ChainException;

public class  DuplElimException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8463117762257013164L;
	public  DuplElimException(String s){super(null,s);}
	public  DuplElimException(Exception prev, String s){ super(prev,s);}
}
