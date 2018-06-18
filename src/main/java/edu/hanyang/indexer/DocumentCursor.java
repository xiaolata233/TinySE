package edu.hanyang.indexer;

import java.io.IOException;

public abstract class DocumentCursor {
	public enum LIST_TYPE { POSLIST, NONPOSLIST };
	public LIST_TYPE type = null;
	
	/**
	 * @return
	 * true if the cursor is currently located at the end of list
	 * false otherwise
	 */
	public abstract boolean is_eol() throws IOException;
	/**
	 * @return
	 * docid at the current cursor
	 */
	public abstract int get_docid() throws IOException;
	/**
	 * shift the cursor to the next document
	 */
	public abstract void go_next() throws IOException;
	/**
	 * @return
	 * an instance of PositionCursor; you may retrieve positions
	 * where the term appears in the current document
	 */
	public abstract PositionCursor get_position_cursor () throws IOException;
	/**
	 * @return
	 * the number of documents in the inverted list
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public abstract int get_doc_count() throws Exception;
	/**
	 * @return
	 * the minimum document id
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public abstract int get_min_docid() throws Exception;
	/**
	 * @return
	 * the maximum document id
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public abstract int get_max_docid() throws Exception;
}
