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
import index.IndexScan;
import iterator.CondExpr;
import iterator.DuplElim;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.Iterator;
import iterator.SpeedJoin;
import iterator.NestedLoopsJoins;
import iterator.RelSpec;
import iterator.Sort;
import iterator.SortMerge;

import java.io.File;

import catalog.IndexDesc;
import catalog.Utility;

/**
   Here is the implementation for the tests. There are N tests performed.
   We start off by showing that each operator works on its own.
   Then more complicated trees are constructed.
   As a nice feature, we allow the user to specify a selection condition.
   We also allow the user to hardwire trees together.
 */


class JoinsDriver implements GlobalConst {

	private boolean OK = true;
	private boolean FAIL = false;

	/** Constructor
	 */
	public JoinsDriver() {

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

		//Con catalogo
		//ExtendedSystemDefs extSysDef = 
		new ExtendedSystemDefs( dbpath, logpath, 1000,500,200,"Clock");
		//Sin catalogo
		//SystemDefs sysdef = new SystemDefs( dbpath, 1000, NUMBUF, "Clock" );

		try {
			Utility.loadRecordsUT("sailors.in","sailors.db");
			Utility.loadRecordsUT("boats.in","boats.db"); 
			Utility.loadRecordsUT("reserves.in","reserves.db"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}

	public boolean runTests() {

		Disclaimer();
		Query1();

		Query2();
		Query3();
		SystemDefs.estadisticas = 0;
		Query4();
		System.out.println(SystemDefs.estadisticas);


		Query5();
		Query6();

		
		SystemDefs.estadisticas = 0;
		Query7();
		System.out.println(SystemDefs.estadisticas);
		
		
		SystemDefs.estadisticas = 0;
		Query8();
		System.out.println(SystemDefs.estadisticas);

		System.out.print ("Finished joins testing"+"\n");


		return true;
	} 

	private void Query1_CondExpr(CondExpr[] expr) {

		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrSymbol);
		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

		expr[1].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[1].next  = null;
		expr[1].type1 = new AttrType(AttrType.attrSymbol);
		expr[1].type2 = new AttrType(AttrType.attrInteger);
		expr[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);
		expr[1].operand2.integer = 1;

		expr[2] = null;
	}

	private void Query2_CondExpr(CondExpr[] expr, CondExpr[] expr2) {

		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrSymbol);
		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

		expr[1] = null;

		expr2[0].next  = null;
		expr2[0].op    = new AttrOperator(AttrOperator.aopEQ); 
		expr2[0].type1 = new AttrType(AttrType.attrSymbol);
		expr2[0].type2 = new AttrType(AttrType.attrSymbol);   
		expr2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
		expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

		expr2[1].op   = new AttrOperator(AttrOperator.aopEQ);
		expr2[1].next = null;
		expr2[1].type1 = new AttrType(AttrType.attrSymbol);
		expr2[1].type2 = new AttrType(AttrType.attrString);
		expr2[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
		expr2[1].operand2.string = "red";

		expr2[2] = null;
	}

	private void Query3_CondExpr(CondExpr[] expr) {

		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].type2 = new AttrType(AttrType.attrSymbol);
		expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);
		expr[1] = null;
	}

	private CondExpr[] Query5_CondExpr() {
		CondExpr [] expr2 = new CondExpr[3];
		expr2[0] = new CondExpr();


		expr2[0].next  = null;
		expr2[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr2[0].type1 = new AttrType(AttrType.attrSymbol);

		expr2[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer),1);
		expr2[0].type2 = new AttrType(AttrType.attrSymbol);

		expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

		expr2[1] = new CondExpr();
		expr2[1].op   = new AttrOperator(AttrOperator.aopGT);
		expr2[1].next = null;
		expr2[1].type1 = new AttrType(AttrType.attrSymbol);

		expr2[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),4);
		expr2[1].type2 = new AttrType(AttrType.attrReal);
		expr2[1].operand2.real = (float)40.0;


		expr2[1].next = new CondExpr();
		expr2[1].next.op   = new AttrOperator(AttrOperator.aopLT);
		expr2[1].next.next = null;
		expr2[1].next.type1 = new AttrType(AttrType.attrSymbol); // rating
		expr2[1].next.operand1.symbol = new FldSpec ( new RelSpec(RelSpec.outer),3);
		expr2[1].next.type2 = new AttrType(AttrType.attrInteger);
		expr2[1].next.operand2.integer = 7;

		expr2[2] = null;
		return expr2;
	}

	private void Query6_CondExpr(CondExpr[] expr, CondExpr[] expr2) {

		expr[0].next  = null;
		expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);

		expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
		expr[0].type2 = new AttrType(AttrType.attrSymbol);

		expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

		expr[1].next  = null;
		expr[1].op    = new AttrOperator(AttrOperator.aopGT);
		expr[1].type1 = new AttrType(AttrType.attrSymbol);

		expr[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),3);
		expr[1].type2 = new AttrType(AttrType.attrInteger);
		expr[1].operand2.integer = 7;

		expr[2] = null;

		expr2[0].next  = null;
		expr2[0].op    = new AttrOperator(AttrOperator.aopEQ);
		expr2[0].type1 = new AttrType(AttrType.attrSymbol);

		expr2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
		expr2[0].type2 = new AttrType(AttrType.attrSymbol);

		expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

		expr2[1].next = null;
		expr2[1].op   = new AttrOperator(AttrOperator.aopEQ);
		expr2[1].type1 = new AttrType(AttrType.attrSymbol);

		expr2[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
		expr2[1].type2 = new AttrType(AttrType.attrString);
		expr2[1].operand2.string = "red";

		expr2[2] = null;
	}

	public void Query1() {

		System.out.print("**********************Query1 strating *********************\n");
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.
		System.out.print ("Query: Find the names of sailors who have reserved "
				+ "boat number 1.\n"
				+ "       and print out the date of reservation.\n\n"
				+ "  SELECT S.sname, R.date\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid AND R.bid = 1\n\n");

		System.out.print ("\n(Tests FileScan, Projection, and Sort-Merge Join)\n");

		CondExpr[] outFilter = new CondExpr[3];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		outFilter[2] = new CondExpr();

		Query1_CondExpr(outFilter);

		FldSpec [] Sprojection = new FldSpec[4];
		Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
		Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);

		AttrType[] Stypes = null;
		short [] Ssizes = null;
		FileScan am = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
			am  = new FileScan("sailors.in", Stypes, Ssizes, Sprojection, null);
		} catch (Exception e) {
			//	bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] Rprojection = new FldSpec[3];
		Rprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Rprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Rprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);

		AttrType[] Rtypes = null;
		short [] Rsizes = null;
		FileScan am2 = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
			am2  = new FileScan("reserves.in", Rtypes, Rsizes, Rprojection, null);
		} catch (Exception e) {
			//	bail out
			System.err.println ("*** Error setting up scan for reserves");
			Runtime.getRuntime().exit(1);
		}   

		FldSpec [] proj_list = new FldSpec[2];
		proj_list[0] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		proj_list[1] = new FldSpec(new RelSpec(RelSpec.innerRel), 3);

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
			System.err.println("*** join error in SortMerge constructor ***"); 
			status = FAIL;
			System.err.println (""+e);
			e.printStackTrace();
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error constructing SortMerge");
			Runtime.getRuntime().exit(1);
		}



		QueryCheck qcheck1 = new QueryCheck(1);

		Tuple t = new Tuple();
		t = null;

		AttrType [] jtype = new AttrType[2];
		jtype[0] = new AttrType (AttrType.attrString);
		jtype[1] = new AttrType (AttrType.attrString);

		try {
			while ((t = sm.get_next()) != null) {
				t.print(jtype);

				qcheck1.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace();
			status = FAIL;
		}
		if (status != OK) {
			//bail out
			System.err.println ("*** Error in get next tuple ");
			Runtime.getRuntime().exit(1);
		}

		qcheck1.report(1);
		try {
			sm.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}
		System.out.println ("\n"); 
		if (status != OK) {
			//bail out
			System.err.println ("*** Error in closing ");
			Runtime.getRuntime().exit(1);
		}
	}

	public void Query2() {
		System.out.print("**********************Query2 strating *********************\n");
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.
		System.out.print 
		("Query: Find the names of sailors who have reserved "
				+ "a red boat\n"
				+ "       and return them in alphabetical order.\n\n"
				+ "  SELECT   S.sname\n"
				+ "  FROM     Sailors S, Boats B, Reserves R\n"
				+ "  WHERE    S.sid = R.sid AND R.bid = B.bid AND B.color = 'red'\n"
				+ "  ORDER BY S.sname\n"
				+ "Plan used:\n"
				+ " Sort (Pi(sname) (Sigma(B.color='red')  "
				+ "|><|  Pi(sname, bid) (S  |><|  R)))\n\n"
				+ "(Tests File scan, Index scan ,Projection,  index selection,\n "
				+ "sort and simple nested-loop join.)\n\n");

		// Build Index first
		IndexType b_index = new IndexType (IndexType.B_Index);

		try{
			SystemDefs.JavabaseCatalog.addIndex("sailors.in", "sid", b_index, 1);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.print ("Failure to add index.\n");
			Runtime.getRuntime().exit(1);
		}

		CondExpr [] outFilter  = new CondExpr[2];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();

		CondExpr [] outFilter2 = new CondExpr[3];
		outFilter2[0] = new CondExpr();
		outFilter2[1] = new CondExpr();
		outFilter2[2] = new CondExpr();

		Query2_CondExpr(outFilter, outFilter2);
		
		
		CondExpr [] selects = new CondExpr[1];
		selects[0] = null;

		// create the index file

		IndexDesc index = null;
		try {
			index = SystemDefs.JavabaseCatalog.getIndexInfo("sailors.in", "sid", b_index);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//_______________________________________________________________
		//*******************close an scan on the heapfile**************
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				// new FldSpec(new RelSpec(RelSpec.outer), 3),
				// new FldSpec(new RelSpec(RelSpec.outer), 4)
		};
		
		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		iterator.Iterator am = null;
		System.out.print ("After Building btree index on sailors.sid.\n\n");
		try {
			am = (iterator.Iterator)new IndexScan ( index.getIndexType(), index.getRelationName(),index.getIndexName(), Stypes, Ssizes,
					Sprojection, null, 1, false);
		}

		catch (Exception e) {
			System.err.println ("*** Error creating scan for Index scan");
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}


		AttrType [] Stypes2 = {
				new AttrType(AttrType.attrInteger), 
				new AttrType(AttrType.attrString), 
		};

		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		FldSpec []  proj1 = {
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.innerRel), 2)
		}; // S.sname, R.bid


		NestedLoopsJoins nlj = null;
		try {
			nlj = new NestedLoopsJoins (Stypes2, Ssizes,
					Rtypes,  Rsizes,
					10,
					am, "reserves.in",
					outFilter, null, proj1, 2);
		}
		catch (Exception e) {
			System.err.println ("*** Error preparing for nested_loop_join");
			System.err.println (""+e);
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}

		AttrType [] Btypes = null;
		short []   Bsizes = null;
		try {
			Btypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("boats.in");
			Bsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("boats.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		AttrType [] Jtypes = {
				new AttrType(AttrType.attrString), 
				new AttrType(AttrType.attrInteger), 
		};

		short  []  Jsizes = new short[1];
		Jsizes[0] = 30;
		
		FldSpec [] proj2  = {
				new FldSpec(new RelSpec(RelSpec.outer), 1)
		};
		
		NestedLoopsJoins nlj2 = null ; 
		try {
			nlj2 = new NestedLoopsJoins (Jtypes, Jsizes,
					Btypes, Bsizes,
					10,
					nlj, "boats.in",
					outFilter2, null, proj2, 1);
		}
		catch (Exception e) {
			System.err.println ("*** Error preparing for nested_loop_join");
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}
		
		AttrType [] JJtype = {
				new AttrType(AttrType.attrString), 
		};
		
		short [] JJsize = new short[1];
		JJsize[0] = 30;

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		Sort sort_names = null;
		try {
			sort_names = new Sort (JJtype, JJsize,
					(iterator.Iterator) nlj2, 1, ascending, JJsize[0], 10);
		}
		catch (Exception e) {
			System.err.println ("*** Error preparing for nested_loop_join");
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}


		QueryCheck qcheck2 = new QueryCheck(2);


		Tuple t = null;
		try {
			while ((t = sort_names.get_next()) != null) {
				t.print(JJtype);
				qcheck2.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}

		qcheck2.report(2);

		System.out.println ("\n"); 
		try {
			sort_names.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}

		if (status != OK) {
			//bail out

			Runtime.getRuntime().exit(1);
		}
	}


	public void Query3() {
		System.out.print("**********************Query3 strating *********************\n"); 
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.

		System.out.print 
		( "Query: Find the names of sailors who have reserved a boat.\n\n"
				+ "  SELECT S.sname\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid\n\n"
				+ "(Tests FileScan, Projection, and SortMerge Join.)\n\n");

		CondExpr [] outFilter = new CondExpr[2];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();

		Query3_CondExpr(outFilter);

		Tuple t = new Tuple();
		t = null;

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};

		iterator.Iterator am = null;
		try {
			am  = (iterator.Iterator)new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] Rprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3)
		}; 

		iterator.Iterator am2 = null;
		try {
			am2 = (iterator.Iterator)new FileScan("reserves.in", Rtypes, Rsizes,
					Rprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for reserves");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] proj_list = {
				new FldSpec(new RelSpec(RelSpec.outer), 2)
		};

		AttrType [] jtype     = { new AttrType(AttrType.attrString) };

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		SortMerge sm = null;
		try {
			sm = new SortMerge(Stypes,Ssizes,
					Rtypes, Rsizes,
					1, 4,
					1, 4,
					10,
					am, am2,
					false, false, ascending,
					outFilter, proj_list);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error constructing SortMerge");
			Runtime.getRuntime().exit(1);
		}

		QueryCheck qcheck3 = new QueryCheck(3);


		t = null;

		try {
			while ((t = sm.get_next()) != null) {
				t.print(jtype);
				qcheck3.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}


		qcheck3.report(3);

		System.out.println ("\n"); 
		try {
			sm.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}
	}

	public void Query4() {
		System.out.print("**********************Query4 strating *********************\n");
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.

		System.out.print 
		("Query: Find the names of sailors who have reserved a boat\n"
				+ "       and print each name once.\n\n"
				+ "  SELECT DISTINCT S.sname\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid\n\n"
				+ "(Tests FileScan, Projection, Sort-Merge Join and "
				+ "Duplication elimination.)\n\n");

		CondExpr [] outFilter = new CondExpr[2];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();

		Query3_CondExpr(outFilter);

		Tuple t = new Tuple();
		t = null;

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};

		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] Rprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3)
		}; 

		iterator.Iterator am2 = null;
		try {
			am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
					Rprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for reserves");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] proj_list = {
				new FldSpec(new RelSpec(RelSpec.outer), 2)
		};

		AttrType [] jtype     = { new AttrType(AttrType.attrString) };

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		SortMerge sm = null;
		short  []  jsizes    = new short[1];
		jsizes[0] = 30;
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
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
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

		QueryCheck qcheck4 = new QueryCheck(4);


		t = null;

		try {
			while ((t = ed.get_next()) != null) {
				t.print(jtype);
				qcheck4.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace(); 
			Runtime.getRuntime().exit(1);
		}

		qcheck4.report(4);
		try {
			ed.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}
		System.out.println ("\n");  
		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}
	}

	public void Query5() {
		System.out.print("**********************Query5 strating *********************\n");  
		boolean status = OK;
		// Sailors, Boats, Reserves Queries.

		System.out.print 
		("Query: Find the names of old sailors or sailors with "
				+ "a rating less\n       than 7, who have reserved a boat, "
				+ "(perhaps to increase the\n       amount they have to "
				+ "pay to make a reservation).\n\n"
				+ "  SELECT S.sname, S.rating, S.age\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid and (S.age > 40 || S.rating < 7)\n\n"
				+ "(Tests FileScan, Multiple Selection, Projection, "
				+ "and Sort-Merge Join.)\n\n");


		CondExpr [] outFilter;
		outFilter = Query5_CondExpr();

		Tuple t = new Tuple();
		t = null;

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};

		CondExpr[] selects = new CondExpr [1];
		selects[0] = null;

		FldSpec [] proj_list = {
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};

		FldSpec [] Rprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3)
		};

		AttrType [] jtype     = { 
				new AttrType(AttrType.attrString), 
				new AttrType(AttrType.attrInteger), 
				new AttrType(AttrType.attrReal)
		};


		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes, 
					Sprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		iterator.Iterator am2 = null;
		try {
			am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
					Rprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
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
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error constructing SortMerge");
			Runtime.getRuntime().exit(1);
		}

		QueryCheck qcheck5 = new QueryCheck(5);
		//Tuple t = new Tuple();
		t = null;

		try {
			while ((t = sm.get_next()) != null) {
				t.print(jtype);
				qcheck5.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}

		qcheck5.report(5);
		try {
			sm.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}
		System.out.println ("\n"); 
		if (status != OK) {
			//bail out
			System.err.println ("*** Error close for sortmerge");
			Runtime.getRuntime().exit(1);
		}
	}

	public void Query6()
	{
		System.out.print("**********************Query6 strating *********************\n");
		boolean status = OK;
		// Sailors, Boats, Reserves Queries.
		System.out.print( "Query: Find the names of sailors with a rating greater than 7\n"
				+ "  who have reserved a red boat, and print them out in sorted order.\n\n"
				+ "  SELECT   S.sname\n"
				+ "  FROM     Sailors S, Boats B, Reserves R\n"
				+ "  WHERE    S.sid = R.sid AND S.rating > 7 AND R.bid = B.bid \n"
				+ "           AND B.color = 'red'\n"
				+ "  ORDER BY S.name\n\n"

				+ "Plan used:\n"
				+" Sort(Pi(sname) (Sigma(B.color='red')  |><|  Pi(sname, bid) (Sigma(S.rating > 7)  |><|  R)))\n\n"

				+ "(Tests FileScan, Multiple Selection, Projection,sort and nested-loop join.)\n\n");

		CondExpr [] outFilter  = new CondExpr[3];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		outFilter[2] = new CondExpr();
		CondExpr [] outFilter2 = new CondExpr[3];
		outFilter2[0] = new CondExpr();
		outFilter2[1] = new CondExpr();
		outFilter2[2] = new CondExpr();

		Query6_CondExpr(outFilter, outFilter2);
		Tuple t = new Tuple();
		t = null;

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		AttrType [] Btypes = null;
		short []   Bsizes = null;
		try {
			Btypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("boats.in");
			Bsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("boats.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		AttrType [] Jtypes = {
				new AttrType(AttrType.attrString), 
				new AttrType(AttrType.attrInteger), 
		};

		short  []  Jsizes = new short[1];
		Jsizes[0] = 30;
		AttrType [] JJtype = {
				new AttrType(AttrType.attrString), 
		};

		short [] JJsize = new short[1];
		JJsize[0] = 30; 



		FldSpec []  proj1 = {
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.innerRel), 2)
		}; // S.sname, R.bid

		FldSpec [] proj2  = {
				new FldSpec(new RelSpec(RelSpec.outer), 1)
		};

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};





		FileScan am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes, 
					Sprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
			e.printStackTrace();
		}

		if (status != OK) {
			//bail out

			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}



		NestedLoopsJoins inl = null;
		try {
			inl = new NestedLoopsJoins (Stypes,  Ssizes,
					Rtypes,  Rsizes,
					10,
					am, "reserves.in",
					outFilter, null, proj1, 2);
		}
		catch (Exception e) {
			System.err.println ("*** Error preparing for nested_loop_join");
			System.err.println (""+e);
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}

		System.out.print( "After nested loop join S.sid|><|R.sid.\n");

		NestedLoopsJoins nlj = null;
		try {
			nlj = new NestedLoopsJoins (Jtypes, Jsizes,
					Btypes,  Bsizes,
					10,
					inl, "boats.in",
					outFilter2, null, proj2, 1);
		}
		catch (Exception e) {
			System.err.println ("*** Error preparing for nested_loop_join");
			System.err.println (""+e);
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}

		System.out.print( "After nested loop join R.bid|><|B.bid AND B.color=red.\n");

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		Sort sort_names = null;
		try {
			sort_names = new Sort (JJtype, JJsize,
					(iterator.Iterator) nlj, 1, ascending, JJsize[0], 10);
		}
		catch (Exception e) {
			System.err.println ("*** Error preparing for sorting");
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}


		System.out.print( "After sorting the output tuples.\n");


		QueryCheck qcheck6 = new QueryCheck(6);

		try {
			while ((t =sort_names.get_next()) !=null) {
				t.print(JJtype);
				qcheck6.Check(t);
			}
		}catch (Exception e) {
			System.err.println ("*** Error preparing for get_next tuple");
			System.err.println (""+e);
			Runtime.getRuntime().exit(1);
		}

		qcheck6.report(6);

		System.out.println ("\n"); 
		try {
			sort_names.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}

		if (status != OK) {
			//bail out

			Runtime.getRuntime().exit(1);
		}

	}


	public void Query7() {
		System.out.print("**********************Query7 strating *********************\n");
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.

		System.out.print 
		("Query: Find the names of sailors who have reserved a boat\n"
				+ "       and print each name once.\n\n"
				+ "  SELECT DISTINCT S.sname\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid\n\n"
				+ "(Tests FileScan, Projection, Leandro Join and "
				+ "Duplication elimination.)\n\n");

		CondExpr [] outFilter = new CondExpr[2];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();

		Query3_CondExpr(outFilter);

		Tuple t = new Tuple();
		t = null;

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};

		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] Rprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3)
		}; 
		
		
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
		

		
		System.out.println("BTreeIndex created successfully.\n"); 
		
		System.out.println("BTreeIndex file created successfully.\n"); 


		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for reserves");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] proj_list = {
				new FldSpec(new RelSpec(RelSpec.outer), 2)
		};

		AttrType [] jtype     = { new AttrType(AttrType.attrString) };

		SpeedJoin lj = null;
		short  []  jsizes    = new short[1];
		jsizes[0] = 30;
		try {
			lj = new SpeedJoin(
					Stypes, Ssizes,
					Rtypes, Rsizes,
					10,	am, 1, 
					"reserves.in",index.getIndexName(),
					outFilter, proj_list);
			
			
			
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error constructing SortMerge");
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

		QueryCheck qcheck4 = new QueryCheck(4);


		t = null;

		try {
			while ((t = ed.get_next()) != null) {
				t.print(jtype);
				qcheck4.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace(); 
			Runtime.getRuntime().exit(1);
		}

		qcheck4.report(7);
		try {
			ed.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}
		System.out.println ("\n");  
		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}
	}
	
	public void Query8() {
		System.out.print("**********************Query8 strating *********************\n");
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.

		System.out.print 
		("Query: Find the names of sailors who have reserved a boat\n"
				+ "       and print each name once.\n\n"
				+ "  SELECT DISTINCT S.sname\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid\n\n"
				+ "(Tests FileScan, Projection, nestedloops Join and "
				+ "Duplication elimination.)\n\n");

		CondExpr [] outFilter = new CondExpr[2];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();

		Query3_CondExpr(outFilter);

		Tuple t = new Tuple();
		t = null;

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("sailors.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("sailors.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AttrType [] Rtypes = null;
		short []   Rsizes = null;
		try {
			Rtypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("reserves.in");
			Rsizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("reserves.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FldSpec [] Sprojection = {
				new FldSpec(new RelSpec(RelSpec.outer), 1),
				new FldSpec(new RelSpec(RelSpec.outer), 2),
				new FldSpec(new RelSpec(RelSpec.outer), 3),
				new FldSpec(new RelSpec(RelSpec.outer), 4)
		};

		iterator.Iterator am = null;
		try {
			am  = new FileScan("sailors.in", Stypes, Ssizes,
					Sprojection, null);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		FldSpec [] proj_list = {
				new FldSpec(new RelSpec(RelSpec.outer), 2)
		};

		AttrType [] jtype     = { new AttrType(AttrType.attrString) };

		NestedLoopsJoins nlj = null;
		short  []  jsizes    = new short[1];
		jsizes[0] = 30;
		try {
			nlj = new NestedLoopsJoins(Stypes, Ssizes,
					Rtypes, Rsizes,
					10,	am, "reserves.in",
					outFilter, null, proj_list, proj_list.length);
		}
		catch (Exception e) {
			status = FAIL;
			System.err.println (""+e);
		}

		if (status != OK) {
			//bail out
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

		QueryCheck qcheck4 = new QueryCheck(4);


		t = null;

		try {
			while ((t = ed.get_next()) != null) {
				t.print(jtype);
				qcheck4.Check(t);
			}
		}
		catch (Exception e) {
			System.err.println (""+e);
			e.printStackTrace(); 
			Runtime.getRuntime().exit(1);
		}

		qcheck4.report(8);
		try {
			ed.close();
		}
		catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}
		System.out.println ("\n");  
		if (status != OK) {
			//bail out
			System.err.println ("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}
	}


	private void Disclaimer() {
		System.out.print ("\n\nAny resemblance of persons in this database to"
				+ " people living or dead\nis purely coincidental. The contents of "
				+ "this database do not reflect\nthe views of the University,"
				+ " the Computer  Sciences Department or the\n"
				+ "developers...\n\n");
	}
}

public class JoinTest
{
	public static void main(String argv[])
	{
		boolean sortstatus;

		JoinsDriver jjoin = new JoinsDriver();

		sortstatus = jjoin.runTests();
		if (sortstatus != true) {
			System.out.println("Error ocurred during join tests");
		}
		else {
			System.out.println("join tests completed successfully");
		}
	}
}

