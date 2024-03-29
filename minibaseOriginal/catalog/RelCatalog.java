//------------------------------------
//RelCatalog.java

//Ning Wang, April,24  1998
//-------------------------------------

package catalog;

import global.AttrType;
import global.Catalogglobal;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.IndexType;
import global.RID;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import heap.exceptions.FieldNumberOutOfBoundException;
import heap.exceptions.InvalidTupleSizeException;
import heap.exceptions.InvalidTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import catalog.exceptions.Catalogattrnotfound;
import catalog.exceptions.Catalogbadtype;
import catalog.exceptions.Catalogdupattrs;
import catalog.exceptions.Cataloghferror;
import catalog.exceptions.Catalogindexnotfound;
import catalog.exceptions.Catalogioerror;
import catalog.exceptions.Catalogmissparam;
import catalog.exceptions.Catalognomem;
import catalog.exceptions.Catalogrelexists;
import catalog.exceptions.Catalogrelnotfound;
import catalog.exceptions.RelCatalogException;

import bufmgr.exceptions.BufMgrException;
import diskmgr.exceptions.DiskMgrException;
/**
 * @author  Fernando
 */
public class RelCatalog extends Heapfile
implements  GlobalConst, Catalogglobal
{
	// Helps runStats
	//Status genStats(RelDesc &relRec, AttrDesc *&attrRecs);

	// CONSTRUCTOR
	RelCatalog(String filename)
	throws IOException, 
	BufMgrException,
	DiskMgrException,
	Exception
	{
		super(filename);

		tuple = new Tuple(Tuple.max_size);

		attrs = new AttrType[5];
		attrs[0] = new AttrType(AttrType.attrString);
		attrs[1] = new AttrType(AttrType.attrInteger);
		attrs[2] = new AttrType(AttrType.attrInteger);
		attrs[3] = new AttrType(AttrType.attrInteger);
		attrs[4] = new AttrType(AttrType.attrInteger);

		str_sizes = new short[5];
		str_sizes[0] = (short)MAXNAME;

		try {
			tuple.setHdr( attrs, str_sizes);
		}
		catch (Exception e) {
			System.err.println ("tuple.setHdr"+e);
			throw new RelCatalogException(e, "setHdr() failed");
		}
	};

	public List<String> getRelationNames () throws InvalidTupleSizeException, IOException, FieldNumberOutOfBoundException, InvalidTypeException{
		List<String> names = new ArrayList<String> ();
		Scan scan = null;

		scan = openScan();

		Tuple tuple = null;
		while((tuple = scan.getNext(new RID())) != null){
			tuple.setHdr( attrs, str_sizes);
			names.add(tuple.getStrFld(1));
		}

		scan.closescan();
		return names;
	}
	// GET RELATION DESCRIPTION FOR A RELATION
	public RelDesc getInfo(String relation)
	throws Catalogmissparam, 
	Catalogioerror, 
	Cataloghferror,
	RelCatalogException, 
	IOException, 
	Catalogrelnotfound
	{
		if (relation == null){
			throw new Catalogmissparam(null, "MISSING_PARAM");
		}

		Scan pscan = null;
		try {
			pscan = this.openScan();
		}
		catch (Exception e1) {
			System.err.println ("Scan"+e1);
			throw new RelCatalogException(e1, "scan failed");
		}

		while (true) {
			Tuple tuple = null;
			try {
				RID rid = new RID();
				tuple = pscan.getNext(rid);
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (tuple == null){
				throw new Catalogrelnotfound(null, "Catalog: Relation not Found!");
			}

			try {
				tuple.setHdr(attrs, str_sizes);
			} catch (InvalidTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RelDesc record = read_tuple(tuple);

			if (record.relName.equalsIgnoreCase(relation)){
				pscan.closescan();
				return record;
			}
		}
	};

	// CREATE A NEW RELATION
	public void createRel(String relation,  attrInfo [] attrList)
	throws Catalogmissparam, 
	Catalogrelexists, 
	Catalogdupattrs, 
	Catalognomem,
	IOException, 
	RelCatalogException,
	Catalogioerror,
	Cataloghferror
	{
		int attrCnt = attrList.length;

		if (relation== null  || attrCnt < 1){
			throw new Catalogmissparam(null, "MISSING_PARAM");
		}

		boolean status = true;
		RelDesc rd = null;
		try {
			rd = getInfo(relation);
		}
		catch (Catalogioerror e) {
			System.err.println ("Catalog I/O Error!"+e);
			throw new Catalogioerror(null, "");
		}
		catch (Cataloghferror e1) {
			System.err.println ("Catalog Heapfile Error!"+e1);
			throw new Cataloghferror(null, "");
		}
		catch (Catalogmissparam e2) {
			System.err.println ("Catalog Missing Param Error!"+e2);
			throw new Catalogmissparam(null, "");
		}
		catch (Catalogrelnotfound e3) {
			status = false;
		}

		if (status){
			throw new Catalogrelexists(null, "Relation Exists!");
		}

		// MAKE SURE THERE ARE NO DUPLICATE ATTRIBUTE NAMES		
		for(int i = 0; i < attrCnt; i++) {
			/* Duplicate attributes.*/
			for(int j = 0; j < i; j++){
				if (attrList[i].attrName.equalsIgnoreCase(attrList[j].attrName)){
					throw new Catalogdupattrs(null, "Duplicate Attributes!");
				}
			}
		}

		rd = new RelDesc();
		rd.relName = new String(relation);
		rd.attrCnt = attrCnt;
		rd.indexCnt = 0;
		rd.numTuples = NUMTUPLESFILE;
		rd.numPages = NUMPAGESFILE;

		try {
			addInfo(rd);
		}
		catch (Exception e2) {
			System.err.println ("addInfo"+e2);
			throw new RelCatalogException(e2, "addInfo failed");
		}

		int offset = 0;
		AttrDesc ad = new AttrDesc();
		ad.relName = new String(relation);

		for (int i=0; i< attrCnt; i++) {
			ad.attrName = new String(attrList[i].attrName);
			ad.attrOffset = offset;
			ad.attrType = new AttrType(attrList[i].attrType.attrType);
			ad.indexCnt = 0;
			ad.attrPos = i + 1;   // field position in the record
			ad.setPk(attrList[i].pk);

			if(attrList[i].attrType.attrType == AttrType.attrString) {
				ad.attrLen = attrList[i].attrLen;
				ad.maxVal.strVal = new String("Z");
				ad.minVal.strVal = new String("A");
			}
			else if(attrList[i].attrType.attrType == AttrType.attrInteger) {
				ad.attrLen = 4;
				ad.minVal.intVal = MINNUMVAL;
				ad.maxVal.intVal = MAXNUMVAL;
			}
			else if(attrList[i].attrType.attrType == AttrType.attrReal) {
				ad.attrLen = 4;
				ad.minVal.floatVal = MINNUMVAL;
				ad.maxVal.floatVal = MAXNUMVAL;
			}

			try {
				ExtendedSystemDefs.MINIBASE_ATTRCAT.addInfo(ad);
			}
			catch (Exception e2) {
				System.err.println ("addInfo"+e2);
				throw new RelCatalogException(e2, "addInfo() failed");
			}

			offset += ad.attrLen;
		}

		// NOW CREATE HEAPFILE

		try {
			Heapfile rel = new Heapfile(relation);
			if (rel == null)
				throw new Catalognomem(null, "NO Memory!");
		}
		catch (Exception e2) {
			System.err.println ("Heapfile"+e2);
			throw new RelCatalogException(e2, "create heapfile failed");
		}

	};

	// ADD AN INDEX TO A RELATION
	public void addIndex(String relation, String attrName,
			IndexType accessType, int buckets)
	throws RelCatalogException,
	IOException,
	Catalogioerror, 
	Cataloghferror, 
	Catalogmissparam,
	java.lang.Exception, 
	Catalogindexnotfound, 
	Catalognomem,
	Catalogbadtype, 
	Catalogattrnotfound,
	Exception

	{
		RelDesc rd = null;

		if ((relation == null)||(attrName == null))
			throw new Catalogmissparam(null, "MISSING_PARAM");

		// GET RELATION DATA
		try {
			rd= getInfo(relation);
		}
		catch (Catalogioerror e) {
			System.err.println ("Catalog I/O Error!"+e);
			throw new Catalogioerror(null, "");
		}
		catch (Cataloghferror e1) {
			System.err.println ("Catalog Heapfile Error!"+e1);
			throw new Cataloghferror(null, "");
		}
		catch (Catalogmissparam e2) {
			System.err.println ("Catalog Missing Param Error!"+e2);
			throw new Catalogmissparam(null, "");
		}


		// CREATE INDEX FILE
		try {
			ExtendedSystemDefs.MINIBASE_INDCAT.addIndex(relation, attrName,accessType, 0);
		}
		catch (Exception e2) {
			System.err.println ("addIndex"+e2);
			throw new RelCatalogException(e2, "addIndex failed");
		}

		// MODIFY INDEXCNT IN RELCAT
		rd.indexCnt++;

		try {
			removeInfo(relation);
			addInfo(rd);
		}
		catch (Catalogmissparam e4) {
			throw e4;
		}
		catch (Exception e2) {
			throw new RelCatalogException(e2, "add/remove info failed");
		}

	};



	// ADD INFORMATION ON A RELATION TO  CATALOG
	public void addInfo(RelDesc record)
	throws RelCatalogException, 
	IOException
	{
		try {
			make_tuple(tuple, record);
		}
		catch (Exception e4) {
			System.err.println ("make_tuple"+e4);
			throw new RelCatalogException(e4, "make_tuple failed");
		}

		try {
			insertRecord(tuple.getTupleByteArray());
		}
		catch (Exception e2) {
			System.err.println ("insertRecord"+e2);
			throw new RelCatalogException(e2, "insertRecord failed");
		}
	};

	// REMOVE INFORMATION ON A RELATION FROM CATALOG
	public void removeInfo(String relation)
	throws RelCatalogException, 
	IOException, 
	Catalogmissparam,
	Catalogattrnotfound
	{		
		if (relation == null)
			throw new Catalogmissparam(null, "MISSING_PARAM");

		Scan pscan = null;
		try {
			pscan = this.openScan();
		}
		catch (Exception e1) {
			System.err.println ("Scan"+e1);
			throw new RelCatalogException(e1, "scan failed");
		}



		while(true) {
			RID rid = new RID();
			RelDesc record = null;

			Tuple tuple = null;
			try {
				tuple = pscan.getNext(rid);
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (tuple == null) 
				throw new Catalogattrnotfound(null,
						"Catalog Attribute not Found!");
			try {
				tuple.setHdr( attrs, str_sizes);
				record = read_tuple(tuple);
			}
			catch (Exception e4) {
				System.err.println ("read_tuple"+e4);
				throw new RelCatalogException(e4, "read_tuple failed");
			}

			if (record.relName.equalsIgnoreCase(relation)==true) {
				try {
					deleteRecord(rid);
				}
				catch (Exception e3) {
					System.err.println ("deleteRecord"+e3);
					throw new RelCatalogException(e3, "deleteRecord failed");
				}
				pscan.closescan();
				return;
			}
		}
	};

	// Converts AttrDesc to tuple.
	public void make_tuple(Tuple tuple, RelDesc record)
	throws IOException, 
	RelCatalogException
	{
		try {
			tuple.setStrFld(1, record.relName);
			tuple.setIntFld(2, record.attrCnt);
			tuple.setIntFld(3, record.indexCnt);
			tuple.setIntFld(4, record.numTuples);
			tuple.setIntFld(5, record.numPages);
		}
		catch (Exception e1) {
			System.err.println ("setFld"+e1);
			throw new RelCatalogException(e1, "setFld failed");
		}

	};

	public  RelDesc  read_tuple(Tuple tuple)
	throws IOException, 
	RelCatalogException
	{
		RelDesc record = new RelDesc();
		try {
			record.relName = tuple.getStrFld(1);
			record.attrCnt = tuple.getIntFld(2);
			record.indexCnt = tuple.getIntFld(3);
			record.numTuples = tuple.getIntFld(4);
			record.numPages = tuple.getIntFld(5);
		}
		catch (Exception e1) {
			System.err.println ("getFld"+e1);
			throw new RelCatalogException(e1, "getFld failed");
		}

		return record;

	};

	// Methods have not been implemented.

	// DESTROY A RELATION
	void destroyRel(String relation){};

	// DROP AN INDEX FROM A RELATION
	void dropIndex(String relation, String attrname, IndexType accessType){};

	// DUMPS A CATALOG TO A DISK FILE (FOR OPTIMIZER)
	void dumpCatalog(String filename){};

	// Collects stats from all the tables of the database.
	void runStats(String filename){};

	// OUTPUTS A RELATION TO DISK FOR OPTIMIZER
	// void dumpRelation(fstream outFile, RelDesc relRec, int tupleSize){}; 

	// OUTPUTS ATTRIBUTES TO DISK FOR OPTIMIZER (used by runstats)
	// void rsdumpRelAttributes (fstream outFile,AttrDesc [] attrRecs,
	//        int attrCnt, String relName){};

	// OUTPUTS ATTRIBUTES TO DISK FOR OPTIMIZER
	// void dumpRelAttributes (fstream outFile, AttrDesc [] attrRecs,
	//        int attrCnt){};

	// OUTPUTS ACCESS METHODS TO DISK FOR OPTIMIZER
	// void dumpRelIndex(fstream outFile,IndexDesc [] indexRecs,
	//                    int indexCnt, int attrsize){};


	Tuple tuple;
	short [] str_sizes;
	/**
	 * @uml.property  name="attrs"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	AttrType [] attrs;

};

