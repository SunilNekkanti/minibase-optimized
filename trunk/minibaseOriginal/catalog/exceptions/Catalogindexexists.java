package  catalog.exceptions;
import chainexception.*;

public class Catalogindexexists extends ChainException {

 /**
	 * 
	 */
	private static final long serialVersionUID = 6575671617711125132L;

public Catalogindexexists()
   {
      super();
   }

   public Catalogindexexists(Exception err, String name)
	{
	       super(err, name);
	}
}

