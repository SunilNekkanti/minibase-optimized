package  catalog.exceptions;
import chainexception.*;

public class Catalogbadtype extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = -1959960052835053384L;

public Catalogbadtype(Exception err, String name)
	{
	       super(err, name);
	}
}

