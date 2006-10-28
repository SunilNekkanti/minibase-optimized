package  catalog.exceptions;
import chainexception.*;

public class Catalogbadattrcount extends ChainException {

   public class Catalogattrexists extends ChainException {
	
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

