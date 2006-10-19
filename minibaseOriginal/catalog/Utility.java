//------------------------------------
//Utility.java
//
//Ning Wang, April 25, 1998
//-------------------------------------

package catalog;

import global.AttrType;
import global.Catalogglobal;
import global.ExtendedSystemDefs;
import global.IndexType;
import global.RID;
import heap.Heapfile;
import heap.Tuple;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import btree.BTreeFile;
import btree.IntegerKey;
import btree.KeyClass;
import btree.StringKey;

public class Utility implements Catalogglobal{
	
	// WRAPS DELETE UTILITY IN TX
	void deleteRecordUT(String relation, attrNode item){};
	
	// DELETES RECORDS
	void deleteRecUT(String relation, attrNode item){};
	
	// DELETES INDEX ENRIES FOR RECORDS
	void deleteRecIndexesUT(String relation, RID rid, Tuple tuple){};
	
	// WRAPS INSERT UTILITY  IN TX
	public static void insertRecordUT(String relation, attrNode [] attrList)
	throws Catalogmissparam, 
	Catalogrelexists, 
	Catalogdupattrs, 
	Catalognomem,
	IOException, 
	Catalogioerror,
	Cataloghferror, 
	Catalogrelnotfound, 
	Catalogindexnotfound,
	Catalogattrnotfound, 
	Catalogbadattrcount, 
	Catalogattrexists,
	Catalogbadtype,
	Exception
	{
		insertRecUT(relation, attrList);
	};
	
	
//	---------------------------------------------------
//	INSERT A RECORD INTO THE DATABASE
//	- takes
//	1. relation name
//	2. attribute count
//	3. array of attribute names and values
//	- does
//	1. typechecks
//	2. creates tuple
//	3. inserts into datafile
//	4. inserts into each indexfile
//	---------------------------------------------------
	
