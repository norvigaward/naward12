warcexamples
============

Example programs that work with the March 2014 Common Crawl warc, wet and wat files on the SURFsara hadoop cluster environment.

We have prepared some example code to show how to work with each of the files in the Common Crawl data. This should give you a good starting point for your own hacking. The code can be found on our github: [warcexamples source](https://github.com/norvigaward/warcexamples). Precompiled binaries are available from our maven repository: [http://beehub.nl/surfsara-repo/releases/](http://beehub.nl/surfsara-repo/releases/). To clone the source:

    git clone https://github.com/norvigaward/warcexamples

The warcexamples project contains both Ant/Ivy and Maven build files should by easy to use with your favourite editor/ide. If you have any questions on building or compiling the source please let us know by sending an email to: [hadoop.support@surfsara.nl](mailto:hadoop.support@surfsara.nl?subject=Norvig Award: examples).

#### Running the mapreduce examples

In order to run the examples you should have the Hadoop client software prepared and a valid Kerberos ticket (for details see the documentation [here](https://surfsara.nl/systems/hadoop/hathi) and [here](https://github.com/sara-nl/hathi-client)).
            Once you have done this you can run the examples with the yarn jar command:

    yarn jar warcexamples.jar

Or depending on how you built the examples:

    yarn jar warcexamples-1.1-fatjar.jar

Running the above command should show you a list of the current example programs:

*   NER: mapreduce example that performs Named Entity Recognition on text in wet files. See the [`nl.surfsara.warcexamples.hadoop.wet`](https://github.com/norvigaward/warcexamples/tree/master/src/nl/surfsara/warcexamples/hadoop/wet) package for relevant code.
Usage:

        yarn jar warcexamples.jar ner hdfs_input_path hdfs_output_path

*   servertype: extracts the servertype information from the wat files. See the [`nl.surfsara.warcexamples.hadoop.wat`](https://github.com/norvigaward/warcexamples/tree/master/src/nl/surfsara/warcexamples/hadoop/wat) package for relevant code. Usage:

        yarn jar warcexamples.jar servertype hdfs_input_path hdfs_output_path

*   href: parses the html in warc files and outputs the url of the crawled page and the links (href attribute) from the parsed document. See the [`nl.surfsara.warcexamples.hadoop.warc`](https://github.com/norvigaward/warcexamples/tree/master/src/nl/surfsara/warcexamples/hadoop/warc) package for relevant code. Usage:

        yarn jar warcexamples.jar href hdfs_input_path hdfs_output_path

    Note that the input path should consist of sequence files.

*   headers: dumps the headers from a wat, warc or wet file (gzipped ones). This is not a mapreduce example but files are read from HDFS. This can be run from your local computer or the VM. See the [`nl.surfsara.warcexamples.hadoop.hdfs`](https://github.com/norvigaward/warcexamples/tree/master/src/nl/surfsara/warcexamples/hdfs) package for relevant code. Usage:

        yarn jar warcexamples.jar headers hdfs_input_file


If you want to specify hadoop specific options to the programs you can add these to the command line. For example the following command runs the href example with 10 reducers and max 4GB memory for the JVM:

        yarn jar warcexamples.jar href -D mapreduce.job.reduces=10 \
                -D mapred.child.java.opts=-Xmx4G hdfs_input_path hdfs_output_path

#### Explore the code

Most of the examples follow the same structure: an implementation of the `org.apache.hadoop.util.Tool` interface coupled to a custom mapper with identity or stock reducer. The dependencies
          are handled by Ivy/Maven. All the mapreduce examples make use of our own [warcutils](https://github.com/norvigaward/warcutils) package for reading data from HDFS (maven repository [here](http://beehub.nl/surfsara-repo/releases/SURFsara/warcutils)).

#### Running some pig examples

Apache Pig allows you to write data-analysis programs in a data-flow language called Pig Latin. The pig compiler will convert your Pig Latin program to a series of MapReduce jobs. Using Pig allows you to think about your problem in a series of relations instead of MapReduce. We provide Pig Loaders for the warc and seq files as part of the warcutils package. The Pig Loaders do not expose all the information that is available in the warc files, but your are free to extend them to extract the information relevant for your project.

The [warcexamples repository](https://github.com/norvigaward/warcexamples) contains two Pig examples scripts that demonstrate how you can process the web crawl using Pig. One of them looks at the Content-Type and Content-Length of the records and calculates the average length per type. Running a pig job is very simple:

    $ cd ~/warcexamples/pig
    $ pig sizepertype.pig

With Pig it is also possible to run the job on the local machine by adding `-x local` to the command. Running pig locally on a single segment is a very fast way to explore the data and develop your algorithm. You do need to make sure that you have the data and dependencies (jars) available on your local machine.
