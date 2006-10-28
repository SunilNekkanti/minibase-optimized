//------------------------------------
// AttrDesc.java
//
// Ning Wang, April,24  1998
//-------------------------------------

package catalog;

import global.*;

// AttrDesc class: schema of attribute catalog:
/**
 * @author  Fernando
 */
public class AttrDesc
{
	public AttrDesc(){
		minVal = new attrData();
		maxVal = new attrData();
	}
	
	public String getName(){
		return attrName;
	}
	
	public int getLength(){
		return attrLen;
	}
	
	public AttrType getType(){
		return attrType;
	}
	
	public boolean isPk(){
		return pk;
	}
	
	public void setPk(boolean pk){
		this.pk = pk;
	}
	
	String relName;                       // relation name
	String attrName;                      // attribute name
	int      attrOffset = 0;                  // attribute offset
	int      attrPos = 0;                     // attribute position
	AttrType attrType;                    // attribute type
	int      attrLen = 0;                     // attribute length
	int      indexCnt = 0;                    // number of indexes
	attrData minVal;                      // min max key values
	attrData maxVal;
	boolean pk = false;
};


