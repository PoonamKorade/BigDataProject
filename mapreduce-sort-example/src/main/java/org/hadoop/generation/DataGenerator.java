package org.hadoop.generation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataGenerator {
	public static void main(String[] args) throws Exception {
		try {
			generateData();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void generateData() throws IOException, InterruptedException {
		System.out.println("Started generation of 10GB file.");
		Properties prop = new Properties();
		InputStream input = null;
		input = DataGenerator.class.getClassLoader().getResourceAsStream("config.properties");
		
		// load a properties file
		prop.load(input);
		String outputPathDirectory = (String) prop.get("outputFileDirectory");
		String outputFileName = (String) prop.get("outputFileName");
		String finalMergedFile = outputPathDirectory + "/" + outputFileName + ".txt";
		new File(outputPathDirectory).mkdir();
		
		//start 10 executor threads which will create multiple files of 1GB.
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			executor.execute(new DataGeneratorThread(outputPathDirectory, outputFileName));
		}
		executor.shutdown();
		executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
		
		//check if merged file already exists in the path. if yes delete it.
		File mergedFile = new File(finalMergedFile);
		if(mergedFile.exists()) {
			mergedFile.delete();
		}
		
		//Get list of all files of 1GB
		File folder = new File(outputPathDirectory);
		File[] listOfFiles = folder.listFiles();
		
		//create a new merged file
		mergedFile.createNewFile();
		
		//merge individual files
		DataGenerator dataGen = new DataGenerator();
		if (mergedFile.exists() && listOfFiles.length != 0) {
			dataGen.mergeFiles(listOfFiles, mergedFile);
		} else {
			throw new RuntimeException("Merged file doesnt exist or files array is empty");
		}
		System.out.println("Completed generating 10GB file");
	}

	public void mergeFiles(File[] files, File mergedFile) {
		if (!mergedFile.exists() || files.length == 0) {
			throw new RuntimeException("Merged file doesnt exist or files array is empty");
		}
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(mergedFile, true);
			out = new BufferedWriter(fstream);
			for (File f : files) {
				if(!f.getName().contains(".txt")) {
					continue;
				}
				FileInputStream fis;
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));

				String aLine;
				while ((aLine = in.readLine()) != null) {
					aLine = aLine.trim(); // remove leading and trailing whitespace
				    if (!aLine.equals("")) // don't write out blank lines
				    {
				    	out.write(aLine);
						out.newLine();
				    }
				}
				in.close();
			}
			out.close();
			
			//delete all the individual files.
			for (File f : files) {
				f.delete();
			}
		} catch (IOException e) {
			throw new RuntimeException("Merged file doesnt exist or files array is empty");
		}
	}
}