	public static void insertRecUT(String relation, attrNode [] attrList)
	throws Catalogmissparam, 
	Catalogrelexists, 
	Catalogdupattrs, 
	Catalognomem,
	IOException, 
	Catalogioerror,
	Cataloghferror, 
	Catalogrelnotfound, 
	Catalogindexnotfound,
	Catalogattrnotfound, 
	Catalogbadattrcount, 
	Catalogattrexists,
	Catalogbadtype,
	Exception
	{
		int attrCnt = attrList.length;
		
		// GET RELATION
		RelDesc  relRec = null;
		ExtendedSystemDefs.MINIBASE_RELCAT.getInfo(relation, relRec);
		
		// CHECK FOR VALID NO OF RECORDS		
		if (relRec.attrCnt != attrCnt){
			throw new Catalogbadattrcount(null,"CATALOG: Bad Attribute Count!");
		}
		
		// GET INFO ON ATTRIBUTES
		AttrDesc  [] attrRecs = null;
		
		int attrCount = ExtendedSystemDefs.MINIBASE_ATTRCAT.getRelInfo(relation, 0, attrRecs);
		
		// CHECK ATTRIBUTE LIST
		for(int i = 0; i < attrCnt; i++){ 
			if (attrRecs[i].attrName.equalsIgnoreCase(attrList[i].attrName)){
				throw new Catalogattrexists(null, "Catalog: Attribute Exists!");
			}
		}
		
		// GET INFO ON INDEXES
		IndexDesc [] indexRecs = null;
		ExtendedSystemDefs.MINIBASE_INDCAT.getRelInfo(relation,0, indexRecs);
		
		// TYPE CHECK RIGHT HERE......Make sure that the values being
		// passed are valid
		
		for (int i = 0; i < attrCount; i++) {
			switch (attrRecs[i].attrType.attrType) {
			case(AttrType.attrInteger):
				if (!check_int(attrList[i])) {
					throw new Catalogbadtype(null, "Catalog: Bad Type!");
				}
			break;
			
			case (AttrType.attrReal):
				if (!check_float(attrList[i])) {
					throw new Catalogbadtype(null, "Catalog: Bad Type!");
				}
			break;
			
			case (AttrType.attrString):
				if (!check_string(attrList[i])) {
					throw new Catalogbadtype(null, "Catalog: Bad Type!");
				}
			break;
			
			default:
			{
				throw new Catalogbadtype(null, "Catalog: Bad Type!");
			}
			}
		}
		
		
//		CREATE TUPLE  	
		Tuple tuple = new Tuple(Tuple.max_size);
		
		
		AttrType  [] typeArray = null;
		short []    sizeArray = null;
		int count = ExtendedSystemDefs.MINIBASE_ATTRCAT.getTupleStructure(relation,0, typeArray,sizeArray);
		
		tuple.setHdr((short)count, typeArray, sizeArray);
		
//		CONVERT DATA STRINGS TO VARIABLE VALUES & INSERT INTO TUPLE
		
		for (int i = 0; i < relRec.attrCnt; i++) {
			switch (attrRecs[i].attrType.attrType) {
			
			case(AttrType.attrInteger):
				Integer integerVal = new Integer(attrList[i].attrValue);
			int intVal = integerVal.intValue();
			tuple.setIntFld(attrRecs[i].attrPos, intVal);
			break;
			
			case (AttrType.attrReal):
				Float floatVal1 = new Float(attrList[i].attrValue);
			float fVal = floatVal1.floatValue();
			tuple.setFloFld(attrRecs[i].attrPos, fVal);
			break;
			
			case (AttrType.attrString):
				tuple.setStrFld(attrRecs[i].attrPos, attrList[i].attrValue);
			break;
			
			default:
				System.out.println("Error in insertRecUT in utility.C");
			}
		}
		
		tuple.size();
		
//		GET DATAFILE
		Heapfile heap = new Heapfile(relation);
		
		
//		INSERT INTO DATAFILE
		heap.insertRecord(tuple.getTupleByteArray());
		
//		NOW INSERT INTO EACH INDEX FOR RELATION
		
		// DELETE FOLLOWING ON RETURN 
		String    indexName = null;
		int      attrPos = 0;
		AttrType attrType = new AttrType(AttrType.attrInteger);
		
		for(int i=0; i < relRec.indexCnt; i++)    {
			indexName = ExtendedSystemDefs.MINIBASE_INDCAT.buildIndexName(relation, indexRecs[i].attrName, //  
					indexRecs[i].accessType);
			
			// FIND INDEXED ATTRIBUTE
			
			for(int x = 0; x < attrCnt ; x++)
			{
				if (attrRecs[x].attrName.equalsIgnoreCase(indexRecs[i].attrName)==true)
				{
					attrType = attrRecs[x].attrType;
					attrPos  = attrRecs[x].attrPos;
					break;
				}
			}
			
			// PULL OUT KEY
			
			KeyClass key = null;
			
			switch(attrType.attrType)
			{
			case AttrType.attrInteger  : 
				int intVal = tuple.getIntFld(attrPos);
				IntegerKey k1 = new IntegerKey(intVal);
				key = k1;
				break;
				
			case AttrType.attrReal     : 
				float floatVal = tuple.getFloFld(attrPos);
				IntegerKey k2 = new IntegerKey((int)floatVal); // no FloatKey  
				key = k2;
				break;
				
			case AttrType.attrString   : 
				String strVal = tuple.getStrFld(attrPos);
				StringKey k3 = new StringKey(strVal);
				key = k3;
				break;
			default:
				System.out.println("Error in insertRecUT");
			}
			
			
			// INSERT INTO INDEX
			
			// BTREE INSERT CODE
			
			if (indexRecs[i].accessType.indexType == IndexType.B_Index)
			{
				try {
					BTreeFile btree = new BTreeFile(indexName);
					
					if (btree == null){
						throw new Catalognomem(null, "Catalog: No Enough Memory!");
					}
					btree.insert(key,null);
				} 
				catch (Exception e1) {
					throw e1;
				}
			}
			
		} // end for loop - errors break out of loop
	};
	
