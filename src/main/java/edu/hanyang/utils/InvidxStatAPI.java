package edu.hanyang.utils;

import edu.hanyang.indexer.StatAPI;

public class InvidxStatAPI extends StatAPI {
	
	private ExecuteQuery eq = null;
	public InvidxStatAPI (ExecuteQuery eq) {
		this.eq = eq;
	}

	@Override
	public int get_pages(int termid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_doc_count(int termid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_min_docid(int termid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_max_docid(int termid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
