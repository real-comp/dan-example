A walk-though and example code for reading and manipulating mvr-common JSON encoded data in Java.

Note that this example does _not_ use the real-comp/prime tool.


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



## Acquire sample data
The data used in this project is _not_ included with this project.  You will need to use some pre-built mvr-common
JSON data.

I will be using a file named _mvr_tx_20160916.json_



## Run Example1 on the sample data