	// WRAPS LOAD UTILITY IN TX
	public static void loadUT(String relation, String fileName) throws Exception{
		
		FileInputStream fstream = null;
		fstream = new FileInputStream(fileName);
		
		DataInputStream in = new DataInputStream(fstream);
		
		List<attrNode> record = new ArrayList<attrNode>();
		
		// Leemos la primera fila con la informacion sobre las columnas
		if(in.available() != 0){
			String line = in.readLine();
			
			String[]  columnsFile = line.split("\t");
			
			for(String column: columnsFile){
				attrNode attribute = new attrNode();
				attribute.attrName = column;
				record.add(attribute);
			}
		}
		
		while (in.available() !=0) {
			String line =  in.readLine();
			
			String[] values = line.split("\t");
			
			if(values.length > record.size()){
				throw new Exception();
			}
			
			for(int i=0;i<values.length;i++){
				record.get(i).attrValue = values[i];
			}
			
			insertRecordUT(relation,(attrNode[])record.toArray());
		}
	};
	
	// LOADS RECORDS
	public static void loadRecordsUT(String relation, String fileName) throws Catalogmissparam, Catalogrelexists, Catalogdupattrs, Catalognomem, Catalogioerror, Cataloghferror, Catalogrelnotfound, Catalogindexnotfound, Catalogattrnotfound, Catalogbadattrcount, Catalogattrexists, Catalogbadtype, Exception{
		loadUT(relation,fileName);
	};
	
	// LOADS INDEXES
	//void loadIndexesUT(Tuple tuple, int attrCnt, int indexCnt,
	//  AttrDesc [] attrs, IndexDesc [] indexes, void [] iFiles, RID rid ){};
	
//	-------------------------------
//	TYPECHECK INTS
//	--------------------------------
	
	/*Checks to see if a given string is a valid int.  ["-" | ""][0..9]+  */
	public static boolean check_int(attrNode N)
	{
		byte [] index ;
		index = N.attrValue.getBytes();
		int length = N.attrValue.length();
		
		int count = 0;
		if ((length >1) && (index[count] == '-'))
			count ++;	
		else
			return false;
		
		for (int i = count; i < length; i++)	
			if ((index[i] < '0') || (index[i] > '9'))
				return false;
		
		return true;
	}
	
	
//	-----------------------------------
//	TYPECHECK FLOAT
//	------------------------------------
	
	/* CHecks to see if a string is a valid float.
	 Nothing special.
	 ["-" | ""] [0..9]+ ["." | ""] [0..9]+       */
	static boolean check_float(attrNode N)
	{
		byte [] index = N.attrValue.getBytes();
		int length = N.attrValue.length();
		
		int count = 0;
		if ((length >1) && (index[count] == '-'))
			count ++;	
		else
			return false;
		
		if ((length >1)&&(index[count] == '.')) {   // If we begin with a ., then we must check to make
			// sure that all characters following it are numbers
			count++;
			for (int i = count; i < length; i++) 
				if ((index[i] < '0') || (index[i] > '9'))
					return false;
			return true;
		}
		
		// If the first character is NOT a number (ignoring minus signs),
		// then we must check fror numbers, then check for ., then check for numbers
		for (int i=count; i < length; i++) {
			if ((index[i] != '.')&&((index[i] < '0') || (index[i] > '9')))
				return false;
			
			// We've hit a ., so we must check for only numbers now..XS
			if (index[i] == '.')
				for (int j= i+1; j < length; j++)
					if ((index[j] < '0') || (index[j] > '9'))
						return false;
		}
		return true;
	}
	
//	-------------------------------------------------------------------
//	CHECK STRING LENGTH
//	Checks to make sure that the length of the string is within the liMits
//	set by the attrDesc
//	--------------------------------------------------------------------
	
	static boolean check_string(attrNode N)
	{
		return true;
	}
	
	
}
