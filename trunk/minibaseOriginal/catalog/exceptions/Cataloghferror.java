package  catalog.exceptions;
import chainexception.*;

public class Cataloghferror extends ChainException {

 /**
	 * 
	 */
	private static final long serialVersionUID = 1704280921457767379L;

public Cataloghferror()
   {
      super();
   }

   public Cataloghferror(Exception err, String name)
	{
	       super(err, name);
	}
}

