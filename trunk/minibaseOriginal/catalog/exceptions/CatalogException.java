package  catalog.exceptions;

import chainexception.ChainException;

public class CatalogException extends ChainException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1410595058082496349L;
	
	public CatalogException( Exception _prev, String name)
	{
		super(_prev, name);
	}
}

