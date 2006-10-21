package  catalog.exceptions;
import chainexception.*;

public class AttrCatalogException extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = 3272062316846733688L;

public AttrCatalogException(Exception err, String name)
    {
      super(err, name);
    }
}

