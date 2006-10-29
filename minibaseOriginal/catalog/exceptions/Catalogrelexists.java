package  catalog.exceptions;
import chainexception.*;

public class Catalogrelexists extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = 1429917628895177006L;

public Catalogrelexists(Exception err, String name)
	{
	       super(err, name);
	}
}

