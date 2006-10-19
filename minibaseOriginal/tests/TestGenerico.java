package tests;

import global.AttrType;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.RID;
import global.SystemDefs;
import heap.FieldNumberOutOfBoundException;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.SpaceNotAvailableException;
import heap.Tuple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import catalog.Catalogrelexists;
import catalog.Utility;
import catalog.attrInfo;

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
		//Para windows
		//String logpath = "c:\\windows\\temp\\testlog";
		//String logpath = "c:\\windows\\temp\\minibase.testdb";
		
		//Para unix
		String logpath = "/tmp/testlog";
		String dbpath = "/tmp/minibase.testdb";
		
		File file = new File(logpath);
		file.delete();
		file = new File(dbpath);
		file.delete();
		
		//Con catalogo
		ExtendedSystemDefs extSysDef = new ExtendedSystemDefs( dbpath, logpath, 1000,500,200,"Clock");
		//Sin catalogo
		//SystemDefs sysdef = new SystemDefs( dbpath, 1000, NUMBUF, "Clock" );
		
		List<attrInfo> attributes = new ArrayList<attrInfo>();
		
		attributes.add(new attrInfo("sid", new AttrType(AttrType.attrInteger), 0));
		attributes.add(new attrInfo("sname", new AttrType(AttrType.attrString), 30));
		attributes.add(new attrInfo("rating", new AttrType(AttrType.attrInteger), 0));
		attributes.add(new attrInfo("age", new AttrType(AttrType.attrReal), 0));
		attrInfo[] list = new attrInfo[attributes.size()];
		attributes.toArray(list);
		
		try {
			SystemDefs.JavabaseCatalog.createRel("sailors",list);
			
		} catch (Catalogrelexists e1) {
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Utility.loadRecordsUT("sailors","sailors.db");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		/*
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
		*/
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
