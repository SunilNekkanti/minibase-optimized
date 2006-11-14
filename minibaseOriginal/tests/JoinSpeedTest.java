package tests;
//originally from : joins.C

import global.AttrOperator;
import global.AttrType;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.IndexType;
import global.SystemDefs;
import global.TupleOrder;
import heap.Tuple;
import iterator.CondExpr;
import iterator.DuplElim;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.SpeedJoin;
import iterator.NestedLoopsJoins;
import iterator.RelSpec;
import iterator.SortMerge;

import java.io.File;

import catalog.IndexDesc;
import catalog.Utility;



class JoinsSpeedDriver implements GlobalConst {

	private CondExpr[] outFilter;
	private AttrType [] Stypes = null;
	private short []   Ssizes = null;
	private AttrType [] Rtypes = null;
	private short []   Rsizes = null;
	private FldSpec [] Sprojection = {
			new FldSpec(new RelSpec(RelSpec.outer), 1),
			new FldSpec(new RelSpec(RelSpec.outer), 2),
			new FldSpec(new RelSpec(RelSpec.outer), 3),
			new FldSpec(new RelSpec(RelSpec.outer), 4)
	};
	private FldSpec [] Rprojection = {
			new FldSpec(new RelSpec(RelSpec.outer), 1),
			new FldSpec(new RelSpec(RelSpec.outer), 2),
			new FldSpec(new RelSpec(RelSpec.outer), 3)
	}; 
	private FldSpec [] proj_list = {
			new FldSpec(new RelSpec(RelSpec.outer), 2)
	};
	private AttrType [] jtype     = { new AttrType(AttrType.attrString) };
	private short [] jsizes = { 30 };
	/** Constructor
	 */
	public JoinsSpeedDriver() {

		String logpath,dbpath;
		
		if (System.getProperty("os.name").contains("Windows")){
			//	Para windows
			logpath = "c:\\windows\\temp\\jointest.testlog";
			dbpath = "c:\\windows\\temp\\jointest.minibase.testdb";
		} else {
			//Para unix
			logpath = "/tmp/jointest.minibase-log";
			dbpath = "/tmp/jointest.minibase-db";
		}

		File file = new File(logpath);
		file.delete();
		file = new File(dbpath);
		file.delete();

		new ExtendedSystemDefs( dbpath, logpath, 1000,500,200,"Clock");

		try {
			Utility.loadRecordsUT("sailors.in","sailors.db");
			Utility.loadRecordsUT("boats.in","boats.db"); 
			Utility.loadRecordsUT("reserves.in","reserves.db"); 
		} catch (Exception e) {
			e.printStackTrace();
		}    
		outFilter = new CondExpr[2];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		Query_CondExpr(outFilter);

		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	
	}

	public boolean runTests() {

		System.out.print 
		("Query: Find the names of sailors who have reserved a boat\n"
				+ "       and print each name once.\n\n"
				+ "  SELECT DISTINCT S.sname\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid\n\n");

		
		
		SystemDefs.estadisticas = 0;
		QuerySortMerge();
		System.out.println("SortMergeJoin hace " + SystemDefs.estadisticas + " get_next\n");
		
		SystemDefs.estadisticas = 0;
		QueryLeandroJoin();
		System.out.println("LeandroJoin hace " + SystemDefs.estadisticas + " get_next\n");
		
		
		SystemDefs.estadisticas = 0;
		QueryNestedLoops();
		System.out.println("NestedLoopsJoin hace " + SystemDefs.estadisticas + " get_next\n");

		System.out.print ("Finished joins speed testing"+"\n");


		return true;
	} 


