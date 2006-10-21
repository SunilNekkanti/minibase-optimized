package iterator.exceptions;

import chainexception.ChainException;

public class SortException extends ChainException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7442130113545204372L;
	public SortException(String s) {super(null,s);}
	public SortException(Exception e, String s) {super(e,s);}
}
