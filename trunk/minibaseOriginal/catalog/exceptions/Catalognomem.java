package  catalog.exceptions;
import chainexception.*;

public class Catalognomem extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = -4522187874844370502L;

public Catalognomem(Exception err, String name)
	{
	       super(err, name);
	}
}

