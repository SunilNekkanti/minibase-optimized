#This Makefile is used to create assignments.

#the following macros should be updated according to
#the assignment to be generated

ASSIGNMENT=SM_Join
PACKAGE=iterator
JAR=sortmergeAssign.jar
JARFILES=bufmgr/*.class diskmgr/*.class global/*.class \
         heap/*.class chainexception/*.class  btree/*.class index/*.class
DOCFILES=bufmgr diskmgr global chainexception heap btree iterator


##############  update the above for each assignment in making

HANDOUT=/p/course/cs784-raghu/minibase_share/javaminibase/javamini_hwk/handout
ASSIGN=/p/course/cs784-raghu/minibase_share/javaminibase/javamini_hwk/assign

LIBDIR=$(ASSIGN)/$(ASSIGNMENT)/lib
KEY=$(ASSIGN)/$(ASSIGNMENT)/key
SRC=$(ASSIGN)/$(ASSIGNMENT)/src

IMAGES=/s/jdk1.1.6/unix/docs/api/images
IMAGELINK=$(PACKAGEINDEX)/images
PACKAGEINDEX=$(HANDOUT)/$(ASSIGNMENT)/javadoc

JAVADOC=javadoc -public -d $(PACKAGEINDEX)

### Generate jar and javadoc files.  Apply to most assignments.

# create lib (jar file) for the assignment
jar:
	jar cf $(JAR) $(JARFILES)
	mv $(JAR) $(LIBDIR)

# create javadoc for the assignment

doc:
	$(JAVADOC) $(DOCFILES)
	\rm -f $(IMAGELINK)
	ln -s $(IMAGES) $(IMAGELINK)


########## Buffer Manager Assignment Installation

BM=jar doc bmkey bmsrc

assignbm:$(BM)

# create key for bufmgr assignment
bmkey:
	cp $(PACKAGE)/*.java $(KEY)/$(PACKAGE)
	cp diskmgr/*.java $(KEY)/diskmgr
	cp tests/BMTest.java tests/TestDriver.java $(KEY)/tests

# create src for bufmgr assignment
bmsrc:
	cp diskmgr/*.java $(SRC)/diskmgr
	cp tests/BMTest.java tests/TestDriver.java $(SRC)/tests


########## Heapfile Assignment Installation

## note the src has to be created separately.  go to heap directory
## and do "make clean" and "make hp".  Then come back to the current 
## directory, do "make hfsrc".  Only the necessary *.class file will
## be given to the students.

HF=jar doc hfkey 

assignhf:$(HF)

# create key for bufmgr assignment
hfkey:
	cp $(PACKAGE)/*.java $(KEY)/$(PACKAGE)
	cp tests/HFTest.java tests/TestDriver.java $(KEY)/tests

# create src for bufmgr assignment
hfsrc:
	cp $(PACKAGE)/*.class \
     $(SRC)/$(PACKAGE)
	cp tests/HFTest.java tests/TestDriver.java $(SRC)/tests


########## BTree Assignment Installation
#
#Note that javadoc for btree has to be created SEPARATELY,
#because we don't want to give away too much secrete.  Only
#the classes that need to be show up on javadoc should be
#contained in the btree directory for which the javadoc is
#is made.

#Note that the src directory also needs to be prepared with
#great care.  The students should only be given the necessary
# *.class files.

BT=jar btkey 

assignbt:$(BT)

# create key for btree assignment
btkey:
	cp $(PACKAGE)/*.java $(KEY)/$(PACKAGE)
	cp btree/*.java $(KEY)/btree
	cp tests/BTTest.java tests/TestDriver.java $(KEY)/tests

# create src for btree assignment
btsrc:
	cp btree/*.class btree/IndexFile.java btree/IndexFileScan.java $(SRC)/btree
	cp tests/BTTest.java tests/TestDriver.java $(SRC)/tests

########## SortMerge Join Assignment Installation

SM=jar doc smkey smsrc 

assignsm:$(SM)

# create key for btree assignment
smkey:
	cp $(PACKAGE)/*.java $(KEY)/$(PACKAGE)
	cp iterator/*.java $(KEY)/iterator
	cp tests/SM_JoinTest.java tests/QueryCheck.java \
     tests/TestDriver.java $(KEY)/tests

# create src for btree assignment
smsrc:
	cp iterator/*.class $(SRC)/iterator
	cp tests/SM_JoinTest.java tests/TestDriver.java $(SRC)/tests
