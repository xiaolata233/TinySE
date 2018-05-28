package edu.hanyang.utils;

import java.util.ArrayList;
import java.util.List;

import edu.hanyang.indexer.IntermediateList;

public class TestIntermediateList extends IntermediateList {
	public List<Integer> docList;
	int docIdx;
	
	public TestIntermediateList() {
		docList = new ArrayList<>();
	}

	public void put_docid(int docid) {
		docList.add(docid);
	}
}
