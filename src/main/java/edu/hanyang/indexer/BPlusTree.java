package edu.hanyang.indexer;

public interface BPlusTree {
	public void open(String metafile, String filepath, int blocksize, int nblocks);
	public void insert(int key, int val);
	public int search(int key);
	public void close();
}
