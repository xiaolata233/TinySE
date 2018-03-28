package edu.hanyang.indexer;

public interface ExternalSort {
	public void sort(String infile, String outfile, String tmpdir, int blocksize, int nblocks);
}
