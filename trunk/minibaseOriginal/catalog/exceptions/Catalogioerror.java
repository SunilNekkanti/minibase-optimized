package  catalog.exceptions;
import chainexception.*;

public class Catalogioerror extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = 5906367704897687571L;

public Catalogioerror(Exception err, String name)
	{
	       super(err, name);
	}
}

