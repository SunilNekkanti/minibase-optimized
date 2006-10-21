package iterator.exceptions;

import chainexception.ChainException;

public class IteratorBMException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5651359507056417872L;
	public IteratorBMException(String s){super(null,s);}
	public IteratorBMException(Exception prev, String s){ super(prev,s);}
}
