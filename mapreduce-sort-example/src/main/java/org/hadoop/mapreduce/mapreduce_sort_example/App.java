package org.hadoop.mapreduce.mapreduce_sort_example;

import java.util.*;

import org.apache.hadoop.io.Text;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

//		List<Text> deneme = new ArrayList<Text>();
		Set<Text> deneme = new TreeSet<Text>();
		Text bir = new Text("aliAta1");
		Text iki = new Text("bliAta1");
		Text uc = new Text("alzAta1");
		Text dort = new Text("alaAta1");
		deneme.add(bir);
		deneme.add(iki);
		deneme.add(uc);
		deneme.add(dort);

//		Collections.sort(deneme);

		for (Text text : deneme) {
			System.out.println(text);
		}
	}
}
