package edu.hanyang.indexer;

public abstract class StatAPI {
	public abstract int get_pages(int termid) throws Exception;
	public abstract int get_doc_count(int termid) throws Exception;
	public abstract int get_min_docid(int termid) throws Exception;
	public abstract int get_max_docid(int termid) throws Exception;
}
