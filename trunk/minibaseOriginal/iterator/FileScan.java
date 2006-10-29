package iterator;


import global.AttrType;
import global.RID;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import heap.exceptions.FieldNumberOutOfBoundException;
import heap.exceptions.InvalidTupleSizeException;
import heap.exceptions.InvalidTypeException;
import iterator.exceptions.FileScanException;
import iterator.exceptions.InvalidRelation;
import iterator.exceptions.JoinsException;
import iterator.exceptions.PredEvalException;
import iterator.exceptions.TupleUtilsException;
import iterator.exceptions.UnknowAttrType;
import iterator.exceptions.WrongPermat;

import java.io.IOException;

import bufmgr.exceptions.PageNotReadException;

/**
 * open a heapfile and according to the condition expression to get output file, call get_next to get all tuples
 */
public class FileScan extends  Iterator
{
	/**
	 * @uml.property  name="_in1"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private AttrType[] _in1;
	private short in1_len;
	private short[] s_sizes; 
	private Heapfile f;
	private Scan scan;
	private Tuple     tuple1;
	private Tuple    Jtuple;
	private int nOutFlds;
	/**
	 * @uml.property  name="outputFilter"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private CondExpr[]  OutputFilter;
	/**
	 * @uml.property  name="perm_mat"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	public FldSpec[] perm_mat;
	
	
	
	/**
	 *constructor
	 *@param file_name heapfile to be opened
	 *@param in1[]  array showing what the attributes of the input fields are. 
	 *@param s1_sizes[]  shows the length of the string fields.
	 *@param len_in1  number of attributes in the input tuple
	 *@param n_out_flds  number of fields in the out tuple
	 *@param proj_list  shows what input fields go where in the output tuple
	 *@param outFilter  select expressions
	 *@exception IOException some I/O fault
	 *@exception FileScanException exception from this class
	 *@exception TupleUtilsException exception from this class
	 *@exception InvalidRelation invalid relation 
	 */
	public  FileScan (String  file_name,
			AttrType in1[],                
			short s1_sizes[],      
			FldSpec[] proj_list,
			CondExpr[]  outFilter        		    
	)
	throws IOException,
	FileScanException,
	TupleUtilsException, 
	InvalidRelation
	{   
		_in1 = in1; 
		in1_len = (short)in1.length;
		s_sizes = s1_sizes;
		nOutFlds = proj_list.length;
		
		Jtuple =  new Tuple();
		AttrType[] Jtypes = new AttrType[nOutFlds];
		TupleUtils.setup_op_tuple(Jtuple, Jtypes, in1, in1_len, s1_sizes, proj_list, nOutFlds);
		
		OutputFilter = outFilter;
		perm_mat = proj_list;
		 
		tuple1 =  new Tuple();
		
		try {
			tuple1.setHdr( _in1, s1_sizes);
		}catch (Exception e){
			throw new FileScanException(e, "setHdr() failed");
		}
		tuple1.size();
		
		try {
			f = new Heapfile(file_name);
			
		}
		catch(Exception e) {
			throw new FileScanException(e, "Create new heapfile failed");
		}
		
		try {
			scan = f.openScan();
		}
		catch(Exception e){
			throw new FileScanException(e, "openScan() failed");
		}
	}
	
	/**
	 *@return shows what input fields go where in the output tuple
	 */
	public FldSpec[] show()
	{
		return perm_mat;
	}
	
	/**
	 *@return the result tuple
	 *@exception JoinsException some join exception
	 *@exception IOException I/O errors
	 *@exception InvalidTupleSizeException invalid tuple size
	 *@exception InvalidTypeException tuple type not valid
	 *@exception PageNotReadException exception from lower layer
	 *@exception PredEvalException exception from PredEval class
	 *@exception UnknowAttrType attribute type unknown
	 *@exception FieldNumberOutOfBoundException array out of bounds
	 *@exception WrongPermat exception for wrong FldSpec argument
	 */
	public Tuple get_next()
	throws JoinsException,
	IOException,
	InvalidTupleSizeException,
	InvalidTypeException,
	PageNotReadException, 
	PredEvalException,
	UnknowAttrType,
	FieldNumberOutOfBoundException,
	WrongPermat
	{     
		RID rid = new RID();
		
		while(true) {
			
			tuple1 =  scan.getNext(rid);
			
			if(tuple1 == null) {
				return null;
			}
			
			tuple1.setHdr( _in1, s_sizes);
			
			if (PredEval.Eval(OutputFilter, tuple1, null, _in1, null)){
				Projection.Project(tuple1, _in1,  Jtuple, perm_mat, nOutFlds); 
				return  Jtuple;
			}        
		}
	}
	
	/**
	 *implement the abstract method close() from super class Iterator
	 *to finish cleaning up
	 */
	public void close() 
	{
		
		if (!closeFlag) {
			scan.closescan();
			closeFlag = true;
		} 
	}
	
}


