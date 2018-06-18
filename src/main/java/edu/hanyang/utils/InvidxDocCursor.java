package edu.hanyang.utils;

import java.io.IOException;

import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.PositionCursor;

public class InvidxDocCursor extends DocumentCursor {
	
	public int termid = -1;
	
	public InvidxDocCursor (int termid, LIST_TYPE type) {
		this.termid = termid;
		this.type = type;
	}

	@Override
	public boolean is_eol() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int get_docid() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void go_next() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PositionCursor get_position_cursor() throws IOException {
		if (accesstype != 0) throw new IOException("errror: this cursor cannot access in-doc positions");
		return null;
	}

	
	// XXX: obsolete
	@Override
	public int get_doc_count() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_min_docid() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_max_docid() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
