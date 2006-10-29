//------------------------------------
//IndexCatalog.java

//Ning Wang, April,24, 1998
//-------------------------------------

package catalog;

import btree.*;
import bufmgr.*;
import bufmgr.exceptions.BufMgrException;
import diskmgr.*;
import diskmgr.exceptions.DiskMgrException;
import global.*;
import heap.*;
import heap.exceptions.InvalidTupleSizeException;

import java.io.*;

import catalog.exceptions.Catalogattrnotfound;
import catalog.exceptions.Catalogbadtype;
import catalog.exceptions.Cataloghferror;
import catalog.exceptions.Catalogindexexists;
import catalog.exceptions.Catalogindexnotfound;
import catalog.exceptions.Catalogioerror;
import catalog.exceptions.Catalogmissparam;
import catalog.exceptions.Catalognomem;
import catalog.exceptions.Catalogrelexists;
import catalog.exceptions.Catalogrelnotfound;
import catalog.exceptions.IndexCatalogException;
import catalog.exceptions.RelCatalogException;

/**
 * @author  Fernando
 */
public class IndexCatalog extends Heapfile
implements GlobalConst, Catalogglobal
{

	// OPEN INDEX CATALOG
	IndexCatalog(String filename)
	throws IOException, 
	BufMgrException,
	DiskMgrException,
	Exception
	{
		super(filename);

		tuple = new Tuple(Tuple.max_size);
		attrs = new AttrType[7];

		attrs[0] = new AttrType(AttrType.attrString);
		attrs[1] = new AttrType(AttrType.attrString);
		attrs[2] = new AttrType(AttrType.attrInteger);
		attrs[3] = new AttrType(AttrType.attrInteger); // 0 = None
		// 1 = B_Index
		// 2 = Linear Hash

		attrs[4] = new AttrType(AttrType.attrInteger); 
		// 0 = Ascending
		// 1 = Descending
		// 2 = Random
		attrs[5] = new AttrType(AttrType.attrInteger);
		attrs[6] = new AttrType(AttrType.attrInteger);


		str_sizes = new short[2];
		str_sizes[0] = (short)MAXNAME;
		str_sizes[1] = (short)MAXNAME;

		try {
			tuple.setHdr(attrs, str_sizes);
		}
		catch (Exception e) {
			throw new IndexCatalogException(e, "setHdr() failed");
		}
	};

	// GET ALL INDEXES FOR A RELATION
	// Return indexCnt.
	public IndexDesc [] getRelInfo(String relation)
	throws Catalogmissparam, 
	Catalogioerror, 
	Cataloghferror, 
	Catalogindexnotfound,
	IOException, 
	Catalognomem, 
	Catalogattrnotfound,
	IndexCatalogException,
	RelCatalogException,
	Catalogrelnotfound
	{

		int status;
		int recSize;
		RID rid = null;
		Scan pscan = null;
		int count = 0;

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

		// SET INDEX COUNT BY REFERENCE 

		int indexCnt = record.indexCnt;

		if (indexCnt == 0)
			return new IndexDesc [0];


		// OPEN SCAN

		try {
			pscan = this.openScan();
		}
		catch (Exception e) {
			throw new IndexCatalogException(e,"scan() failed");
		}

		// ALLOCATE INDEX ARRAY

		IndexDesc[] indexes = new IndexDesc[indexCnt];
		if (indexes == null)
			throw new Catalognomem(null, "Catalog: No Enough Memory!");

		// SCAN THE FILE

		while(true) 
		{

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
				tuple.setHdr(attrs,str_sizes);	
				indexes[count] = read_tuple(tuple);
			}
			catch (Exception e4) {
				throw new IndexCatalogException(e4," read_tuple() failed");
			}

			if(indexes[count].relName.equalsIgnoreCase(relation)==true)
				count++;

			if(count == indexCnt)  // IF ALL INDEXES FOUND
				break;
		}

		return indexes;

	};

	// RETURN INFO ON AN INDEX
	public IndexDesc getInfo(String relation, String attrName,
			IndexType accessType) throws Catalogmissparam, InvalidTupleSizeException, IOException, Catalogindexnotfound, IndexCatalogException
			{
		if ((relation == null)||(attrName == null)){
			throw new Catalogmissparam(null, "MISSING_PARAM");
		}

		// OPEN SCAN

		Scan pscan = new Scan(this);


		// SCAN FILE

		IndexDesc record = new IndexDesc();
		while (true)
		{

			Tuple tuple = pscan.getNext(new RID());
			if (tuple == null)
				throw new Catalogindexnotfound(null,"Catalog: Index not Found!");

			try {
				tuple.setHdr(attrs,str_sizes);	
				record = read_tuple(tuple);
			}
			catch (Exception e4) {
				throw new IndexCatalogException(e4, "read_tuple failed");
			}

			if(record.relName.equalsIgnoreCase(relation)==true 
					&& record.attrName.equalsIgnoreCase(attrName)==true 
					&& (accessType == record.accessType))
				break;  // FOUND
		}
		return record;
			};

			// GET ALL INDEXES INLUDING A SPECIFIED ATTRIBUTE
			public int getAttrIndexes(String relation,
					String attrName, int indexCnt, IndexDesc [] indexes)
			throws Catalogmissparam, 
			Catalogioerror, 
			Cataloghferror,
			IOException, 
			Catalognomem,
			Catalogindexnotfound,
			Catalogattrnotfound,
			IndexCatalogException
			{

				int status;
				int recSize;
				RID rid = null;
				Scan pscan = null;
				int count = 0;

				if (relation == null)
					throw new Catalogmissparam(null, "MISSING_PARAM");

				AttrDesc record = null;
				try {
					record = ExtendedSystemDefs.MINIBASE_ATTRCAT.getInfo(relation, attrName);
				}
				catch (Catalogioerror e) {
					throw e;
				}
				catch (Cataloghferror e1) {
					throw e1;
				}
				catch (Catalogmissparam e2) {
					throw e2;
				}
				catch (Catalogattrnotfound e3) {
					throw e3;
				}
				catch (Exception e4) {
					throw new IndexCatalogException (e4,"getInfo() failed");
				}

				// ASSIGN INDEX COUNT

				indexCnt = record.indexCnt;
				if(indexCnt == 0)
					return 0;

				// OPEN SCAN

				try {
					pscan = new Scan(this);
				}
				catch (Exception e) {
					throw new IndexCatalogException(e,"scan failed");
				}

				// ALLOCATE INDEX ARRAY

				indexes = new IndexDesc[indexCnt];
				if (indexes == null)
					throw new Catalognomem(null, "Catalog: No Enough Memory!");

				// SCAN FILE

				while(true) 
				{
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
						tuple.setHdr(attrs,str_sizes);
						indexes[count] = read_tuple(tuple);
					}
					catch (Exception e4) {
						throw new IndexCatalogException(e4, "pascan.getNext() failed");
					}

					if(indexes[count].relName.equalsIgnoreCase(relation)==true 
							&& indexes[count].attrName.equalsIgnoreCase(attrName)==true)
						count++;

					if(count == indexCnt)  // if all indexes found
						break; 
				}

				return indexCnt;    
			};

			// CREATES A FILE NAME FOR AN INDEX 
			public String buildIndexName(String relation, String attrName,
					IndexType accessType)
			{
				String accessName = null;
				int sizeName;
				int sizeOfByte = 1;
				String indexName = null;

				// DETERMINE INDEX TYPE

				if(accessType.indexType == IndexType.B_Index)
					accessName = new String("B_Index");
				else if(accessType.indexType == IndexType.Hash)
					accessName = new String("Hash");

				// CHECK FOR LEGIT NAME SIZE

				sizeName = relation.length() + accessName.length() +
				attrName.length() + (3 * sizeOfByte);

				//if(sizeName > MAXNAME)
				//    return MINIBASE_FIRST_ERROR( CATALOG, Catalog::INDEX_NAME_TOO_LONG );

				// CREATE NAME

				indexName = new String(relation);
				indexName = indexName.concat("-");
				indexName = indexName.concat(accessName);
				indexName = indexName.concat("-");
				indexName = indexName.concat(attrName);

				return indexName;    
			};

			// ADD INDEX ENTRY TO CATALOG
			public void addInfo(IndexDesc record)
			throws IOException,
			IndexCatalogException
			{
				RID rid;

				try {
					make_tuple(tuple, record);
				}
				catch (Exception e4) {
					throw new IndexCatalogException(e4, "make_tuple failed");
				}

				try {
					insertRecord(tuple.getTupleByteArray());
				}
				catch (Exception e) {
					throw new IndexCatalogException(e, "insertRecord() failed");
				}
			};

			// REMOVE INDEX ENTRY FROM CATALOG
			public void removeInfo(String relation, String attrName,
					IndexType accessType)
			throws IOException, 
			Catalogmissparam, 
			Catalogattrnotfound,
			IndexCatalogException
			{
				int recSize;
				RID rid = null;
				Scan pscan = null;
				IndexDesc record = null;

				if ((relation == null)||(attrName == null))
					throw new Catalogmissparam(null, "MISSING_PARAM");

				// OPEN SCAN
				try {
					pscan = new Scan(this);
				}
				catch (Exception e) {
					throw new IndexCatalogException(e,"scan failed");
				}

				// SCAN FILE

				while (true)
				{

					try {
						tuple = pscan.getNext(rid);
					} catch (InvalidTupleSizeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (tuple == null) 
						throw new Catalogattrnotfound(null,
								"Catalog: Attribute not Found!");
					try {
						tuple.setHdr(attrs,str_sizes);
						record = read_tuple(tuple);
					}
					catch (Exception e4) {
						throw new IndexCatalogException(e4, "read_tuple failed");
					}

					if(record.relName.equalsIgnoreCase(relation)==true 
							&& record.attrName.equalsIgnoreCase(attrName)==true
							&& (record.accessType == accessType))
					{
						try {
							deleteRecord(rid);  //  FOUND -  DELETE        
						}
						catch (Exception e){
							throw new IndexCatalogException(e, "deleteRecord() failed");
						}
						break; 
					}
				}

				return;    
			};

			// ADD INDEX TO A RELATION
			public void addIndex(String relation, String attrName,
					IndexType accessType, int buckets )
			throws IOException,
			Catalogioerror, 
			Cataloghferror, 
			Catalogmissparam,
			Catalogattrnotfound, 
			Catalogbadtype, 
			Catalognomem,
			Catalogindexnotfound, 
			IndexCatalogException,
			java.lang.Exception
			{		
				if ((relation == null)||(attrName == null)){
					throw new Catalogmissparam(null, "MISSING_PARAM");
				}
				// CHECK FOR EXISTING INDEX

				boolean exist = true;
				IndexDesc indexRec = null;

				try{
					indexRec = getInfo(relation, attrName, accessType);
				}
				catch (Catalogindexnotfound e){
					exist = false;
				}

				if(exist){
					throw new Catalogindexexists(null, "Index Exists!");
				}

				// GET ATTRIBUTE INFO 

				AttrDesc  attrRec = null;
				try {
					attrRec = ExtendedSystemDefs.MINIBASE_ATTRCAT.getInfo(relation, attrName);
				}
				catch (Exception e2) {
					throw new IndexCatalogException(e2, "getInfo() failed");
				}

				// Can only index on int's and strings currently
				if ((attrRec.attrType.attrType != AttrType.attrInteger) 
						&& (attrRec.attrType.attrType != AttrType.attrString)) 
					throw new Catalogbadtype(null, "Catalog: BAD TYPE!");


				// UPDATE ATTRIBUTE INFO

				attrRec.indexCnt++;

				try {
					ExtendedSystemDefs.MINIBASE_ATTRCAT.removeInfo(relation, attrName);
					ExtendedSystemDefs.MINIBASE_ATTRCAT.addInfo(attrRec);
				}
				catch (Exception e) {
					throw new IndexCatalogException(e, "add/remove info failed");
				}

				// BUILD INDEX FILE NAME

				String indexName = buildIndexName(relation, attrName, accessType);


				// ADDED BY BILL KIMMEL - DELETE LATER
				System.out.println("Index name is " +indexName);

				BTreeFile btree = null;

				// IF BTREE

				if (accessType.indexType == IndexType.B_Index)
				{
					btree = new BTreeFile(indexName, attrRec.attrType.attrType, attrRec.attrLen, 0);
				} 


				// ADD ENTRY IN INDEXCAT


				indexRec = new IndexDesc();
				indexRec.relName = new String(relation);
				indexRec.attrName = new String(attrName);
				indexRec.accessType = accessType;

				if (accessType.indexType == IndexType.B_Index)
					indexRec.order = new TupleOrder(TupleOrder.Ascending);
				else
					indexRec.order = new TupleOrder(TupleOrder.Random);

				indexRec.distinctKeys = DISTINCTKEYS;
				indexRec.clustered = 0;  // 0 means non-clustered!!!!

				indexRec.indexPages  = INDEXPAGES;

				try {
					addInfo(indexRec);
				}
				catch (Exception e) {
					throw new IndexCatalogException(e, "addInfo() failed");
				}




				// PREPARE TO SCAN DATA FILE

				Heapfile datafile = null;
				try {
					datafile = new Heapfile(relation);
					if (datafile == null)
						throw new Catalognomem(null, "NO Enough Memory!");
				}
				catch (Exception e) {
					throw new IndexCatalogException(e, "create heapfile failed");
				}


				Scan 	pscan = null;
				try {
					pscan = datafile.openScan();
				}
				catch (Exception e) {
					throw new IndexCatalogException(e,"openScan() failed");
				}


				// PREPARE TUPLE

				Tuple tuple = new Tuple(Tuple.max_size);
				if (tuple == null)
					throw new Catalognomem(null, "Catalog, No Enough Memory!");

				// NOW PROCESS THE HEAPFILE AND INSERT KEY,RID INTO INDEX

				while(true) {
					RID    	rid = new RID();
					try {
						tuple = pscan.getNext(rid);
						if (tuple == null) 
							return;
					}
					catch (Exception e) {
						throw new IndexCatalogException(e, "getNext() failed");
					}
					ExtendedSystemDefs.MINIBASE_ATTRCAT.getTupleStructure(relation, tuple);

					// PULL OUT THE KEY VALUE FROM HEAPFILE RECORD

					KeyClass key = null;
					if (attrRec.attrType.attrType == AttrType.attrInteger)
					{
						int intKey = tuple.getIntFld(attrRec.attrPos);
						key = new IntegerKey(intKey);
					}
					else if (attrRec.attrType.attrType == AttrType.attrReal)
					{
						float floatKey = tuple.getFloFld(attrRec.attrPos);
						key = new IntegerKey((int)floatKey);
					}
					else if (attrRec.attrType.attrType == AttrType.attrString)
					{
						String charKey = new String(tuple.getStrFld(attrRec.attrPos));
						key = new StringKey(charKey);
					}

					// NOW INSERT RECORD INTO INDEX

					if (accessType.indexType == IndexType.B_Index) {
						try {
							btree.insert(key, rid); 
						}
						catch (Exception e) {
							throw new IndexCatalogException(e, "insert failed");	
						}
					}
				}

			};

			// DROP INDEX FROM A RELATION
			void dropIndex(String relation, String attrName,
					IndexType accessType){};

					// DROP ALL INDEXES FOR A RELATION
					void dropRelation(String relation){};


					void make_tuple(Tuple tuple, IndexDesc record)
					throws IOException,
					IndexCatalogException
					{
						try {
							tuple.setStrFld(1, record.relName);
							tuple.setStrFld(2, record.attrName);

							if (record.accessType.indexType == IndexType.None)
								tuple.setIntFld(3, 0);
							else
								if (record.accessType.indexType == IndexType.B_Index)
									tuple.setIntFld(3, 1);
								else
									if (record.accessType.indexType == IndexType.Hash)
										tuple.setIntFld(3, 2);
									else
										System.out.println("Invalid accessType in IndexCatalog::make_tupl");

							if (record.order.tupleOrder == TupleOrder.Ascending)
								tuple.setIntFld(4, 0);
							else
								if (record.order.tupleOrder == TupleOrder.Descending)
									tuple.setIntFld(4, 1);
								else
									if (record.order.tupleOrder == TupleOrder.Random)
										tuple.setIntFld(4, 2);
									else
										System.out.println("Invalid order in IndexCatalog::make_tuple");

							tuple.setIntFld(5, record.clustered);
							tuple.setIntFld(6, record.distinctKeys);
							tuple.setIntFld(7, record.indexPages);
						}        
						catch (Exception e) {
							throw new IndexCatalogException(e,"make_tuple failed");
						}

						return;
					};

					IndexDesc read_tuple(Tuple tuple)
					throws IOException,
					IndexCatalogException
					{
						IndexDesc  record = new IndexDesc();
						try {
							record.relName = tuple.getStrFld(1);
							record.attrName = tuple.getStrFld(2);

							int temp;
							temp = tuple.getIntFld(3);
							if (temp == 0)
								record.accessType = new IndexType(IndexType.None);
							else
								if (temp == 1)
									record.accessType = new IndexType(IndexType.B_Index);
								else
									if (temp == 2)
										record.accessType = new IndexType(IndexType.Hash);
									else
										System.out.println("111Error in IndexCatalog::read_tuple");

							temp = tuple.getIntFld(4);
							if (temp == 0)
								record.order = new TupleOrder(TupleOrder.Ascending);
							else
								if (temp == 1)
									record.order = new TupleOrder(TupleOrder.Descending);
								else
									if (temp == 2)
										record.order = new TupleOrder(TupleOrder.Random);
									else
										System.out.println("222Error in IndexCatalog::read_tuple");

							record.clustered = tuple.getIntFld(5);
							record.distinctKeys = tuple.getIntFld(6);
							record.indexPages = tuple.getIntFld(7);
						}        
						catch (Exception e) {
							throw new IndexCatalogException(e,"read_tuple failed");
						}

						return record; 

					};


					Tuple tuple;
					short [] str_sizes;
					/**
					 * @uml.property  name="attrs"
					 * @uml.associationEnd  multiplicity="(0 -1)"
					 */
					AttrType [] attrs;

};

