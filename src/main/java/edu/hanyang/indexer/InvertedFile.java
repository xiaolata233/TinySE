package edu.hanyang.indexer;

import java.io.IOException;

public interface InvertedFile {
	/**
	 * @param metafile
	 * 		meta file path
	 * @param postinglistfile
	 * 		posting list file (requirement: use a single file for inverted lists)
	 * @throws IOException
	 */
	public void open(String metafile, String postinglistfile) throws IOException;
	public void create() throws IOException;
	public DocumentCursor get_cursor (int termid) throws IOException;
	public void close() throws IOException;
}
