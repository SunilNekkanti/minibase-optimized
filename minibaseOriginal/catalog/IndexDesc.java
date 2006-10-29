//------------------------------------
//IndexDesc.java

//Ning Wang, April,24  1998
//-------------------------------------

package catalog;

import global.IndexType;
import global.TupleOrder;

//IndexDesc class: schema for index catalog
/**
 * @author  Fernando
 */
public class IndexDesc
{
	String indexName;
	String relName;                     // relation name
	String attrName;                    // attribute name
	IndexType  accessType;                // access method
	TupleOrder order;                     // order of keys
	int        clustered = 0;                 //
	int        distinctKeys = 0;              // no of distinct key values
	int        indexPages = 0;                // no of index pages

	public String getIndexName() {
		return indexName;
	}
	public IndexType getIndexType(){
		return accessType;
	}
	public String getRelationName(){
		return relName;
	}
};

