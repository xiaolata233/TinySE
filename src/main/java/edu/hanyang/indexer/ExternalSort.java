package edu.hanyang.indexer;

import java.io.IOException;

public interface ExternalSort {
	public void sort(String infile, String outfile, String tmpdir, int blocksize, int nblocks) throws IOException;
}
