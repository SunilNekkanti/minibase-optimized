JDKPATH = /s/java
LIBPATH = /p/course/cs784-raghu/minibase_share/javaminibase/construction
CLASSPATH = $(JDKPATH)/lib/classes.zip:.:..:$(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac -classpath $(CLASSPATH)
JAVA  = $(JDKPATH)/bin/java  -classpath $(CLASSPATH)

PROGS = 

all: $(PROGS)

# test buffer manager

BMTest:BMTest.java
	$(JAVAC) TestDriver.java BMTest.java

bmtest: BMTest
	$(JAVA) tests.BMTest

# test disk manager

DBTest:DBTest.java
	$(JAVAC) TestDriver.java DBTest.java

dbtest: DBTest
	$(JAVA) tests.DBTest

# test heapfile 

HFTest:HFTest.java
	$(JAVAC) TestDriver.java HFTest.java

hftest: HFTest
	$(JAVA) tests.HFTest

# test btree

BTTest:BTTest.java
	$(JAVAC) TestDriver.java BTTest.java

bttest: BTTest
	$(JAVA) tests.BTTest

# test index

IndexTest:IndexTest.java
	$(JAVAC) TestDriver.java IndexTest.java

indextest: IndexTest
	$(JAVA) tests.IndexTest

# test join

JoinTest:JoinTest.java
	$(JAVAC) TestDriver.java JoinTest.java

jointest: JoinTest
	$(JAVA) tests.JoinTest

# test sort

SortTest:SortTest.java
	$(JAVAC) TestDriver.java SortTest.java

sorttest: SortTest
	$(JAVA) tests.SortTest

# test sortMerge

SMJoin:SM_JoinTest.java
	$(JAVAC) SM_JoinTest.java TestDriver.java

sortmerge: SMJoin
	$(JAVA) tests.SM_JoinTest



xx:
	jar tf /p/course/cs784-raghu/minibase_share/javaminibase/construction/javaminibase.jar

clean:
	\rm -f *.class *~ \#* core TRACE
