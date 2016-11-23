#!/bin/sh

hdfs dfs -rm -r /smallInputFile
hdfs dfs -rm -r /smallOutputFile 
hdfs dfs -mkdir /smallInputFile 
hdfs dfs -put ~/Desktop/output/output.txt /smallInputFile
hadoop jar ~/Downloads/sort-map-reduce-master/mapreduce-sort-example/target/mapreduce-sort-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar /smallInputFile /smallOutputFile