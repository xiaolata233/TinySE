package edu.hanyang.utils;

import java.io.IOException;

import edu.hanyang.indexer.PositionCursor;

public class InvidxPosCursor extends PositionCursor {

	@Override
	public boolean is_eol() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int get_pos() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void go_next() throws IOException {
		// TODO Auto-generated method stub
		
	}

	
	// XXX: obsolete
	@Override
	public int get_term_count() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
