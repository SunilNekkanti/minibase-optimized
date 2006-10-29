package tests;

import global.AttrType;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.RID;
import global.SystemDefs;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;

import java.io.File;
import java.util.Iterator;

import catalog.AttrDesc;
import catalog.Utility;
import catalog.exceptions.Catalogattrexists;
import catalog.exceptions.Catalogbadattrcount;
import catalog.exceptions.Catalogbadtype;
import catalog.exceptions.Catalogdupattrs;
import catalog.exceptions.Catalogrelexists;

public class TestGenerico  implements GlobalConst {
	public static void main(String argv[]) throws Catalogrelexists, Catalogdupattrs, Catalogbadattrcount, Catalogattrexists, Catalogbadtype, Exception  {
		//Para windows
		//String logpath = "c:\\windows\\temp\\testlog";
		//String logpath = "c:\\windows\\temp\\minibase.testdb";
		
		//Para unix
		String logpath = "c:\\windows\\temp\\testlog";
		String dbpath = "c:\\windows\\temp\\minibase.testdb";
		
		File file = new File(logpath);
		file.delete();
		file = new File(dbpath);
		file.delete();
		
		//Con catalogo
		//ExtendedSystemDefs extSysDef = 
		new ExtendedSystemDefs( dbpath, logpath, 1000,500,200,"Clock");
		//Sin catalogo
		//SystemDefs sysdef = new SystemDefs( dbpath, 1000, NUMBUF, "Clock" );
		
		Utility.loadRecordsUT("sailors","sailors.db");
		Utility.loadRecordsUT("boats","boats.db");
		
		Iterator it = SystemDefs.JavabaseCatalog.getRelCat().getRelationNames().iterator();
		
		while(it.hasNext()){
			String name = (String)it.next();
			System.out.println(name);
			
			AttrDesc[] attr = SystemDefs.JavabaseCatalog.getAttrCat().getRelInfo(name);
			
			String header = "";
			
			for(int i=0;i<attr.length;i++){
				header += attr[i].getName() + " ";
			}
			
			System.out.println(header);
			
			Heapfile heap = new Heapfile(name);
			Scan scan = heap.openScan();
			
			AttrType[] types = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType(name);
			Tuple tuple = null;
			while((tuple = scan.getNext(new RID()))!= null){
				SystemDefs.JavabaseCatalog.getAttrCat().getTupleStructure(name, tuple);
				tuple.print(types);
			}
		}
	}
}
