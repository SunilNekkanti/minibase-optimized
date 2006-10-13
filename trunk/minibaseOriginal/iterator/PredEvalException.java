package iterator;
import chainexception.ChainException;

public class  PredEvalException extends ChainException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9188159335813873146L;
	public PredEvalException(String s){super(null,s);}
	public PredEvalException(Exception prev, String s){ super(prev,s);}
}
