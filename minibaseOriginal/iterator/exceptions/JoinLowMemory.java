package iterator.exceptions;

import chainexception.ChainException;

public class JoinLowMemory extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5243768417151092734L;
	public JoinLowMemory(String s){super(null,s);}
	public JoinLowMemory(Exception prev, String s){super(prev,s);}
}
