package edu.hanyang.utils;

import java.io.IOException;
import java.util.List;

import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.IntermediateList;
import edu.hanyang.indexer.IntermediatePositionalList;
import edu.hanyang.indexer.PositionCursor;

public class TestDocCursor extends DocumentCursor {
	List<Integer> posList; // word1: docID1, nPos, pos1, pos2.., docID2
							// word2: docID1, nPos, pos1, pos2.., docID2
	int type; // type = 0: IntermediateList, type = 1: IntermediatePositionalList
	int currentDocPos;

	public TestDocCursor(IntermediateList list) {
		this.posList = ((TestIntermediateList) list).docList;
		this.type = 0;
	}

	public TestDocCursor(IntermediatePositionalList list) {
		this.posList = ((TestIntermediatePositionalList) list).posList;
		this.type = 1;
	}

	public TestDocCursor(List<Integer> positionalList) {
		this.posList = positionalList;
		this.type = 1;
	}

	public boolean is_eol() {
		if (currentDocPos >= posList.size()) {
			return true;
		}
		return false;
	}

	public int get_docid() {
		return posList.get(currentDocPos);
	}

	// postinglist:  1 2 1 2, 2 3 1 2 3
	// index number: 0 1 2 3, 4 5 6 7 8
	// 1st doc pos:  0 + 2 + 1 + 1 = 4
	// 2nd doc pos:  4 + 3 + 1 + 1 = 9
	public void go_next() throws IOException {
		if (is_eol()) {
			throw new IOException("Wrong use of DocumentCursor : out of index");
		}
		if (type == 0) { // docID list
			currentDocPos++;
		} else { // positional list
			int step = posList.get(currentDocPos + 1);
			currentDocPos += (step + 1 + 1);
		}
	}

	// postinglist:  1 2 1 2, 2 3 1 2 3
	// index number: 0 1 2 3, 4 5 6 7 8
	// start pos:    0 + 2 = 2
	// end pos:      2 + 2 = 4
	public PositionCursor get_position_cursor() throws IOException {
		if (type == 0) {
			throw new IOException("Wrong use of DocumentCursor : document id list cannot make position cursor");
		}

		int start = currentDocPos + 2;
		int end = start + posList.get(currentDocPos + 1);
		PositionCursor cursor = new TestPosCursor(posList.subList(start, end));
		return cursor;
	}

	public int get_doc_count() throws IOException {
		if (type == 0) { // docID list
			return posList.size();
		} else { // positional list
			int tmp = currentDocPos;
			currentDocPos = 0;
			int cnt = 0;

			while (!is_eol()) {
				go_next();
				cnt++;
			}

			currentDocPos = tmp;
			return cnt;
		}
	}

	public int get_min_docid() {
		return posList.get(0);
	}

	public int get_max_docid() throws IOException {
		if (type == 0) {
			return posList.get(posList.size() - 1);
		} else {
			int tmp = currentDocPos;
			currentDocPos = 0;
			int docID = 0;

			while (!is_eol()) {
				docID = get_docid();
				go_next();
			}

			currentDocPos = tmp;
			return docID;
		}
	}
}
