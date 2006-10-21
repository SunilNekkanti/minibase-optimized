//------------------------------------
// Catalog.java
//
// Ning Wang, April, 1998
//-------------------------------------

package catalog;

import global.AttrType;
import global.Catalogglobal;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.IndexType;
import heap.Scan;

import java.io.IOException;

/**
 * @author  Fernando
 */
public class Catalog 
  implements GlobalConst, Catalogglobal
{
  
  // open relation catalog (invokes constructors for each)
  
  public Catalog()
    {
      
      try {
	// RELCAT
	relCat = new RelCatalog("relcatalog");
	
	// ATTRCAT
	attrCat = new AttrCatalog("attrcatalog");
	
	// INDCAT
	indCat = new IndexCatalog("indexcatalog");
      }
      catch (Exception e) {
	System.err.println (""+e);
      }
    }
  
  
  // get catalog entry for a relation
  public void getRelationInfo(String relation, RelDesc record)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   RelCatalogException
    {
      relCat.getInfo(relation, record);
    };
  
  // create a new relation
    public  void createRel(String relation, attrInfo [] attrList)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   RelCatalogException
    {
      relCat.createRel( relation, attrList);
    };
  
  // destroy a relation
    public  void destroyRel(String relation)
    {
      relCat.destroyRel( relation);
    };
  
  // add a index to a relation
    public  void addIndex(String relation, String attrname,
		IndexType accessType, int buckets)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype, 
	   java.lang.Exception
    {
      relCat.addIndex(relation, attrname, accessType, 0);
    };
  
  // drop an index from a relation
    public  void dropIndex(String relation, String attrname,
		 IndexType accessType)
    {
      relCat.dropIndex(relation, attrname, accessType);
    };
  
  // get a catalog entry for an attribute
    public  void getAttributeInfo(String relation, String attrName,
			AttrDesc record)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   AttrCatalogException
    {
      attrCat.getInfo(relation, attrName, record); 
    };
  
  // get catalog entries for all attributes of a relation
  // return attrCnt.
    public  int getRelAttributes(String relation, int attrCnt,
		       AttrDesc [] attrs)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   AttrCatalogException
    { 
      attrs = attrCat.getRelInfo(relation);
      
      return attrs.length;
    };
  
  // get catalog entries for all indexes for a relation
    public  int getRelIndexes(String relation, int indexCnt,
		    IndexDesc [] indexes)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   IndexCatalogException,
	   RelCatalogException
    {
      indexes = indCat.getRelInfo(relation);
      
      return indexes.length;
    };
  
  // get catalog entries for all indexes for an attribute 
    public  int getAttrIndexes(String relation, String attrName,
		     int indexCnt, IndexDesc [] indexes)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   IndexCatalogException
    {
      int count;	
      count = indCat.getAttrIndexes(relation, attrName, indexCnt, indexes);
      
      return count;
    };
  
  // get catalog entry on an index
    public void getIndexInfo(String relation, String attrName,
		    IndexType accessType, IndexDesc record)
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
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
      indCat.getInfo(relation, attrName, accessType, record); 
    };
  
  // dump catalogs to a disk file for optimizer
    public void dumpCatalog(String filename)
    {
      relCat.dumpCatalog(filename);
    };
  
  // Runs stats on the database...
    public void runStats(String filename)
    {
      relCat.runStats(filename); 
    };
  
    public void listRelations()
    throws CatalogException
    {
      try {
	Scan relscan = new Scan(relCat);
      }
      catch (Exception e1) {
	throw new CatalogException (e1, "scan failed");
      }
    };
  
    void initialize()
    throws Catalogmissparam, 
	   Catalogrelexists, 
	   Catalogdupattrs, 
	   Catalognomem,
	   IOException, 
	   CatalogException,
	   Catalogioerror,
	   Cataloghferror, 
	   Catalogrelnotfound, 
	   Catalogindexnotfound,
	   Catalogattrnotfound, 
	   Catalogbadattrcount, 
	   Catalogattrexists,
	   Catalogbadtype,
	   RelCatalogException 
    {
      int max;
      int sizeOfInt = 4;
      
      
      // *************RELCATALOG***************************************
      
      attrInfo [] attrs ;
      
      attrs = new attrInfo[5];
      
      attrs[0].attrType = new AttrType(AttrType.attrString);
      attrs[0].attrName = new String("relName" );
      attrs[0].attrLen = MAXNAME;      // <= NOT SAFE!!!!!
      
      attrs[1].attrType = new AttrType(AttrType.attrInteger);
      attrs[1].attrName = new String("attrCnt" );
      attrs[1].attrLen = sizeOfInt;	// sizeof(int)
      
      attrs[2].attrType = new AttrType(AttrType.attrInteger);
      attrs[2].attrName = new String("indexCnt" );
      attrs[2].attrLen = sizeOfInt;
      
      attrs[3].attrType = new AttrType(AttrType.attrInteger);
      attrs[3].attrName = new String("numTuples" );
      attrs[3].attrLen = sizeOfInt;
      
      attrs[4].attrType = new AttrType(AttrType.attrInteger);
      attrs[4].attrName = new String("numPages" );
      attrs[4].attrLen = sizeOfInt;
      
      ExtendedSystemDefs.MINIBASE_CATALOGPTR.createRel(RELCATNAME, attrs);
      
      
      // ***************  ATTRCATALOG ****************
      
      attrs = new attrInfo[9];
      
      attrs[0].attrType = new AttrType(AttrType.attrString);
      attrs[0].attrName = new String("relName" );
      attrs[0].attrLen = MAXNAME;      // <= NOT SAFE!!!!!
      
      attrs[1].attrType = new AttrType(AttrType.attrString);
      attrs[1].attrName = new String("attrName" );
      attrs[1].attrLen = MAXNAME;
      
      attrs[2].attrType = new AttrType(AttrType.attrInteger);
      attrs[2].attrName = new String("attrOffset" );
      attrs[2].attrLen = sizeOfInt;
      
      attrs[3].attrType = new AttrType(AttrType.attrInteger);
      attrs[3].attrName = new String("attrPos" );
      attrs[3].attrLen = sizeOfInt;
      
      // 0 = string, 1 = real, 2 = integer
      attrs[4].attrType = new AttrType(AttrType.attrInteger);
      attrs[4].attrName = new String("attrType" );
      attrs[4].attrLen = sizeOfInt;
      
      
      attrs[5].attrType = new AttrType(AttrType.attrInteger);
      attrs[5].attrName = new String("attrLen" );
      attrs[5].attrLen = sizeOfInt;
      
      attrs[6].attrType = new AttrType(AttrType.attrInteger);
      attrs[6].attrName = new String("indexCnt" );
      attrs[6].attrLen = sizeOfInt;
      
      max = 10;   // comes from attrData char strVal[10]
      if (sizeOfInt > max)
	max = sizeOfInt;
      
      int sizeOfFloat = 4;
      if (sizeOfFloat > max)
	max = sizeOfFloat;
      
      attrs[7].attrType = new AttrType(AttrType.attrString);   // ?????  BK ?????
      attrs[7].attrName = new String("minVal" );
      attrs[7].attrLen = max;
      
      attrs[8].attrType = new AttrType(AttrType.attrString);   // ?????  BK ?????
      attrs[8].attrName = new String("maxVal" );
      attrs[8].attrLen = max;
      
      ExtendedSystemDefs.MINIBASE_CATALOGPTR.createRel(ATTRCATNAME,attrs);
      
      
      // ************* INDEXCATALOG ******************
      
      attrs = new attrInfo[7];
      
      attrs[0].attrType = new AttrType(AttrType.attrString);
      attrs[0].attrName = new String("relName" );
      attrs[0].attrLen = MAXNAME;      // <= NOT SAFE!!!!!
      
      attrs[1].attrType = new AttrType(AttrType.attrString);
      attrs[1].attrName = new String("attrName" );
      attrs[1].attrLen = MAXNAME;
      
      // 0 = None
      // 1 = B_Index
      // 2 = Linear Hash
      attrs[2].attrType = new AttrType(AttrType.attrInteger);
      attrs[2].attrName = new String("accessType" );
      attrs[2].attrLen = sizeOfInt;
      
      // 0 = Ascending
      // 1 = Descending
      // 2 = Random
      attrs[3].attrType = new AttrType(AttrType.attrInteger);
      attrs[3].attrName = new String("order" );
      attrs[3].attrLen = sizeOfInt; 
      
      attrs[4].attrType = new AttrType(AttrType.attrInteger);
      attrs[4].attrName = new String("clustered" );
      attrs[4].attrLen = sizeOfInt;
      
      attrs[5].attrType = new AttrType(AttrType.attrInteger);
      attrs[5].attrName = new String("distinctKeys" );
      attrs[5].attrLen = sizeOfInt;
      
      attrs[6].attrType = new AttrType(AttrType.attrInteger);
      attrs[6].attrName = new String("indexPages" );
      attrs[6].attrLen = sizeOfInt;
      
      ExtendedSystemDefs.MINIBASE_CATALOGPTR.createRel(INDEXCATNAME, attrs);
    };
  
  /**
 * @return  the indCat
 * @uml.property  name="indCat"
 */
public IndexCatalog getIndCat()
    { return indCat; };
  /**
 * @return  the relCat
 * @uml.property  name="relCat"
 */
public RelCatalog getRelCat()
    { return relCat; };
  /**
 * @return  the attrCat
 * @uml.property  name="attrCat"
 */
public AttrCatalog getAttrCat()
    { return attrCat; };
  
  
  private IndexCatalog  indCat;
  private RelCatalog    relCat;
  private AttrCatalog   attrCat;
}

