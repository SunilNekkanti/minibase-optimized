package tests;

import global.*;
import heap.*;

import java.io.*;
import java.util.*;

class Column{
	
	public Column (String value,int attrType,int strSize){
		this.value = new String();
		this.type = attrType;
		this.strSize = strSize;
	}
	
	String getValue (){
		return value;
	}
	
	int getType (){
		return type;
	}
	
	int getSize (){
		return strSize;
	}
	
	private String value;
	private int type;
	private int strSize;
}

public class TestGenerico  implements GlobalConst {
	public static void main(String argv[])  {
		String logpath = "/tmp/testlog";
		String dbpath = "/tmp/minibase.testdb";

		//Para unix
		String remove_cmd = "/bin/rm -rf ";
		//Para windows:
		//String remove_cmd = "cmd /k del ";
		
		String remove_logcmd = remove_cmd + logpath;
		String remove_dbcmd = remove_cmd + dbpath;
		
		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
		}
		catch (IOException e) {
			System.err.println (""+e);
		}
		
		
		//ExtendedSystemDefs extSysDef = new ExtendedSystemDefs( dbpath, logpath, 1000,500,200,"Clock");
		SystemDefs sysdef = new SystemDefs( dbpath, 1000, NUMBUF, "Clock" );
		
		ArrayList<Column> columns = new ArrayList<Column>();
		columns.add(new Column("id",AttrType.attrInteger,0));
		columns.add(new Column("nombre",AttrType.attrString,20));
		columns.add(new Column("edad",AttrType.attrReal,0));
		ArrayList<ArrayList<String> > records = new ArrayList<ArrayList<String> >();
		ArrayList<String> record = new ArrayList<String>();
		record.add("124");
		record.add("Jorge");
		record.add("24");
		records.add(record);
		
		try {
			Heapfile table = new Heapfile("marinos.in");
			insertRecords(table,columns,records);
		} catch (Exception e) {
			System.err.println (""+e);
		}
	}
	
	public static void createTable (String tableName,List<Column> columns) throws InvalidTypeException, InvalidTupleSizeException, IOException, HFException, HFBufMgrException, HFDiskMgrException, InvalidSlotNumberException, SpaceNotAvailableException {
		
		ArrayList<AttrType> types = new ArrayList<AttrType>();
		ArrayList<Integer>  sTypes = new ArrayList<Integer>();
		
		Iterator it = columns.iterator();
		for(int i=0;it.hasNext();i++){
			Column column = (Column)it.next();
			
			types.add(new AttrType (column.getType()));
			
			//Si es de tipo string nos fijamos el tamaño
			if(column.getType() == AttrType.attrString){
				sTypes.add(column.getSize());
			}
		}
		
		short [] sizeTypes = new short[sTypes.size()];
		
		for(int i=0;i<sizeTypes.length;i++){
			sizeTypes[i] = (short)sTypes.get(i).intValue();
		}
		
		AttrType [] attrType = new AttrType[types.size()];
		int i=0;
		for( AttrType type: types){
			attrType[i] = type;
			i++;
		}
		
		
		Heapfile table = new Heapfile(tableName);
		Tuple tuple = new Tuple();
		tuple.setHdr((short) columns.size(),attrType,sizeTypes);
		
		table.insertRecord(tuple.returnTupleByteArray());
	}
	
	public static List<RID> insertRecords (Heapfile table,List<Column> columns,ArrayList<ArrayList<String>> records) throws InvalidTypeException, InvalidTupleSizeException, IOException, NumberFormatException, FieldNumberOutOfBoundException, InvalidSlotNumberException, SpaceNotAvailableException, HFException, HFBufMgrException, HFDiskMgrException{
		ArrayList<AttrType> types = new ArrayList<AttrType>();
		ArrayList<Integer>  sTypes = new ArrayList<Integer>();
		
		Iterator it = columns.iterator();
		for(int i=0;it.hasNext();i++){
			Column column = (Column)it.next();
			
			types.add(new AttrType (column.getType()));
			
			//Si es de tipo string nos fijamos el tamaño
			if(column.getType() == AttrType.attrString){
				sTypes.add(column.getSize());
			}
		}
		
		short [] sizeTypes = new short[sTypes.size()];
		
		for(int i=0;i<sizeTypes.length;i++){
			sizeTypes[i] = (short)sTypes.get(i).intValue();
		}
		
		AttrType [] attrType = new AttrType[types.size()];
		int i=0;
		for( AttrType type: types){
			attrType[i] = type;
			i++;
		}
		
		
		Tuple tuple = new Tuple();
		tuple.setHdr((short) columns.size(),attrType,sizeTypes);
		int size = tuple.size();
		tuple = new Tuple(size);
		tuple.setHdr((short) columns.size(),attrType,sizeTypes);
		
		ArrayList<RID> rids = new ArrayList<RID>();
		for(List<String> record : records){
			i=1;
			for(Column column:columns){
				
				switch(column.getType()){
				
				case AttrType.attrInteger:
					tuple.setIntFld(i,Integer.parseInt(record.get(i-1)));
					break;
				case AttrType.attrReal:
					tuple.setFloFld(i,Float.parseFloat(record.get(i-1)));
					break;
				case AttrType.attrString:
					tuple.setStrFld(i,record.get(i-1));
					break;
				}
				
				i++;
			}
			rids.add(table.insertRecord(tuple.returnTupleByteArray()));
			tuple.print(attrType);
		}
		
		return rids;
	}
	
	public void printAll (Heapfile table, List<Column> columns,RID rid) throws InvalidSlotNumberException, InvalidTupleSizeException, HFException, HFDiskMgrException, HFBufMgrException, Exception{
		ArrayList<AttrType> types = new ArrayList<AttrType>();
		
		Iterator it = columns.iterator();
		while(it.hasNext()){
			Column column = (Column)it.next();
			
			types.add(new AttrType (column.getType()));
		}
		
		AttrType [] attrType = new AttrType[types.size()];
		int i=0;
		for( AttrType type: types){
			attrType[i] = type;
			i++;
		}
		
		Tuple tuple = table.getRecord(rid);
		tuple.print(attrType);
	}
}
