package tests;

import global.AttrType;
import global.ExtendedSystemDefs;
import global.GlobalConst;
import global.SystemDefs;
import global.TupleOrder;
import heap.Tuple;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.RelSpec;
import iterator.Sort;

import java.util.Random;

import catalog.Utility;
import catalog.attrInfo;
import catalog.attrNode;


class SORTDriver extends TestDriver 
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
	private static short REC_LEN1 = 32; 
	private static int   SORTPGNUM = 12; 


	public SORTDriver() {
		super("sorttest");
	}

	public boolean runAllTests ()  {
		System.out.println ("\n" + "Running " + testName() + " tests...." + "\n");
		new ExtendedSystemDefs( dbpath, logpath,300, 500,NUMBUF, "Clock" );
		return super.runAllTests();
	}

	protected boolean test1()
	{
		System.out.println("------------------------ TEST 1 --------------------------");

		boolean status = true;

		try {
			Utility.loadRecordsUT("test1.in","indexTest1.db");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		AttrType [] attrType = null;
		short []   attrSize = null;
		try {
			attrType = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test1.in");
			attrSize = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test1.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// create an iterator by open a file scan
		FldSpec[] projlist = new FldSpec[1];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);

		FileScan fscan = null;

		try {
			fscan = new FileScan("test1.in", attrType, attrSize, projlist, null);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		TupleOrder order = new TupleOrder(TupleOrder.Ascending);
		
		// Sort "test1.in" 
		Sort sort = null;
		try {
			sort = new Sort(attrType, attrSize, fscan, 1, order, REC_LEN1, SORTPGNUM);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		int count = 0;
		Tuple t = null;
		String outval = null;

		try {
			t = sort.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		boolean flag = true;

		while (t != null) {
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
				System.err.println("outval = " + outval + "\tdata[count] = " + data[count]);

				System.err.println("Test1 -- OOPS! test1.out not sorted");
				status = false;
			}
			count++;

			try {
				t = sort.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (count < NUM_RECORDS) {
			System.err.println("Test1 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.out.println("Test1 -- Sorting OK");
		}

		// clean up
		try {
			sort.close();
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
		

		try {
			Utility.loadRecordsUT("test2.in","indexTest1.db");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		AttrType [] attrType = null;
		short []   attrSize = null;
		try {
			attrType = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test2.in");
			attrSize = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test2.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// create an iterator by open a file scan
		FldSpec[] projlist = new FldSpec[1];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);

		FileScan fscan = null;

		try {
			fscan = new FileScan("test2.in", attrType, attrSize,  projlist, null);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		TupleOrder order = new TupleOrder(TupleOrder.Descending);
		
		// Sort "test2.in"
		Sort sort = null;
		try {
			sort = new Sort(attrType, attrSize, fscan, 1, order, REC_LEN1, SORTPGNUM);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		int count = 0;
		Tuple t = null;
		String outval = null;

		try {
			t = sort.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		boolean flag = true;

		while (t != null) {
			if (count >= NUM_RECORDS) {
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

			if (outval.compareTo(data[NUM_RECORDS - count - 1]) != 0) {
				System.err.println("Test2 -- OOPS! test2.out not sorted");
				status = false;
			}
			count++;

			try {
				t = sort.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (count < NUM_RECORDS) {
			System.err.println("Test2 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.out.println("Test2 -- Sorting OK");
		}

		// clean up
		try {
			sort.close();
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

		Random random1 = new Random((long) 1000);
		Random random2 = new Random((long) 1000);

		int inum = 0;
		float fnum = 0;
		int count = 0;

		//TODO: MEjorar rendimiento
		for (int i=0; i<LARGE; i++) {
		//for (int i=0; i<5; i++) {
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
		
		
		TupleOrder[] order = new TupleOrder[2];
		order[0] = new TupleOrder(TupleOrder.Ascending);
		order[1] = new TupleOrder(TupleOrder.Descending);

		// create an iterator by open a file scan
		FldSpec[] projlist = new FldSpec[4];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);
		projlist[1] = new FldSpec(rel, 2);
		projlist[2] = new FldSpec(rel, 3);
		projlist[3] = new FldSpec(rel, 4);

		AttrType [] attrType = null;
		short []   attrSize = null;
		try {
			attrType = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test3.in");
			attrSize = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test3.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		FileScan fscan = null;

		// Sort "test3.in" on the int attribute (field 3) -- Ascending
		System.out.println(" -- Sorting in ascending order on the int field -- ");

		try {
			fscan = new FileScan("test3.in", attrType, attrSize,  projlist, null);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		Sort sort = null;
		try {
			sort = new Sort(attrType, attrSize, fscan, 3, order[0], 4, SORTPGNUM);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		count = 0;
		Tuple t = null;
		int iout = 0;
		int ival = 0;

		try {
			t = sort.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		if (t != null) {
			// get an initial value
			try {
				ival = t.getIntFld(3);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		} 

		boolean flag = true;

		while (t != null) {
			if (count >= LARGE) {
				System.err.println("Test3 -- OOPS! too many records");
				status = false;
				flag = false; 
				break;
			}

			try {
				iout = t.getIntFld(3);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			if (iout < ival) {
				System.err.println("count = " + count + " iout = " + iout + " ival = " + ival);

				System.err.println("Test3 -- OOPS! test3.out not sorted");
				status = false;
				break; 
			}
			count++;
			ival = iout;

			try {
				t = sort.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (count < LARGE) {
			System.err.println("Test3 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.err.println("Test3 -- Sorting of int field OK\n");
		}

		// clean up
		try {
			sort.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		// Sort "test3.in" on the int attribute (field 3) -- Ascending
		System.out.println(" -- Sorting in descending order on the float field -- ");

		try {
			fscan = new FileScan("test3.in", attrType, attrSize,  projlist, null);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		try {
			sort = new Sort(attrType, attrSize, fscan, 4, order[1], 4, SORTPGNUM);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		count = 0;
		t = null;
		float fout = 0;
		float fval = 0;

		try {
			t = sort.get_next();
			SystemDefs.JavabaseCatalog.getAttrCat().getTupleStructure("test3.in", t);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		if (t != null) {
			// get an initial value
			try {
				fval = t.getFloFld(4);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		} 

		flag = true;

		while (t != null) {
			if (count >= LARGE) {
				System.err.println("Test3 -- OOPS! too many records");
				status = false;
				flag = false; 
				break;
			}

			try {
				fout = t.getFloFld(4);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			if (fout > fval) {
				System.err.println("count = " + count + " fout = " + fout + " fval = " + fval);

				System.err.println("Test3 -- OOPS! test3.out not sorted");
				status = false;
				break; 
			}
			count++;
			fval = fout;

			try {
				t = sort.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (count < LARGE) {
			System.err.println("Test3 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.out.println("Test3 -- Sorting of float field OK\n");
		}

		// clean up
		try {
			sort.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		System.out.println("------------------- TEST 3 completed ---------------------\n");

		return status;
	}

	protected boolean test4()
	{
		System.out.println("------------------------ TEST 4 --------------------------");

		boolean status = true;

		try {
			Utility.loadRecordsUT("test4-1.in","indexTest1.db");
			Utility.loadRecordsUT("test4-2.in","indexTest1.db");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		AttrType [] attrType = null;
		short []   attrSize = null;
		try {
			attrType = SystemDefs.JavabaseCatalog.getAttrCat().getAttrType("test4-1.in");
			attrSize = SystemDefs.JavabaseCatalog.getAttrCat().getStringsSizeType("test4-2.in");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TupleOrder[] order = new TupleOrder[2];
		order[0] = new TupleOrder(TupleOrder.Ascending);
		order[1] = new TupleOrder(TupleOrder.Descending);

		// create an iterator by open a file scan
		FldSpec[] projlist = new FldSpec[1];
		RelSpec rel = new RelSpec(RelSpec.outer); 
		projlist[0] = new FldSpec(rel, 1);

		FileScan fscan1 = null;
		FileScan fscan2 = null;

		try {
			fscan1 = new FileScan("test4-1.in", attrType, attrSize,  projlist, null);
			fscan2 = new FileScan("test4-2.in", attrType, attrSize,  projlist, null);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		// Sort input files
		Sort sort1 = null;
		Sort sort2 = null;
		try {
			sort1 = new Sort(attrType, attrSize, fscan1, 1, order[0], REC_LEN1, SORTPGNUM);
			sort2 = new Sort(attrType, attrSize, fscan2, 1, order[1], REC_LEN1, SORTPGNUM);
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}


		int count = 0;
		Tuple t1 = null;
		Tuple t2 = null; 
		String outval = null;

		try {
			t1 = sort1.get_next();
			t2 = sort2.get_next();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace(); 
		}

		boolean flag = true;

		while (t1 != null) {
			if (count >= NUM_RECORDS) {
				System.err.println("Test4 -- OOPS! too many records");
				status = false;
				flag = false; 
				break;
			}

			try {
				outval = t1.getStrFld(1);
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			if (outval.compareTo(data[count]) != 0) {
				System.err.println("outval = " + outval + "\tdata2[count] = " + data[count]);

				System.err.println("Test4 -- OOPS! test4.out not sorted");
				status = false;
			}
			count++;

			if (t2 == null) {
				System.err.println("Test4 -- t2 is null prematurely");
				status = false;
			}
			else {
				try {
					outval = t2.getStrFld(1);
				}
				catch (Exception e) {
					status = false;
					e.printStackTrace();
				}

				if (outval.compareTo(data[NUM_RECORDS - count]) != 0) {
					System.err.println("outval = " + outval + "\tdata2[count] = " + data[NUM_RECORDS - count]);

					System.err.println("Test4 -- OOPS! test4.out not sorted");
					status = false;
				}
			}

			try {
				t1 = sort1.get_next();
				t2 = sort2.get_next();
			}
			catch (Exception e) {
				status = false;
				e.printStackTrace();
			}
		}
		if (count < NUM_RECORDS) {
			System.err.println("count = " + count);

			System.err.println("Test4 -- OOPS! too few records");
			status = false;
		}
		else if (flag && status) {
			System.out.println("Test4 -- Sorting OK");
		}

		// clean up
		try {
			sort1.close();
			sort2.close();
		}
		catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		System.out.println("------------------- TEST 4 completed ---------------------\n");

		return status;
	}

	protected boolean test5()
	{
		return true;
	}

	protected boolean test6()
	{
		return true;
	}

	protected String testName()
	{
		return "Sort";
	}
}

public class SortTest
{
	public static void main(String argv[])
	{
		boolean sortstatus;

		SORTDriver sortt = new SORTDriver();

		sortstatus = sortt.runAllTests();
		if (sortstatus != true) {
			System.out.println("Error ocurred during sorting tests");
		}
		else {
			System.out.println("Sorting tests completed successfully");
		}
	}
}

