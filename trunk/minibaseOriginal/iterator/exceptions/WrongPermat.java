package iterator.exceptions;

import chainexception.ChainException;

public class WrongPermat extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4635290870305780139L;
	public WrongPermat(String s){super(null,s);}
	public WrongPermat(Exception prev, String s){super(prev,s);}
}
