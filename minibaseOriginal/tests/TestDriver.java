package tests;
import java.io.File;

import chainexception.ChainException;

//Major Changes:
//1. Change the return type of test() functions from 'int' to 'boolean'
//to avoid defining static int TRUE/FALSE, which makes it easier for
//derived functions to return the right type.
//2. Function runTest is not implemented to avoid dealing with function
//pointers.  Instead, it's flattened in runAllTests() function.
//3. Change
//Status TestDriver::runTests()
//Status TestDriver::runAllTests()
//to
//public boolean runTests();
//protected boolean runAllTests();

/** 
 * TestDriver class is a base class for various test driver
 * objects.
 * <br>
 * Note that the code written so far is very machine dependent.  It assumes
 * the users are on UNIX system.  For example, in function runTests, a UNIX
 * command is called to clean up the working directories.
 * 
 */

public class TestDriver {

	public final static boolean OK   = true; 
	public final static boolean FAIL = false; 

	protected String dbpath;  
	protected String logpath;


	/** 
	 * TestDriver Constructor 
	 *
	 * @param nameRoot The name of the test being run
	 */

	protected TestDriver (String nameRoot) {

		//  sprintf( dbpath, MINIBASE_DB, nameRoot, getpid() );
		//  sprintf( logpath, MINIBASE_LOG, nameRoot, getpid() );

		//NOTE: Assign random numbers to the dbpath doesn't work because
		//we can never open the same database again if everytime we are
		//given a different number.  
		
		if (System.getProperty("os.name").contains("Windows")){
			//	Para windows
			logpath = "c:\\windows\\temp\\" + nameRoot + ".minibase-log";
			dbpath = "c:\\windows\\temp\\" + nameRoot +".minibase-db";
		} else {
			//Para unix
			logpath = "/tmp/" + nameRoot + ".minibase-log";
			dbpath = "/tmp/" + nameRoot +".minibase-db";
		}	
		}

	/**
	 * Another Constructor
	 */

	protected TestDriver () {}

	/** 
	 * @return whether the test has completely successfully 
	 */
	protected boolean test1 () { return true; }

	/** 
	 * @return whether the test has completely successfully 
	 */
	protected boolean test2 () { return true; }

	/** 
	 * @return whether the test has completely successfully 
	 */
	protected boolean test3 () { return true; }

	/** 
	 * @return whether the test has completely successfully 
	 */
	protected boolean test4 () { return true; }

	/** 
	 * @return whether the test has completely successfully 
	 */
	protected boolean test5 () { return true; }

	/** 
	 * @return whether the test has completely successfully 
	 */
	protected boolean test6 () { return true; }

	/** 
	 * @return <code>String</code> object which contains the name of the test
	 */
	protected String testName() { 

		//A little reminder to subclassers 
		return "*** unknown ***"; 

	}

	/**
	 * This function does the preparation/cleaning work for the
	 * running tests.
	 *
	 * @return a boolean value indicates whether ALL the tests have passed
	 */
	public boolean runTests ()  {

	    System.out.println ("\n" + "Running " + testName() + " tests...." + "\n");
	    
	    // Kill anything that might be hanging around
	    
		File file = new File(logpath);
		file.delete();
		file = new File(dbpath);
		file.delete();
		
	    //Run the tests. Return type different from C++
	    boolean _pass = runAllTests();

	    file = new File(logpath);
		file.delete();
		file = new File(dbpath);
		file.delete();
	    
	    System.out.println ("\n" + "..." + testName() + " tests ");
	    System.out.print (_pass ? "completely successfully" : "failed");
	    System.out.println (".\n\n");
	    
	    return _pass;
	}

	protected boolean runAllTests() {

		boolean _passAll = OK;

		//The following code checks whether appropriate erros have been logged,
		//which, if implemented, should be done for each test case.  

		//minibase_errors.clear_errors();
		//int result = test();
		//if ( !result || minibase_errors.error() ) {
		//  status = FAIL;
		//  if ( minibase_errors.error() )
		//    cerr << (result? "*** Unexpected error(s) logged, test failed:\n"
		//    : "Errors logged:\n");
		//    minibase_errors.show_errors(cerr);
		//}

		//The following runs all the test functions without checking
		//the logged error types. 

		//Running test1() to test6()
		if (!test1()) { _passAll = FAIL; }
		if (!test2()) { _passAll = FAIL; }
		if (!test3()) { _passAll = FAIL; }
		if (!test4()) { _passAll = FAIL; }
		if (!test5()) { _passAll = FAIL; }
		if (!test6()) { _passAll = FAIL; }

		return _passAll;
	}

	/**
	 * Used to verify whether the exception thrown from
	 * the bottom layer is the one expected.
	 */
	public boolean checkException (ChainException e, 
			String expectedException) {

		boolean notCaught = true;
		while (true) {

			String exception = e.getClass().getName();

			if (exception.equals(expectedException)) {
				return (!notCaught);
			}

			if ( e.prev==null ) {
				return notCaught;
			}
			e = (ChainException)e.prev;
		}

	} // end of checkException

} // end of TestDriver  