	private void Query_CondExpr(CondExpr[] expr) {
		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].type2 = new AttrType(AttrType.attrSymbol);
		expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);
		expr[1] = null;
	}


	public void QuerySortMerge() {
		System.out.print("***** Query sortmerge starting *****\n");

		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			System.err.println (""+e);
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		iterator.Iterator am2 = null;
		try {
			am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
					Rprojection, null);
		}
		catch (Exception e) {
			System.err.println (""+e);
			System.err.println ("*** Error setting up scan for reserves");
			Runtime.getRuntime().exit(1);
		}

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		SortMerge sm = null;
		try {
			sm = new SortMerge(Stypes, Ssizes,
					Rtypes, Rsizes,
					1, 4,
					1, 4,
					10,
					am, am2,
					false, false, ascending,
					outFilter, proj_list);
		}
		catch (Exception e) {
			System.err.println (""+e);
			System.err.println ("*** Error constructing SortMerge");
			Runtime.getRuntime().exit(1);
		}

		DuplElim ed = null;
		try {
			ed = new DuplElim(jtype, (short)1, jsizes, sm, 10, false);
		}
		catch (Exception e) {
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}

		QueryCheck qcheck = new QueryCheck(4);

		Tuple t = null;
		try {
			while ((t = ed.get_next()) != null) {
				//t.print(jtype);
				qcheck.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace(); 
			Runtime.getRuntime().exit(1);
		}

		qcheck.report("SortMerge");
		try {
			ed.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}
	}

	public void QueryLeandroJoin() {
		System.out.println("***** Query LeandroJoin starting *****");

		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			System.err.println (""+e);
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		
		try {
			SystemDefs.JavabaseCatalog.addIndex("reserves.in", "sid", new IndexType (IndexType.B_Index), 1);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		IndexDesc index = null;
		try {
			index = SystemDefs.JavabaseCatalog.getIndexInfo("reserves.in", "sid", new IndexType (IndexType.B_Index));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("BTreeIndex created successfully."); 

		SpeedJoin lj = null;
		try {
			lj = new SpeedJoin(
					Stypes, Ssizes,
					Rtypes, Rsizes,
					10,	am, 1, 
					"reserves.in",index.getIndexName(),
					outFilter, proj_list);			
		}
		catch (Exception e) {
			System.err.println ("*** Error constructing LeandroJoin\n" + e);
			Runtime.getRuntime().exit(1);
		}

		DuplElim ed = null;
		try {
		ed = new DuplElim(jtype, (short)1, jsizes, lj, 10, false);
		}
		catch (Exception e) {
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}

		QueryCheck qcheck = new QueryCheck(4);


		Tuple t = new Tuple();
		try {
			while ((t = ed.get_next()) != null) {
				//t.print(jtype);
				qcheck.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace(); 
			Runtime.getRuntime().exit(1);
		}

		qcheck.report("LeandroJoin");
		try {
			ed.close();
		}
		catch (Exception e) {
			System.err.println ("*** Error setting up scan for sailors\n"+e);
			Runtime.getRuntime().exit(1);
		}
	}
	
	public void QueryNestedLoops() {
		System.out.println("***** Query NestedLoopsJoin starting ***** ");

		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			System.err.println (""+e);
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		NestedLoopsJoins nlj = null;
		try {
			nlj = new NestedLoopsJoins(Stypes, Ssizes,
					Rtypes, Rsizes,
					10,	am, "reserves.in",
					outFilter, null, proj_list, proj_list.length);
		}
		catch (Exception e) {
			System.err.println (""+e);
			System.err.println ("*** Error constructing SortMerge");
			Runtime.getRuntime().exit(1);
		}

		DuplElim ed = null;
		try {
			ed = new DuplElim(jtype, (short)1, jsizes, nlj, 10, false);
		}
		catch (Exception e) {
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}

		QueryCheck qcheck = new QueryCheck(4);

		Tuple t = null;

		try {
			while ((t = ed.get_next()) != null) {
				//t.print(jtype);
				qcheck.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace(); 
			Runtime.getRuntime().exit(1);
		}

		qcheck.report("NestedLoopsJoin");
		try {
			ed.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}
	}
}

public class JoinSpeedTest
{
	public static void main(String argv[])
	{
		boolean sortstatus;

		JoinsSpeedDriver jjoin = new JoinsSpeedDriver();

		sortstatus = jjoin.runTests();
		if (sortstatus != true) {
			System.out.println("Error ocurred during join tests");
		}
		else {
			System.out.println("join tests completed successfully");
		}
	}
}