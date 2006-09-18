package btree;

/**
 * IntegerKey: It extends the KeyClass. It defines the integer Key.
 */ 
public class IntegerKey extends KeyClass {

  private Integer key;

  public String toString(){
     return key.toString();
  }

  /** Class constructor
   *  @param     value   the value of the integer key to be set 
   */
  public IntegerKey(Integer value) 
  { 
    key=new Integer(value.intValue());
  }

  /** Class constructor
   *  @param     value   the value of the integer key to be set 
   */
  public IntegerKey(int value) 
  { 
    key=new Integer(value);
  }



  /**
 * get a copy of the integer key
 * @return  the reference of the copy
 * @uml.property  name="key"
 */
  public Integer getKey() 
  {
    return new Integer(key.intValue());
  }

  /**
 * set the integer key value
 * @uml.property  name="key"
 */  
  public void setKey(Integer value) 
  { 
    key=new Integer(value.intValue());
  }
}
