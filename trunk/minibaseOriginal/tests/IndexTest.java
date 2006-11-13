package tests;

import global.AttrOperator;
import global.AttrType;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.IndexType;
import global.SystemDefs;
import heap.Tuple;
import index.IndexScan;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.RelSpec;

import java.util.Random;

import catalog.IndexDesc;
import catalog.Utility;
import catalog.attrInfo;
import catalog.attrNode;


class IndexDriver extends TestDriver 
implements GlobalConst {

	private static String   data[] = {
		"andyw", "awny", "azat", "barthel", "binh", "bloch", "bradw", 
		"chingju", "chui", "chung-pi", "cychan", "dai", "daode", "dhanoa", 
		"dissoswa", "djensen", "dsilva", "dwiyono", "edwards", "evgueni", 
		"feldmann", "flechtne", "frankief", "ginther", "gray", "guangshu", 
		"gunawan", "hai", "handi", "harimin", "haris", "he", "heitzman", 
		"honghu", "huxtable", "ireland", "jhowe", "joon", "josephin", "joyce",
		"jsong", "juei-wen", "karsono", "keeler", "ketola", "kinc", "kurniawa",
		"leela", "lukas", "mak", "marc", "markert", "meltz", "meyers", 
		"mirwais", "muerle", "muthiah", "neuman", "newell", "peter", "raghu", 
		"randal", "rathgebe", "robert", "savoy", "schiesl", "schleis", 
		"scottc", "seo", "shi", "shun-kit", "siddiqui", "soma", "sonthi", 
		"sungk", "susanc", "tak", "thiodore", "ulloa", "vharvey", "waic",
		"wan", "wawrzon", "wenchao", "wlau", "xbao", "xiaoming", "xin", 
		"yi-chun", "yiching", "yuc", "yung", "yuvadee", "zmudzin" };

	private static int   NUM_RECORDS = data.length; 
	private static int   LARGE = 1000; 
	
	public IndexDriver() {
		super("indextest");
	}

	protected boolean runAllTests() {
		new ExtendedSystemDefs( dbpath, logpath,300, 500,NUMBUF, "Clock" );
		return super.runAllTests();
	}

	protected boolean test1()
	{
		System.out.println("------------------------ TEST 1 --------------------------");

		boolean status = true;

		try {
			Utility.loadRecordsUT("test1.in","indexTest1.db");
			SystemDefs.JavabaseCatalog.addIndex("test1.in", "data", new IndexType (IndexType.B_Index), 1);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println("BTreeIndex created successfully.\n"); 
		System.out.println("BTreeIndex file created successfully.\n"); 

		IndexDesc index = null;
		try {
			index = SystemDefs.JavabaseCatalog.getIndexInfo("test1.in", "data", new IndexType (IndexType.B_Index));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		AttrType [] Stypes = null;
		short []   Ssizes = null;
		try {
			Stypes = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test1.in");
			Ssizes = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test1.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FldSpec[] projlist = new FldSpec[1];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);

		// start index scan
		IndexScan iscan = null;
		try {
			iscan = new IndexScan ( index.getIndexType(), index.getRelationName(),index.getIndexName(), Stypes, Ssizes,	projlist, null, 1, true);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		int count = 0;
		Tuple t = null;
		String outval = null;

		boolean flag = true;

		try {
			while ((t = iscan.get_next()) != null) {
				if (count >= NUM_RECORDS) {
					System.err.println("Test1 -- OOPS! too many records");
					status = false;
					flag = false; 
					break;
				}

				try {
					outval = t.getStrFld(1);
				}
				catch (Exception e) {
					status = false;
					e.printStackTrace();
				}

				if (outval.compareTo(data[count]) != 0) {
					System.err.println("outval = " + outval + "\tdata2[count] = " + data[count]);

					System.err.println("Test1 -- OOPS! index scan not in sorted order");
					status = false;
				}
				count++;			
			}
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		if (count < NUM_RECORDS) {
			System.err.println("Test1 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.out.println("Test1 -- Index Scan OK");
		}

		// clean up
		try {
			iscan.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		System.out.println("------------------- TEST 1 completed ---------------------\n");

		return status;
	}


	protected boolean test2()
	{
		System.out.println("------------------------ TEST 2 --------------------------");

		boolean status = true;
		
		IndexDesc index = null;
		try {
			index = SystemDefs.JavabaseCatalog.getIndexInfo("test1.in", "data", new IndexType (IndexType.B_Index));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("BTreeIndex opened successfully.\n"); 
		
		FldSpec[] projlist = new FldSpec[1];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);

		// set up an identity selection
		CondExpr[] expr = new CondExpr[2];
		expr[0] = new CondExpr();
		expr[0].op = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrString);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
		expr[0].operand2.string = "dsilva";
		expr[0].next = null;
		expr[1] = null;
		
		AttrType [] attrType = null;
		short []   attrSize = null;
		try {
			attrType = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test1.in");
			attrSize = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test1.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// start index scan
		IndexScan iscan = null;
		try {
			iscan = new IndexScan ( index.getIndexType(), index.getRelationName(),index.getIndexName(), attrType, attrSize,	projlist, expr, 1, false);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		
		Tuple t = null;
		String outval = null;

		try {
			t = iscan.get_next();
			
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		if (t == null) {
			System.err.println("Test 2 -- no record retrieved from identity search.");
			status = false;
			return status; 
		}

		try {
			outval = t.getStrFld(1);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		if (outval.compareTo("dsilva") != 0) {
			System.err.println("Test2 -- error in identity search.");
			status = false;
		}

		try {
			t = iscan.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		if (t != null) {
			System.err.println("Test2 -- OOPS! too many records");
			status = false;
		}

		// clean up
		try {
			iscan.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		
		// now try a range scan
		expr = new CondExpr[3]; 
		expr[0] = new CondExpr();
		expr[0].op = new AttrOperator(AttrOperator.aopGE);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrString);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
		expr[0].operand2.string = "dsilva";
		expr[0].next = null;
		expr[1] = new CondExpr();
		expr[1].op = new AttrOperator(AttrOperator.aopLE);
		expr[1].type1 = new AttrType(AttrType.attrSymbol);
		expr[1].type2 = new AttrType(AttrType.attrString);
		expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
		expr[1].operand2.string = "yuc";
		expr[1].next = null;
		expr[2] = null;

		// start index scan
		iscan = null;
		try {
			iscan = new IndexScan ( index.getIndexType(), index.getRelationName(),index.getIndexName(), attrType, attrSize,	projlist, expr, 1, false);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		
		int count = 16; // because starting from dsilva
		t = null;

		try {
			t = iscan.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		boolean flag = true;

		while (t != null) {
			if (count >= (NUM_RECORDS - 3)) {
				System.err.println("Test2 -- OOPS! too many records");
				status = false;
				flag = false; 
				break;
			}

			try {
				outval = t.getStrFld(1);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			if (outval.compareTo(data[count]) != 0) {
				System.err.println("outval = " + outval + "\tdata2[count] = " + data[count]);

				System.err.println("Test2 -- OOPS! index scan not in sorted order");
				status = false;
			}
			count++;

			try {
				t = iscan.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (count < (NUM_RECORDS-3)) {
			System.err.println("Test2 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.out.println("Test2 -- Index Scan OK");
		}

		// clean up
		try {
			iscan.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		System.out.println("------------------- TEST 2 completed ---------------------\n");
		
		return status;
	}

	protected boolean test3()
	{ 
		System.out.println("------------------------ TEST 3 --------------------------");

		boolean status = true;
				
		attrInfo[] attrList = new attrInfo[4];
		
		attrList[0]= new attrInfo("1", new AttrType(AttrType.attrString), 32, false);
		attrList[1]= new attrInfo("2", new AttrType(AttrType.attrString), 32, false);
		attrList[2]= new attrInfo("3", new AttrType(AttrType.attrInteger), 0, false);
		attrList[3]= new attrInfo("4", new AttrType(AttrType.attrReal), 0, false);		
		
		try {
			SystemDefs.JavabaseCatalog.createRel( "test3.in", attrList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		int inum = 0;
		float fnum = 0;
		int count = 0;

		Random random1 = new Random();
		Random random2 = new Random();
		//TODO: MEjorar rendimiento
		//for (int i=0; i<LARGE; i++) {
		for (int i=0; i<5; i++) {
			attrNode[] node = new attrNode[4];

			// setting fields
			inum = random1.nextInt();
			fnum = random2.nextFloat();

			node[0] = new attrNode("1",data[i%NUM_RECORDS]);
			node[1] = new attrNode("2","");
			node[2] = new attrNode("3",Integer.toString(inum%1000));
			node[3] = new attrNode("4",Float.toString(fnum));

			try {
				Utility.insertRecordUT("test3.in", node);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		
		try {
			SystemDefs.JavabaseCatalog.addIndex("test3.in", "3", new IndexType (IndexType.B_Index), 1);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		System.out.println("BTreeIndex created successfully.\n"); 
		
		System.out.println("BTreeIndex file created successfully.\n"); 

		FldSpec[] projlist = new FldSpec[4];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);
		projlist[1] = new FldSpec(rel, 2);
		projlist[2] = new FldSpec(rel, 3);
		projlist[3] = new FldSpec(rel, 4);

		// conditions
		CondExpr[] expr = new CondExpr[3]; 
		expr[0] = new CondExpr();
		expr[0].op = new AttrOperator(AttrOperator.aopGE);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrInteger);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 3);
		expr[0].operand2.integer = 100;
		expr[0].next = null;
		expr[1] = new CondExpr();
		expr[1].op = new AttrOperator(AttrOperator.aopLE);
		expr[1].type1 = new AttrType(AttrType.attrSymbol);
		expr[1].type2 = new AttrType(AttrType.attrInteger);
		expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 3);
		expr[1].operand2.integer = 900;
		expr[1].next = null;
		expr[2] = null;

		AttrType [] attrType = null;
		short []   attrSize = null;
		try {
			attrType = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test3.in");
			attrSize = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test3.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		IndexDesc index = null;
		try {
			index = SystemDefs.JavabaseCatalog.getIndexInfo("test3.in", "3", new IndexType (IndexType.B_Index));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// start index scan
		IndexScan iscan = null;
		try {
			iscan = new IndexScan ( index.getIndexType(), index.getRelationName(),index.getIndexName(), attrType, attrSize,	projlist, expr, 1, false);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		Tuple t = null;
		int iout = 0;
		int ival = 100; // low key

		try {
			t = iscan.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		while (t != null) {
			try {
				iout = t.getIntFld(3);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			if (iout < ival) {
				System.err.println("count = " + count + " iout = " + iout + " ival = " + ival);

				System.err.println("Test3 -- OOPS! index scan not in sorted order");
				status = false;
				break; 
			}
			else if (iout > 900) {
				System.err.println("Test 3 -- OOPS! index scan passed high key");
				status = false;
				break;
			}

			ival = iout;

			try {
				t = iscan.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (status) {
			System.out.println("Test3 -- Index scan on int key OK\n");
		}

		// clean up
		try {
			iscan.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		System.out.println("------------------- TEST 3 completed ---------------------\n");

		return status;
	}
	
	protected String testName()
	{
		return "Index";
	}
}

public class IndexTest
{
	public static void main(String argv[])
	{
		boolean indexstatus;

		IndexDriver indext = new IndexDriver();

		indexstatus = indext.runTests();
		if (indexstatus != true) {
			System.out.println("Error ocurred during index tests");
		}
		else {
			System.out.println("Index tests completed successfully");
		}
	}
}

