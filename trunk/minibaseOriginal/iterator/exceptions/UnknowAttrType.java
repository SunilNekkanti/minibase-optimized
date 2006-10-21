package iterator.exceptions;

import chainexception.ChainException;

public class UnknowAttrType extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1773096264793556102L;
	public UnknowAttrType(String s){super(null,s);}
	public UnknowAttrType(Exception prev, String s){super(prev,s);}
}
