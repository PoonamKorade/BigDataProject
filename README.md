# sort-map-reduce A MapReduce example which uses ArrayList and Collections.sort().
First create the 10g files. Then create teh jar from this project. The main class is already set in the pom.xml just do mvn clean install and mvn assembly:assembly
After that, download hadoop. Here is a nice tutorial I found - https://amodernstory.com/2014/09/23/installing-hadoop-on-mac-osx-yosemite/

once you have hadoop, try the jar in a small file, like with 50 lines. 
hdfs dfs -mkdir smallInputFile
hdfs dfs -put /nsonmez/smallOutput.txt smallInputFile

After this you are ready to run the job. This is the command 
hadoop jar absolutePathOfTheJar/mapreduce-sort-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar smallInputFile smallOutputFile

Note : smallOutputFile shouldn't be a file which is already in the directory. Your job wouldn't run if you'll do so, so just name a file which is not already created. 

Note 2 : If you'll get an error related to META-INF/LICENSE just run this command, probably the write permissions are creating problem.
zip -d absolutePathOfTheJar/mapreduce-sort-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar META-INF/LICENSE

Note 3 : If you'll receive any errors related to safemode run this command
hdfs dfsadmin -safemode leave

Once this job will be completed, you can work on the big file. 
If you'll run the job like this 
hadoop jar absolutePathOfTheJar/mapreduce-sort-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar inputFile outputFile
you'll find the output file here : hdfs dfs -cat outputFile/part-r-00000 | less
