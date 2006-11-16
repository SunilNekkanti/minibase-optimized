//------------------------------------
//AttrCatalog.java

//Ning Wang, April,24,  1998
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
import heap.exceptions.HFBufMgrException;
import heap.exceptions.HFDiskMgrException;
import heap.exceptions.HFException;
import heap.exceptions.InvalidTupleSizeException;
import heap.exceptions.InvalidTypeException;

import java.io.IOException;

import catalog.exceptions.AttrCatalogException;
import catalog.exceptions.Catalogattrnotfound;
import catalog.exceptions.Cataloghferror;
import catalog.exceptions.Catalogindexnotfound;
import catalog.exceptions.Catalogioerror;
import catalog.exceptions.Catalogmissparam;
import catalog.exceptions.Catalognomem;
import catalog.exceptions.Catalogrelnotfound;


/**
 * @author  Fernando
 */
public class AttrCatalog extends Heapfile
implements GlobalConst, Catalogglobal
{
	//OPEN ATTRIBUTE CATALOG
	AttrCatalog(String filename)
	throws java.io.IOException, 
	HFException,
	HFDiskMgrException,
	HFBufMgrException,
	AttrCatalogException
	{
		super(filename);

		int sizeOfInt = 4;
		int sizeOfFloat = 4;
		tuple = new Tuple(Tuple.max_size);
		attrs = new AttrType[14];

		// rel name
		attrs[0] = new AttrType(AttrType.attrString);
		// attr name
		attrs[1] = new AttrType(AttrType.attrString);
		// attr offset
		attrs[2] = new AttrType(AttrType.attrInteger);
		// attr pos
		attrs[3] = new AttrType(AttrType.attrInteger);
		// AttrType will be represented by an integer
		// 0 = string, 1 = real, 2 = integer
		attrs[4] = new AttrType(AttrType.attrInteger);  
		// attr len
		attrs[5] = new AttrType(AttrType.attrInteger);
		// Index		
		attrs[6] = new AttrType(AttrType.attrInteger);
		// min val
		attrs[7] = new AttrType(AttrType.attrString);   // ?????  BK ?????
		// max val
		attrs[8] = new AttrType(AttrType.attrString);   // ?????  BK ?????
		// Pk
		attrs[9] = new AttrType(AttrType.attrInteger);
		//Fk
		attrs[10] = new AttrType(AttrType.attrInteger);
		//Fk Nombre de la relacion
		attrs[11] = new AttrType(AttrType.attrString);
		//Fk Nombre del atributo
		attrs[12] = new AttrType(AttrType.attrString);
		//Check
		attrs[13] = new AttrType(AttrType.attrString);

		// Find the largest possible tuple for values attrs[7] & attrs[8]
		//   str_sizes[2] & str_sizes[3]
		max = 10;   // comes from attrData char strVal[10]
		if (sizeOfInt > max)
			max = (short) sizeOfInt;
		if (sizeOfFloat > max)
			max = (short) sizeOfFloat;


		str_sizes = new short[7];
		str_sizes[0] = (short) MAXNAME;
		str_sizes[1] = (short) MAXNAME;
		str_sizes[2] = max;
		str_sizes[3] = max;
		str_sizes[4] = (short) MAXNAME;
		str_sizes[5] = (short) MAXNAME;
		str_sizes[6] = (short) MAXNAME;
		

		try {
			tuple.setHdr( attrs, str_sizes);
		}
		catch (Exception e) {
			throw new AttrCatalogException(e, "setHdr() failed");
		}
	};

	// GET ATTRIBUTE DESCRIPTION
	public AttrDesc getInfo(String relation, String attrName)
	throws Catalogmissparam, 
	Catalogioerror, 
	Cataloghferror,
	AttrCatalogException, 
	IOException, 
	Catalogattrnotfound
	{		
		if ((relation == null)||(attrName == null)){
			throw new Catalogmissparam(null, "MISSING_PARAM");
		}

		// OPEN SCAN

		Scan pscan = null; 
		try {
			pscan = this.openScan();
		}
		catch (Exception e1) {
			throw new AttrCatalogException(e1, "scan failed");
		}

		// SCAN FILE FOR ATTRIBUTE
		// NOTE MUST RETURN ATTRNOTFOUND IF NOT FOUND!!!

		while (true){
			AttrDesc record = null;

			Tuple tuple = null;
			try {
				tuple = pscan.getNext(new RID());
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			if (tuple == null)
				throw new Catalogattrnotfound(null,"Catalog: Attribute not Found!");

			try {		
				tuple.setHdr( attrs, str_sizes);
				record= read_tuple(tuple);

			}
			catch (Exception e4) {
				throw new AttrCatalogException(e4, "read_tuple failed");
			}

			if ( record.relName.equalsIgnoreCase(relation)
					&& record.attrName.equalsIgnoreCase(attrName) ){
				pscan.closescan();
				return record;
			}
		}
	};

	// GET ALL ATTRIBUTES OF A RELATION/
	// Return attrCnt
	public AttrDesc [] getRelInfo(String relation)
	throws Catalogmissparam, 
	Catalogioerror, 
	Cataloghferror,
	AttrCatalogException, 
	IOException, 
	Catalognomem, 
	Catalogattrnotfound,
	Catalogindexnotfound, 
	Catalogrelnotfound
	{		
		if (relation == null)
			throw new Catalogmissparam(null, "MISSING_PARAM");
		RelDesc record = null;
		try {
			record = ExtendedSystemDefs.MINIBASE_RELCAT.getInfo(relation);
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
			System.err.println ("Catalog: Relation not Found!"+e3);
			throw new Catalogrelnotfound(null, "");
		}
		catch (Exception e4) {
			e4.printStackTrace();
			throw new AttrCatalogException (e4, "getInfo() failed");
		}

		// SET ATTRIBUTE COUNT BY REFERENCE
		int attrCnt = record.attrCnt;
		if (attrCnt == 0)
			return new AttrDesc[0];


		// OPEN SCAN

		Scan pscan = null;		
		try {
			pscan = this.openScan();
		}
		catch (Exception e1) {
			throw new AttrCatalogException(e1, "scan failed");
		}
		// ALLOCATE ARRAY
		AttrDesc [] Attrs = new AttrDesc[attrCnt];
		if (Attrs == null)
			throw new Catalognomem(null, "Catalog: No Enough Memory!");

		// SCAN FILE


		int count = 0;
		while(true) 
		{
			AttrDesc  attrRec= null;


			RID rid = new RID();
			Tuple tuple = null;
			try {
				tuple = pscan.getNext(rid);
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (tuple == null) 
				throw new Catalogindexnotfound(null,
				"Catalog: Index not Found!");
			try {
				tuple.setHdr( attrs, str_sizes);
				attrRec = read_tuple(tuple);
			}
			catch (Exception e4) {
				throw new AttrCatalogException(e4, "read_tuple failed");
			}

			if(attrRec.relName.equalsIgnoreCase(relation)) 
			{
				Attrs[attrRec.attrPos - 1] = attrRec;  
				count++;
			}

			if(count == attrCnt)  // if all atts found
				break; 
		}
		
		pscan.closescan();
		return Attrs;    
	};

	public AttrType[] getAttrType(String relation) throws Catalogmissparam, Catalogioerror, Cataloghferror, AttrCatalogException, Catalognomem, Catalogattrnotfound, Catalogindexnotfound, Catalogrelnotfound, IOException{
		AttrDesc[] attr = this.getRelInfo(relation);


		AttrType[] types = new AttrType[attr.length];

		for(int i=0;i<attr.length;i++){
			types[i] = attr[i].getType();
		}

		return types;
	}

	public short[] getStringsSizeType(String relation) throws Catalogmissparam, Catalogioerror, Cataloghferror, AttrCatalogException, Catalognomem, Catalogattrnotfound, Catalogindexnotfound, Catalogrelnotfound, IOException{
		AttrDesc[] attr = this.getRelInfo(relation);

		int size = 0;
		for(int i=0;i<attr.length;i++){
			if(attr[i].getType().attrType == AttrType.attrString){
				size++;
			}
		}

		short[] stypes = new short[size];

		for(int i=0,j=0;i<attr.length;i++){
			if(attr[i].getType().attrType == AttrType.attrString){
				stypes[j] = (short)attr[i].getLength();
				j++;
			}
		}


		return stypes;
	}
	// RETURNS ATTRTYPE AND STRINGSIZE ARRAYS FOR CONSTRUCTING TUPLES
	public void getTupleStructure(String relation, Tuple tuple)
	throws Catalogmissparam, 
	Catalogioerror, 
	Cataloghferror,
	AttrCatalogException, 
	IOException, 
	Catalognomem, 
	Catalogindexnotfound,
	Catalogattrnotfound, 
	Catalogrelnotfound, InvalidTypeException, InvalidTupleSizeException
	{
		AttrDesc [] attrs = null;	
		// GET ALL OF THE ATTRIBUTES
		try {
			attrs = getRelInfo(relation);
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
		catch (Catalogindexnotfound e3) {
			System.err.println ("Catalog Index not Found!"+e3);
			throw new Catalogindexnotfound(null, "");
		}
		catch (Catalogattrnotfound e4) {
			System.err.println ("Catalog: Attribute not Found!"+e4);
			throw new Catalogattrnotfound(null, "");
		}
		catch (Catalogrelnotfound e5) {
			System.err.println ("Catalog: Relation not Found!"+e5);
			throw new Catalogrelnotfound(null, "");
		}

		// ALLOCATE TYPEARRAY
		int attrCnt = attrs.length;
		AttrType [] typeArray = new AttrType[attrCnt];
		if (typeArray == null)
			throw new Catalognomem(null, "Catalog, No Enough Memory!");

		// LOCATE STRINGS
		int stringcount = 0;
		for(int i = 0; i < attrCnt; i++)
		{
			if(attrs[i].attrType.attrType == AttrType.attrString)
				stringcount++;
		}

		// ALLOCATE STRING SIZE ARRAY
		short[] sizeArray = null;
		if(stringcount > 0) 
		{
			sizeArray = new short[stringcount];
			if (sizeArray == null)
				throw new Catalognomem(null, "Catalog, No Enough Memory!");
		}

		// FILL ARRAYS WITH TYPE AND SIZE DATA

		for(int j = 0, i = 0; i < attrCnt; i++)
		{
			typeArray[i] = new AttrType(attrs[i].attrType.attrType);
			if(attrs[i].attrType.attrType == AttrType.attrString)
			{
				sizeArray[j] = (short) attrs[i].attrLen;
				j++;
			}
		}

		tuple.setHdr( typeArray, sizeArray); 
	};


	// ADD ATTRIBUTE ENTRY TO CATALOG
	public void addInfo(AttrDesc record)
	throws AttrCatalogException, 
	IOException
	{
		try {
			make_tuple(tuple, record);
		}
		catch (Exception e4) {
			throw new AttrCatalogException(e4, "make_tuple failed");
		}

		try {
			insertRecord(tuple.getTupleByteArray());
		}
		catch (Exception e2) {
			throw new AttrCatalogException(e2, "insertRecord failed");
		}
	};


	// REMOVE AN ATTRIBUTE ENTRY FROM CATALOG
	// return true if success, false if not found.
	public void removeInfo(String relation, String attrName)
	throws AttrCatalogException, 
	IOException, 
	Catalogmissparam, 
	Catalogattrnotfound

	{

		if ((relation == null)||(attrName == null))
			throw new Catalogmissparam(null, "MISSING_PARAM");

		Scan pscan = null;
		// OPEN SCAN
		try {
			pscan = openScan();
		}
		catch (Exception e1) {
			throw new AttrCatalogException(e1, "scan failed");
		}
		AttrDesc record = null;

		// SCAN FILE
		while (true) {
			RID rid = new RID();

			Tuple tuple = null;
			try {
				tuple = pscan.getNext(rid);
			} catch (InvalidTupleSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (tuple == null) 
				throw new Catalogattrnotfound(null,
				"Catalog: Attribute not Found!");
			try {
				tuple.setHdr( attrs, str_sizes);
				record = read_tuple(tuple);
			}
			catch (Exception e4) {
				throw new AttrCatalogException(e4, "read_tuple failed");
			}

			if ( record.relName.equalsIgnoreCase(relation) 
					&& record.attrName.equalsIgnoreCase(attrName))
			{
				try {
					deleteRecord(rid);
				}
				catch (Exception e3) {
					throw new AttrCatalogException(e3, "deleteRecord failed");
				}
				pscan.closescan();
				return;
			}
		}
	};


	//--------------------------------------------------
	// MAKE_TUPLE
	//--------------------------------------------------
	// Tuple must have been initialized properly in the 
	// constructor
	// Converts AttrDesc to tuple. 
	public void make_tuple(Tuple tuple, AttrDesc record)
	throws IOException, 
	AttrCatalogException
	{
		try {
			tuple.setStrFld(1, record.relName);
			tuple.setStrFld(2, record.attrName);
			tuple.setIntFld(3, record.attrOffset);
			tuple.setIntFld(4, record.attrPos);

			if (record.attrType.attrType == AttrType.attrString) {
				tuple.setIntFld(5, 0);
				tuple.setStrFld(8, record.minVal.strVal);
				tuple.setStrFld(9, record.maxVal.strVal);
			} else if (record.attrType.attrType== AttrType.attrReal) {
				tuple.setIntFld(5, 1);
				tuple.setFloFld(8,record.minVal.floatVal);
				tuple.setFloFld(9,record.minVal.floatVal);
			} else {
				tuple.setIntFld(5, 2);
				tuple.setIntFld(8,record.minVal.intVal);
				tuple.setIntFld(9,record.maxVal.intVal);
			}	

			tuple.setIntFld(10, record.isPk()?1:0);
			tuple.setIntFld(11, record.isFk()?1:0);
			tuple.setStrFld(12, record.getFkRel());
			tuple.setStrFld(13, record.getFkAttr());
			tuple.setStrFld(14, record.getCheck());
			
			tuple.setIntFld(6, record.attrLen);
			tuple.setIntFld(7, record.indexCnt);
		}
		catch (Exception e1) {
			throw new AttrCatalogException(e1, "make_tuple failed");
		}
	};


	//--------------------------------------------------
	// READ_TUPLE
	//--------------------------------------------------

	public AttrDesc read_tuple(Tuple tuple)
	throws IOException, 
	AttrCatalogException
	{
		AttrDesc record = new AttrDesc();
		try {
			record.relName = tuple.getStrFld(1);
			record.attrName = tuple.getStrFld(2);
			record.attrOffset = tuple.getIntFld(3);
			record.attrPos = tuple.getIntFld(4);
			record.setPk(tuple.getIntFld(10)!=0);
			int temp;
			temp = tuple.getIntFld(5);
			if (temp == 0)
			{
				record.attrType = new AttrType(AttrType.attrString);
				record.minVal.strVal = tuple.getStrFld(8);
				record.maxVal.strVal = tuple.getStrFld(9);
			}
			else
				if (temp == 1)
				{
					record.attrType = new AttrType(AttrType.attrReal);
					record.minVal.floatVal = tuple.getFloFld(8);
					record.maxVal.floatVal = tuple.getFloFld(9);
				}
				else
					if (temp == 2)
					{
						record.attrType = new AttrType(AttrType.attrInteger);
						record.minVal.intVal = tuple.getIntFld(8);
						record.maxVal.intVal = tuple.getIntFld(9);
					}
					else
					{
						return record;
					}

			record.attrLen = tuple.getIntFld(6);
			record.indexCnt = tuple.getIntFld(7);
			record.setFk(tuple.getIntFld(11)!=0);
			record.setFkRel(tuple.getStrFld(12));
			record.setFkAttr(tuple.getStrFld(13));
			record.setCheck(tuple.getStrFld(14));
			
		}
		catch (Exception e1) {
			throw new AttrCatalogException(e1, "read_tuple failed");
		}

		return record;

	}

	// REMOVE ALL ATTRIBUTE ENTRIES FOR A RELATION
	public void dropRelation(String relation){};

	// ADD AN INDEX TO A RELATION
	public void addIndex(String relation, String attrname,IndexType accessType){};


	Tuple tuple;
	short [] str_sizes;
	/**
	 * @uml.property  name="attrs"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	AttrType [] attrs;
	short max;
};
