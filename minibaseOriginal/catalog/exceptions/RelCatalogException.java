package  catalog.exceptions;
import chainexception.*;

public class RelCatalogException extends ChainException{

   public RelCatalogException(Exception err, String name)
    {
      super(err, name);
    }
}

