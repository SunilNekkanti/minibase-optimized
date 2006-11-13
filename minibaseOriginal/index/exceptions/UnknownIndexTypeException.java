package index.exceptions;

import chainexception.ChainException;

public class UnknownIndexTypeException extends ChainException 
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8322690783738163225L;
public UnknownIndexTypeException() {super();}
  public UnknownIndexTypeException(String s) {super(null,s);}
  public UnknownIndexTypeException(Exception e, String s) {super(e,s);}
}
