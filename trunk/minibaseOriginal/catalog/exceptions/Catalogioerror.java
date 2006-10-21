package  catalog.exceptions;
import chainexception.*;

public class Catalogioerror extends ChainException {

   public Catalogioerror(Exception err, String name)
	{
	       super(err, name);
	}
}

