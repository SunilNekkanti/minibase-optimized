package  catalog.exceptions;
import chainexception.*;

public class Catalogbadtype extends ChainException {

   public Catalogbadtype(Exception err, String name)
	{
	       super(err, name);
	}
}

