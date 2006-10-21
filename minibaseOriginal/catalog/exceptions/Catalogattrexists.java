package  catalog.exceptions;
import chainexception.*;

public class Catalogattrexists extends ChainException {

   public Catalogattrexists(Exception err, String name)
	{
	       super(err, name);
	}
}

