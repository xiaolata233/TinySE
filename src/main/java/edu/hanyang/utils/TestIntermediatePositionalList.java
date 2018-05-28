package edu.hanyang.utils;

import java.util.ArrayList;
import java.util.List;

import edu.hanyang.indexer.IntermediatePositionalList;

public class TestIntermediatePositionalList extends IntermediatePositionalList {
	public List<Integer> posList;
	int docIdIdx;

	public TestIntermediatePositionalList() {
		posList = new ArrayList<>();
		docIdIdx = 0;
	}

	// list:    1 1 2 3 4
	// idx:     0 1 2 3 4
	// docIdx:  0
	// nPosIdx: 0 + 1 = 1
	// nPos:    5 - 0 - 1 = 4
	// result:  1 4 1 2 3 4, 2 
	// idx:     0 1 2 3 4 5, 6

	// list:    1 4 1 2 3 4, 2 1 2 
	// idx:     0 1 2 3 4 5, 6 7 8
	// docIdx:  6
	// nPosIdx: 6 + 1 = 7
	// nPos:    9 - 6 - 1 = 2
	// result:  1 4 1 2 3 4, 2 2 1 2
	public void put_docid_and_pos(int docid, int pos) {
		if (posList.size() == 0) {
			posList.add(docid);
			posList.add(1);
			posList.add(pos);
			return;
		}

		if (posList.get(docIdIdx) == docid) {
			posList.add(pos);
			posList.set(docIdIdx + 1, posList.get(docIdIdx + 1) + 1);
		} else {
			posList.add(docid);
			docIdIdx = posList.size() - 1;
			posList.add(1);
			posList.add(pos);
		}
	}
}