package  catalog.exceptions;
import chainexception.*;

public class Catalogattrnotfound extends ChainException {

   public Catalogattrnotfound(Exception err, String name)
	{
	       super(err, name);
	}
}

