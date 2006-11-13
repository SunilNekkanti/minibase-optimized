package  catalog.exceptions;
import chainexception.*;

public class Catalogbadattrcount extends ChainException {

   /**
	 * 
	 */
	private static final long serialVersionUID = 2678562718057382147L;

public class Catalogattrexists extends ChainException {
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = -2384385539785513575L;

	public Catalogattrexists(Exception err, String name)
		{
		       super(err, name);
		}
	}

public Catalogbadattrcount(Exception err, String name)
	{
	       super(err, name);
	}
}

