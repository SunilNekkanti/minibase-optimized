package iterator;
import chainexception.ChainException;

public class JoinsException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3039585538282829284L;
	public JoinsException(String s){super(null,s);}
	public JoinsException(Exception prev, String s){ super(prev,s);}
}
