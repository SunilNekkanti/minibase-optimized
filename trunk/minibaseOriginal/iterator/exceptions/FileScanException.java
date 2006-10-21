package iterator.exceptions;
import chainexception.ChainException;

public class FileScanException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6043151480651969036L;
	public FileScanException(String s){super(null,s);}
	public FileScanException(Exception prev, String s){ super(prev,s);}
}
