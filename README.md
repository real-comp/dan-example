A walk-though and example code for reading and manipulating mvr-common JSON encoded data in Java.

## Prerequisites

You will need to install the following tools:

* Java 8
* Maven 3
* Git

## Checkout
Use git to checkout the source for this project.

```bash
git clone git@github.com:real-comp/dan-example.git
cd dan-example
```

## Compile
Use Maven build tool to compile and package this project locally.  The first time you compile, Maven
will download all the dependencies locally.  Subsequent builds will be faster.

```bash
mvn clean install
```

If successful, the /target sub-directory will contain the ".jar" file for this project, and a "fat jar" that is
 the compiled code along with all the dependencies in a single .jar file.  This "fat jar" is a convenience that
 eliminates the need to download all the dependencies individually and construct a long java classpath statement to
 execute the examples.

The example code is documented heavily.  

## Acquire sample data
The data used in this project is _not_ included with this project.  You will need to use some pre-built mvr-common
JSON data.

I will be using a file named _mvr_tx_20160916.json_


## Example 1

Example 1 emits the VIN for every Vehicle that has one.
This example does _not_ use the Prime tool.  The Jackson JSON parsing library does the heavy lifting here.

```bash
$ head ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example1
1K9E24225D1005143
PS329F884623
CE530P120784
1GTGC24M9DS518469
F10AKM88437
JS1GU73A7D2107096
JM1BD2217D0718424
242076K107367
AR115410004409
1JCCN87E6DT049804

```
My input file has 812,111 JSON encoded MVRTransaction objects.

Lets time the processing of the entire file and count the number of VINS.
```bash
$ time cat ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example1 | wc -l 
801765

real	0m10.584s
user	0m11.452s
sys	0m1.280s
```
This is on my beefy dev box: 4Ghz Intel I7, SSD, 32 GB Ram


## Example 2

Example 2 uses Prime to write information from the MvrTransaction to a CSV file.

```bash
cat ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example2 | wc -l
949149

```
949,148 Owners plus a header record emitted as CSV.

timed:
```bash
time cat ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example2 | wc -l
949149

real	0m15.955s
user	0m17.512s
sys	0m2.872s

```
Much slower, but there is quite a bit going on.

You can see our name parsing work in the CSV fairly easy.  Remember, it will never be 100%.  Eye-balling the records I see we didn't parse "COMMAND ENTERPRIZES"
correctly.  Oh well.


## Example 3

Example 3 uses more of Prime features and does not deal with MvrTransaction objects, but operates on the JSON.
Note that the output of Example 3 will _not_ match Example 2.  I'm only emitting one Owner and there are other 
differences.

```bash
head ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example3
(omitted)

```
This is kinda neat.  That header record is ugly.  Oh well.

```bash
cat ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example3 | wc -l
812112

```
812111 Owners plus a header record emitted as CSV.

timed:
```bash
time cat ~/mvr_tx_20160916.json | java -cp target/dan-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.realcomp.dan.Example3 | wc -l
812112

real	0m14.517s
user	0m15.652s
sys	0m3.020s
```
