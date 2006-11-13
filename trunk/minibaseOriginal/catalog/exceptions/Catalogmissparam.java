package  catalog.exceptions;
import chainexception.*;

public class Catalogmissparam extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = 6647565542339831744L;

public Catalogmissparam(Exception err, String name)
	{
	       super(err, name);
	}
}

