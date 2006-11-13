package  catalog.exceptions;
import chainexception.*;

public class IndexCatalogException extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = -180653938707111748L;

public IndexCatalogException(Exception err, String name)
    {
      super(err, name);
    }
}

