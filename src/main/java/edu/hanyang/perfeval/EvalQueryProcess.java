package edu.hanyang.perfeval;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import edu.hanyang.indexer.QueryProcess;
import edu.hanyang.utils.ExecuteQuery;

public class EvalQueryProcess {

	public static void main(String[] args) throws Exception {
		String filepath = "../all-the-news/";
		String filename = "TestQuery.data";
		
		if (args.length != 1) {
			System.err.println("usage: <query size>");
			System.exit(1);
		}
		Class<?> cls = Class.forName("edu.hanyang.submit.TinySEQueryProcess");
		QueryProcess qp = (QueryProcess) cls.newInstance();
		
//		QueryProcess qp = (QueryProcess) new TinySEQueryProcess();
		
		ExecuteQuery eq = new ExecuteQuery();
		
		// for each query,
		List<String> testqueries = new ArrayList<>();
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath+filename)));

		int val = 0;
		String testquery = "";
		boolean frontQuote = true;
		
		while(testqueries.size() < Integer.parseInt(args[0])) {
			val = dis.readInt();
			if (val == -1) {
				testqueries.add(testquery.substring(0, testquery.length()-2));
				testquery = "";
			}
			else {
				if (val == -2) {
					if(frontQuote) {
						testquery += '"';
						frontQuote = false;
					}
					else {
						testquery = testquery.substring(0, testquery.length()-2) + "\" ";
						frontQuote = true;
					}
				}
				else {
					testquery += String.valueOf(val) + " ";
				}
			}
		}
		dis.close();
		long start = System.currentTimeMillis();
		
		for (String query: testqueries) {
			eq.executeQuery(qp, query);
		}
		
		long end = System.currentTimeMillis();
		System.out.println( "Runtime : " + (end - start) +" ms");
	}
}
