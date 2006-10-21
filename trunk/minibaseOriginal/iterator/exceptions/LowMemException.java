package iterator.exceptions;

import chainexception.ChainException;

public class LowMemException extends ChainException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6695273432676484775L;
	public LowMemException(String s) {super(null,s);}
	public LowMemException(Exception e,String s) {super(e,s);}
}
