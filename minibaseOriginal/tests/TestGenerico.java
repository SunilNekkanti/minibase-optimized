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
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Scan;
import heap.Tuple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import catalog.AttrCatalogException;
import catalog.AttrDesc;
import catalog.Catalogattrnotfound;
import catalog.Cataloghferror;
import catalog.Catalogindexnotfound;
import catalog.Catalogioerror;
import catalog.Catalogmissparam;
import catalog.Catalognomem;
import catalog.Catalogrelexists;
import catalog.Catalogrelnotfound;
import catalog.Utility;
import catalog.attrInfo;

public class TestGenerico  implements GlobalConst {
	public static void main(String argv[]) throws InvalidTupleSizeException, IOException, FieldNumberOutOfBoundException, InvalidTypeException, Catalogmissparam, Catalogioerror, Cataloghferror, AttrCatalogException, Catalognomem, Catalogattrnotfound, Catalogindexnotfound, Catalogrelnotfound, HFException, HFBufMgrException, HFDiskMgrException  {
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
		//ExtendedSystemDefs extSysDef = 
		new ExtendedSystemDefs( dbpath, logpath, 1000,500,200,"Clock");
		//Sin catalogo
		//SystemDefs sysdef = new SystemDefs( dbpath, 1000, NUMBUF, "Clock" );
		
		try {
			Utility.loadRecordsUT("sailors","sailors.db");
			Utility.loadRecordsUT("boats","boats.db");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Iterator it = SystemDefs.JavabaseCatalog.getRelCat().getRelationName().iterator();
		
		while(it.hasNext()){
			String name = (String)it.next();
			System.out.println(name);
			AttrDesc[] attr = SystemDefs.JavabaseCatalog.getAttrCat().getRelInfo(name);
			AttrType[] types = new AttrType[attr.length];
			short[] stypes = new short[attr.length];
			
			String header = "";
			
			for(int i=0,j=0;i<attr.length;i++){
				header += attr[i].getName() + " ";
				types[i] = attr[i].getType();
				if(types[i].attrType == AttrType.attrString){
					stypes[j] = (short)attr[i].getLength();
					j++;
				}
			}
			System.out.println(header);
			
			Heapfile heap = new Heapfile(name);
			
			Scan scan = heap.openScan();
			
			Tuple tuple = null;
			
			while((tuple = scan.getNext(new RID()))!= null){
				tuple.setHdr((short)types.length,types,stypes);
				tuple.print(types);
			}
		}
		
		
		
	}
}
