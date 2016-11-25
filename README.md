# Steps to be followed in order to generate the 10G data file:
1. Open config.properties file under directory src/main/resources

2. Edit the path for outputdirectory(/Users/poonammhaskar/Desktop) with your local directory path where you want to create    the 10GB data file.

3. Run the DataGenerator.java class under the package org.hadoop.generation

4. It will take around 1.5 - 3.0 minutes to create final output.txt file with 10GB of random data. Depending on the processor this execution time will vary. In my machine it is taking 1.5 minutes.

# sort-map-reduce A MapReduce example which uses ArrayList and Collections.sort()
1. Once you create 10G file; then create the jar from this project. The main class is already set in the pom.xml just do 
mvn clean install
mvn assembly:assembly

2. After that, download hadoop. Here is a nice tutorial I found - 
https://amodernstory.com/2014/09/23/installing-hadoop-on-mac-osx-yosemite/

3. once you have hadoop run the below commands to create InputFile folder 
hdfs dfs -mkdir inputFile
hdfs dfs -put ~/Desktop/output/output.txt inputFile

4. After this you are ready to run the job. This is the command 
hadoop jar ~/Downloads/sort-map-reduce-master/mapreduce-sort-example/target/mapreduce-sort-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar /inputFile /outputFile

Note 1 : smallOutputFile shouldn't be a file which is already in the directory. Your job wouldn't run if you'll do so, so just name a file which is not already created. 

Note 2 : If you'll get an error related to META-INF/LICENSE just run this command, probably the write permissions are creating problem then run below command
zip -d ~/Downloads/sort-map-reduce-master/mapreduce-sort-example/target/mapreduce-sort-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar META-INF/LICENSE

Note 3 : If you'll receive any errors related to safemode run this command
hdfs dfsadmin -safemode leave

5. You'll find the output file here:
hdfs dfs -cat outputFile/part-r-00000 | less

# Data validation steps
