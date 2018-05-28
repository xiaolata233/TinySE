package edu.hanyang.utils;

import java.io.IOException;

import edu.hanyang.indexer.PositionCursor;

public class TestPosCursor extends PositionCursor {
	public boolean is_eol() {
		return false;
	}
	public int get_pos() {
		return 0;
	}
	public void go_next() {
	}
	public int get_term_count() {
		return 0;
	}
}
