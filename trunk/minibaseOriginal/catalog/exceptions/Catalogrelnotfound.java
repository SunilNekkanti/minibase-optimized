package  catalog.exceptions;
import chainexception.*;

public class Catalogrelnotfound extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = 6430490281549239167L;

public Catalogrelnotfound(Exception err, String name)
	{
	       super(err, name);
	}
}

