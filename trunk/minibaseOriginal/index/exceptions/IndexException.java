package index.exceptions;

import chainexception.ChainException;

public class IndexException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -889160813758622559L;
public IndexException() {super();}
  public IndexException(String s) {super(null,s);}
  public IndexException(Exception e, String s) {super(e,s);}
}
