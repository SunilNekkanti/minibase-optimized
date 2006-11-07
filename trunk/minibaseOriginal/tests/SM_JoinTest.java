package tests;
//originally from : joins.C

import global.AttrOperator;
import global.AttrType;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.TupleOrder;
import heap.Tuple;
import iterator.CondExpr;
import iterator.DuplElim;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.RelSpec;
import iterator.SortMerge;

import java.io.File;

import catalog.Utility;

/**
 Here is the implementation for the tests. There are N tests performed.
 We start off by showing that each operator works on its own.
 Then more complicated trees are constructed.
 As a nice feature, we allow the user to specify a selection condition.
 We also allow the user to hardwire trees together.
 */

//Define the Sailor schema



//Define the Reserves schema




public class SM_JoinTest
{
	
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
			new ExtendedSystemDefs( dbpath, logpath, 1000,500,NUMBUF,"Clock");
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
			
			
			Query4();
			Query5();
			Query6();
			
			
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
			
			Tuple t = new Tuple();
			
			AttrType [] Stypes = new AttrType[4];
			Stypes[0] = new AttrType (AttrType.attrInteger);
			Stypes[1] = new AttrType (AttrType.attrString);
			Stypes[2] = new AttrType (AttrType.attrInteger);
			Stypes[3] = new AttrType (AttrType.attrReal);
			
			//SOS
			short [] Ssizes = new short[1];
			Ssizes[0] = 30; //first elt. is 30
			
			FldSpec [] Sprojection = new FldSpec[4];
			Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
			Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
			Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
			Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);
			
			FileScan am = null;
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
			
			AttrType [] Rtypes = new AttrType[3];
			Rtypes[0] = new AttrType (AttrType.attrInteger);
			Rtypes[1] = new AttrType (AttrType.attrInteger);
			Rtypes[2] = new AttrType (AttrType.attrString);
			
			short [] Rsizes = new short[1];
			Rsizes[0] = 15; 
			FldSpec [] Rprojection = new FldSpec[3];
			Rprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
			Rprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
			Rprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
			
			FileScan am2 = null;
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
			
			
			FldSpec [] proj_list = new FldSpec[2];
			proj_list[0] = new FldSpec(new RelSpec(RelSpec.outer), 2);
			proj_list[1] = new FldSpec(new RelSpec(RelSpec.innerRel), 3);
			
			AttrType [] jtype = new AttrType[2];
			jtype[0] = new AttrType (AttrType.attrString);
			jtype[1] = new AttrType (AttrType.attrString);
			
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
			
			
			t = null;
			
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
		
		public void Query2() {}
		
		
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
			
			AttrType Stypes[] = {
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrString),
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrReal)
			};
			short []   Ssizes = new short[1];
			Ssizes[0] = 30;
			
			AttrType [] Rtypes = {
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrString),
			};
			short  []  Rsizes = new short[1];
			Rsizes[0] =15;
			
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
			try {
				sm =  new 
				SortMerge(Stypes,
						
						Ssizes,
						Rtypes,
						
						Rsizes,
						1,
						4,
						1,
						4,
						10,
						am,
						am2,
						false,
						false,
						ascending,
						outFilter,
						proj_list) ;
//				SortMerge(Stypes,
//				4,
//				Ssizes,
//				Rtypes,
//				3,
//				Rsizes,
//				1,
//				4,
//				1,
//				4,
//				10,
//				am,
//				am2,
//				false,
//				false,
//				ascending,
//				outFilter,
//				proj_list,
//				1) ;
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
			
			AttrType Stypes[] = {
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrString),
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrReal)
			};
			short []   Ssizes = new short[1];
			Ssizes[0] = 30;
			
			AttrType [] Rtypes = {
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrString),
			};
			short  []  Rsizes = new short[1];
			Rsizes[0] =15;
			
			FldSpec [] Sprojection = {
					new FldSpec(new RelSpec(RelSpec.outer), 1),
					new FldSpec(new RelSpec(RelSpec.outer), 2),
					new FldSpec(new RelSpec(RelSpec.outer), 3),
					new FldSpec(new RelSpec(RelSpec.outer), 4)
			};
			
			iterator.Iterator am = null;
			try {
				am  =  new FileScan("sailors.in", Stypes, Ssizes,
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
				am2 =  new FileScan("reserves.in", Rtypes, Rsizes, 
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
			
			AttrType Stypes[] = {
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrString),
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrReal)
			};
			short []   Ssizes = new short[1];
			Ssizes[0] = 30;
			
			AttrType [] Rtypes = {
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrInteger),
					new AttrType(AttrType.attrString),
			};
			short  []  Rsizes = new short[1];
			Rsizes[0] = 15;
			
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
				am2 =  new FileScan("reserves.in", Rtypes, Rsizes, 
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
						Rtypes,  Rsizes,
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
		
		public void Query6(){}
		
		
		private void Disclaimer() {
			System.out.print ("\n\nAny resemblance of persons in this database to"
					+ " people living or dead\nis purely coincidental. The contents of "
					+ "this database do not reflect\nthe views of the University,"
					+ " the Computer  Sciences Department or the\n"
					+ "developers...\n\n");
		}
	}
	public static void main(String argv[])
	{
		boolean sortstatus;

		SM_JoinTest smtest= new SM_JoinTest(); 
		
		JoinsDriver jjoin = smtest.new JoinsDriver();
		
		sortstatus = jjoin.runTests();
		if (sortstatus != true) {
			System.out.println("Error ocurred during join tests");
		}
		else {
			System.out.println("join tests completed successfully");
		}
	}
}

