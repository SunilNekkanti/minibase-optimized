package catalog.exceptions;

import chainexception.ChainException;

public class CatalogPKException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4109721885051860973L;

	public CatalogPKException(Exception err, String name)
	{
	       super(err, name);
	}

}
