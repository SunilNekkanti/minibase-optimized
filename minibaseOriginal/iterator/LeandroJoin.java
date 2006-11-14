package iterator;


import global.AttrType;
import global.RID;
import global.SystemDefs;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import heap.exceptions.InvalidTupleSizeException;
import heap.exceptions.InvalidTypeException;
import index.IndexUtils;
import index.exceptions.IndexException;
import iterator.exceptions.JoinsException;
import iterator.exceptions.LowMemException;
import iterator.exceptions.NestedLoopException;
import iterator.exceptions.PredEvalException;
import iterator.exceptions.SortException;
import iterator.exceptions.TupleUtilsException;
import iterator.exceptions.UnknowAttrType;
import iterator.exceptions.UnknownKeyTypeException;

import java.io.IOException;

import btree.BTreeFile;
import btree.DataClass;
import btree.IndexFile;
import btree.IndexFileScan;
import btree.IntegerKey;
import btree.KeyClass;
import btree.KeyDataEntry;
import btree.LeafData;
import btree.exceptions.ConstructPageException;
import btree.exceptions.GetFileEntryException;
import btree.exceptions.PinPageException;
import bufmgr.exceptions.PageNotReadException;
/**
 * This file contains an implementation of the nested loops join algorithm as described 
 * in the Shapiro paper. The algorithm is extremely simple: foreach tuple r in R do foreach 
 * tuple s in S do if (ri == sj) then add (r, s) to the result.
 */

