package edu.hanyang.utils;

import java.io.IOException;
import java.util.List;

import edu.hanyang.indexer.PositionCursor;

public class TestPosCursor extends PositionCursor {
	List<Integer> poslist;
	int currentPosIdx = 0;
	
	public TestPosCursor(List<Integer> poslist) {
		this.poslist = poslist;
		this.currentPosIdx = 0;
	}

	public boolean is_eol() {
		if(currentPosIdx >= poslist.size()){
			return true;
		}
		return false;
	}

	public int get_pos() throws IOException {
		if(is_eol()){
			throw new IOException("Wrong use of PositionCursor : out of index");
		}
		return poslist.get(currentPosIdx);
	}

	public void go_next() throws IOException {
		if(is_eol()){
			throw new IOException("Wrong use of PositionCursor : out of index");
		}
		
		currentPosIdx++;
	}

	public int get_term_count() {
		return poslist.size();
	}
}
