package iterator;

import chainexception.ChainException;

public class UnknownKeyTypeException extends ChainException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5999104374324360957L;
	public UnknownKeyTypeException() {super();}
	public UnknownKeyTypeException(String s) {super(null,s);}
	public UnknownKeyTypeException(Exception e, String s) {super(e,s);}
}