public class LeandroJoin  extends Iterator 
{
	/**
	 * @uml.property  name="_in1"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private AttrType _in1[];
	/**
	 * @uml.property  name="_in2"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private AttrType _in2[];
	private   int in2_len;
	private   Iterator  outer;
	private   short t2_str_sizescopy[];
	/**
	 * @uml.property  name="outputFilter"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private   CondExpr OutputFilter[];
	/**
	 * @uml.property  name="rightFilter"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private   boolean done;
	// Is the join complete
	private   boolean get_from_outer;
	private   Tuple outer_tuple;
	private   Tuple inner_tuple;
	private   Tuple     Jtuple;           // Joined tuple
	/**
	 * @uml.property  name="perm_mat"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private   FldSpec   perm_mat[];
	private   int        nOutFlds;
	private   Heapfile  hf;
	private   IndexFileScan      inner;
	private IndexFile innerIndexFile;
	private int join_col_1;	
	private String index_name;
	
	/**constructor
	 *Initialize the two relations which are joined, including relation type,
	 *@param in1  Array containing field types of R.
	 *@param len_in1  # of columns in R.
	 *@param t1_str_sizes shows the length of the string fields.
	 *@param in2  Array containing field types of S
	 *@param len_in2  # of columns in S
	 *@param  t2_str_sizes shows the length of the string fields.
	 *@param amt_of_mem  IN PAGES
	 *@param am1  access method for left i/p to join
	 *@param relationName  access hfapfile for right i/p to join
	 *@param outFilter   select expressions
	 *@param rightFilter reference to filter applied on right i/p
	 *@param proj_list shows what input fields go where in the output tuple
	 *@param n_out_flds number of outer relation fileds
	 *@exception IOException some I/O fault
	 *@exception NestedLoopException exception from this class
	 */
	public LeandroJoin( 
			AttrType    in1[],    
			short   t1_str_sizes[],
			AttrType    in2[],         
			short   t2_str_sizes[],   

			int     amt_of_mem,        

			Iterator     am1,          
			int     join_col_in1,                
			String relationName,      
			String index_2_name,
			CondExpr outFilter[], 
			FldSpec   proj_list[]
	) throws IOException,NestedLoopException
	{
		int       n_out_flds = proj_list.length;
		int     len_in1 = in1.length;
		int     len_in2 = in2.length;
		index_name = index_2_name;
		try {
			innerIndexFile = new BTreeFile(index_name);
		} catch (GetFileEntryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PinPageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConstructPageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		join_col_1 = join_col_in1;
		_in1 = new AttrType[in1.length];
		_in2 = new AttrType[in2.length];
		System.arraycopy(in1,0,_in1,0,in1.length);
		System.arraycopy(in2,0,_in2,0,in2.length);
		in2_len = len_in2;
		
		
		outer = am1;
		t2_str_sizescopy =  t2_str_sizes;
		inner_tuple = new Tuple();
		Jtuple = new Tuple();
		OutputFilter = outFilter;
		
		inner = null;
		done  = false;
		get_from_outer = true;
		
		AttrType[] Jtypes = new AttrType[n_out_flds];
		perm_mat = proj_list;
		nOutFlds = n_out_flds;
		try {
			TupleUtils.setup_op_tuple(Jtuple, Jtypes,
					in1, len_in1, in2, len_in2,
					t1_str_sizes, t2_str_sizes,
					proj_list, nOutFlds);
		}catch (TupleUtilsException e){
			throw new NestedLoopException(e,"TupleUtilsException is caught by NestedLoopsJoins.java");
		}
		
		
		
		try {
			hf = new Heapfile(relationName);
			
		}
		catch(Exception e) {
			throw new NestedLoopException(e, "Create new heapfile failed.");
		}
	}
	
	/**  
	 *@return The joined tuple is returned
	 *@exception IOException I/O errors
	 *@exception JoinsException some join exception
	 *@exception IndexException exception from super class
	 *@exception InvalidTupleSizeException invalid tuple size
	 *@exception InvalidTypeException tuple type not valid
	 *@exception PageNotReadException exception from lower layer
	 *@exception TupleUtilsException exception from using tuple utilities
	 *@exception PredEvalException exception from PredEval class
	 *@exception SortException sort exception
	 *@exception LowMemException memory error
	 *@exception UnknowAttrType attribute type unknown
	 *@exception UnknownKeyTypeException key type unknown
	 *@exception Exception other exceptions
	 
	 */
	public Tuple get_next()
	throws IOException,
	JoinsException ,
	IndexException,
	InvalidTupleSizeException,
	InvalidTypeException, 
	PageNotReadException,
	TupleUtilsException, 
	PredEvalException,
	SortException,
	LowMemException,
	UnknowAttrType,
	UnknownKeyTypeException,
	Exception
	{
		// This is a DUMBEST form of a join, not making use of any key information...
		
		
		if (done)
			return null;
		
		do
		{
			// If get_from_outer is true, Get a tuple from the outer, delete
			// an existing scan on the file, and reopen a new scan on the file.
			// If a get_next on the outer returns DONE?, then the nested loops
			//join is done too.
			
			if (get_from_outer)
			{
				get_from_outer = false;
				if (inner != null)     // If this not the first time,
				{
					// close scan
					inner = null;
				}
				
				SystemDefs.estadisticas++;				
				if ((outer_tuple=outer.get_next()) == null)
				{
					done = true;
					if (inner != null) 
					{
						
						inner = null;
					}
					
					return null;
				}
				
				try {
					// innerIndexFile es un indexFile!
					KeyClass rightValue = new IntegerKey(outer_tuple.getIntFld(join_col_1));
					//outer_tuple.getStrFld(fldNo)
					inner = IndexUtils.BTree_scan(rightValue,rightValue,innerIndexFile);
				}
				catch(Exception e){
					throw new NestedLoopException(e, "openScan failed");
				}

				
			}  // ENDS: if (get_from_outer == TRUE)
			
			
			// The next step is to get a tuple from the inner,
			// while the inner is not completely scanned && there
			// is no match (with pred),get a tuple from the inner.
			
			
			RID rid;
			KeyDataEntry inner_data = null;
			while ((inner_data = inner.get_next()) != null)
			{
				SystemDefs.estadisticas++;
				rid = ((LeafData)inner_data.data).getData();
				inner_tuple= hf.getRecord(rid);
				inner_tuple.setHdr( _in2,t2_str_sizescopy);
				if (PredEval.Eval(OutputFilter, outer_tuple, inner_tuple, 
						_in1, _in2))
				{
					// Apply a projection on the outer and inner tuples.
					Projection.Join(outer_tuple, _in1, 
							inner_tuple, _in2, 
							Jtuple, perm_mat, nOutFlds);
					return Jtuple;
				}
			}
			
			// There has been no match. (otherwise, we would have 
			//returned from the while loop. Hence, inner is 
			//exhausted, => set get_from_outer = TRUE, go to top of loop
			
			get_from_outer = true; // Loop back to top and get next outer tuple.	      
		} while (true);
	} 
	
	/**
	 * implement the abstract method close() from super class Iterator
	 *to finish cleaning up
	 *@exception IOException I/O error from lower layers
	 *@exception JoinsException join error from lower layers
	 *@exception IndexException index access error 
	 */
	public void close() throws JoinsException, IOException,IndexException 
	{
		if (!closeFlag) {
			
			try {
				outer.close();
			}catch (Exception e) {
				throw new JoinsException(e, "NestedLoopsJoin.java: error in closing iterator.");
			}
			closeFlag = true;
		}
	}
}






