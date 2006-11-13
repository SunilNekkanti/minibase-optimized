package index.exceptions;

import chainexception.ChainException;

public class InvalidSelectionException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 5153085038633367082L;
public InvalidSelectionException() {super();}
  public InvalidSelectionException(String s) {super(null,s);}
  public InvalidSelectionException(Exception e, String s) {super(e,s);}
}
