package  catalog.exceptions;
import chainexception.*;

public class Catalogindexnotfound extends ChainException {

 /**
	 * 
	 */
	private static final long serialVersionUID = -3293314029193032171L;

public Catalogindexnotfound()
   {
      super();
   }

   public Catalogindexnotfound(Exception err, String name)
	{
	       super(err, name);
	}
}

