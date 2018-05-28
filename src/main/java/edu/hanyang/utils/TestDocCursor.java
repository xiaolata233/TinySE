package edu.hanyang.utils;

import java.io.IOException;

import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.PositionCursor;

public class TestDocCursor extends DocumentCursor {
	public boolean is_eol() {
		return false;
	}
	public int get_docid() {
		return 0;
	}
	public void go_next() {
		
	}
	public PositionCursor get_position_cursor () {
		return null;
	}
	public int get_doc_count() {
		return 0;
	}
	public int get_min_docid() {
		return 0;
	}
	public int get_max_docid() {
		return 0;
	}
}
