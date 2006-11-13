package  catalog.exceptions;
import chainexception.*;

public class Catalogattrexists extends ChainException {

	   /**
	 * 
	 */
	private static final long serialVersionUID = 9163178880034358522L;

	public Catalogattrexists(Exception err, String name)
		{
		       super(err, name);
		}
}

