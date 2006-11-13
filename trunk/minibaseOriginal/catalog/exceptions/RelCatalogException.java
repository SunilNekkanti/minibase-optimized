package  catalog.exceptions;
import chainexception.*;

public class RelCatalogException extends ChainException{

   /**
	 * 
	 */
	private static final long serialVersionUID = 5691416784760085257L;

public RelCatalogException(Exception err, String name)
    {
      super(err, name);
    }
}

