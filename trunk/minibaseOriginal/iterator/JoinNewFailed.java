package iterator;

import chainexception.ChainException;

public class JoinNewFailed extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8209554616404493278L;
	public JoinNewFailed(String s){super(null,s);}
	public JoinNewFailed(Exception prev, String s){super(prev,s);}
}
