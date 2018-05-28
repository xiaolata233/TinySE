package edu.hanyang.indexer;

import java.io.IOException;

/**
 * @author yhkim
 *
 */
public abstract class PositionCursor {
	/**
	 * @return
	 * true if current cursor is located at the end of list
	 * false otherwise
	 */
	public abstract boolean is_eol() throws IOException;
	/**
	 * @return
	 * position at the current cursor
	 */
	public abstract int get_pos() throws IOException;
	/**
	 * shift cursor to the next position
	 */
	public abstract void go_next() throws IOException;
	/**
	 * @return
	 * the term frequency in the current document
	 * @throws Exception
	 * if the method is not implemented yet, it throws an exception
	 */
	public abstract int get_term_count() throws Exception;
}
