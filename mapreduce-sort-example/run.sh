#!/bin/sh

start=`date +%s`

if [ -z "$1" ]
  then
    echo "Please enter a number indicating the output file size as an argument."
    exit 1
fi

# mvn clean and assembly
echo "################ Started Building project #######################"
mvn clean install -DskipTests
mvn assembly:assembly -DskipTests
zip -d target/sort.jar META-INF/LICENSE
zip -d target/validate.jar META-INF/LICENSE
zip -d target/generate.jar META-INF/LICENSE
echo "################ Completed Building project #######################"
echo ""

# generate data
echo "################ Started generation of $1 GB file #######################"
java -classpath target/generate.jar org.hadoop.generation.DataGenerator $1
echo "################ Completed generation of $1 GB file #######################"
echo ""

echo "################ Started cleanup of previous files #######################"
# clean up previos files
hdfs dfs -rm -r /inputFolder
hdfs dfs -rm -r /outputFolder
hdfs dfs -rm -r /validatedFolder
hdfs dfs -mkdir /inputFolder
hdfs dfs -put ./output/outputFile.txt /inputFolder
rm ./output/outputFile.txt
echo "################ Completed cleanup of previous files #######################"
echo ""

#sort data
echo "################ Started sorting input file #######################"
hadoop jar target/sort.jar /inputFolder /outputFolder
hdfs dfs -rm -r /inputFolder
echo "################ Completed sorting input file #######################"
echo ""

# validate data 
echo "################ Started validation output of sort step #######################"
hadoop jar target/validate.jar /outputFolder/part-r-00000 /validatedFolder
echo "################ Completed validation output of sort step #######################"
echo ""

echo "################ OUTPUT ####################################################################"
file=`hdfs dfs -test -f /validated.out/part-00000`
if [ $? == 0 ]; then
    filesize=`hdfs dfs -du -h /validated.out | grep /validated.out/part-00000 | cut -c1`
	if [ $filesize -eq 0 ]
	then
	  echo "Data Validated status : SUCCESS"
	else
	  echo "Data Validated status : FAILED. Please check file /validatedFolder/part-00000 for errors."
	fi
else
    echo "Data Validated status : FAILED. Please check console for errors."
fi


end=`date +%s`
diff=`expr $end - $start`
echo "Time taken for generation, sorting and validation : "$diff" seconds"
echo "###########################################################################################";