package  catalog.exceptions;
import chainexception.*;

public class Catalogdupattrs extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = 1158186894706324026L;

public Catalogdupattrs(Exception err, String name)
	{
	       super(err, name);
	}
}

