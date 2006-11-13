package  catalog.exceptions;
import chainexception.*;

public class Catalogattrnotfound extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = 3755417919959889373L;

public Catalogattrnotfound(Exception err, String name)
	{
	       super(err, name);
	}
}

