package edu.hanyang.indexer;

import java.io.IOException;

public interface DocumentCursor {
	/**
	 * @return
	 * true if the cursor is currently located at the end of list
	 * false otherwise
	 */
	public boolean is_eol() throws IOException;
	/**
	 * @return
	 * docid at the current cursor
	 */
	public int get_docid() throws IOException;
	/**
	 * shift the cursor to the next document
	 */
	public void go_next() throws IOException;
	/**
	 * @return
	 * an instance of PositionCursor; you may retrieve positions
	 * where the term appears in the current document
	 */
	public PositionCursor get_position_cursor () throws IOException;
	/**
	 * @return
	 * the number of documents in the inverted list
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public int get_doc_count() throws Exception;
	/**
	 * @return
	 * the minimum document id
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public int get_min_docid() throws Exception;
	/**
	 * @return
	 * the maximum document id
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public int get_max_docid() throws Exception;
}
