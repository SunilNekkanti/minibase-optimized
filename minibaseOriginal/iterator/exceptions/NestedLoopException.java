package iterator.exceptions;

import chainexception.ChainException;

public class NestedLoopException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4716913092560866974L;
	public NestedLoopException(String s){super(null,s);}
	public NestedLoopException(Exception prev, String s){ super(prev,s);}
}
