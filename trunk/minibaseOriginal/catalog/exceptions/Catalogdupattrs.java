package  catalog.exceptions;
import chainexception.*;

public class Catalogdupattrs extends ChainException{

   public Catalogdupattrs(Exception err, String name)
	{
	       super(err, name);
	}
}

