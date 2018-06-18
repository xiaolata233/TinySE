package edu.hanyang.perfeval;

import edu.hanyang.indexer.BPlusTree;
import edu.hanyang.indexer.QueryProcess;
import edu.hanyang.utils.ExecuteQuery;

public class EvalQueryProcess {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("usage: <package path>");
			System.exit(1);
		}
		Class<?> cls = Class.forName("edu.hanyang.submit.TinySEBPlusTree");
		QueryProcess qp = (QueryProcess) cls.newInstance();
		
		ExecuteQuery eq = new ExecuteQuery();
		
		// for each query,
		for (String query: testqueries) {
			eq.executeQuery(qp, query);
		}
	}

}
