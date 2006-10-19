//------------------------------------
// attrInfo.java
//
// Ning Wang, April,24  1998
//-------------------------------------

package catalog;
import global.*;

// attrInfo class used for creating relations
/**
 * @author  Fernando
 */
public class attrInfo
{
	public attrInfo(String name, AttrType type, int len){
		this.attrName = name;
		this.attrType = type;
		this.attrLen = len;
	}
  public String   attrName;           // attribute name
  public AttrType attrType;           // INTEGER, FLOAT, or STRING
  public int      attrLen = 0;        // length
}; 

