package iterator;

import chainexception.ChainException;

public class NoOutputBuffer extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9053393842080350969L;
	public NoOutputBuffer(String s){super(null,s);}
	public NoOutputBuffer(Exception prev, String s){super(prev,s);}
}
