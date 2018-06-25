package edu.hanyang.utils;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import edu.hanyang.indexer.StatAPI;

public class InvidxStatAPI extends StatAPI {
	String filepath = "../all-the-news/";
	String filename = "bplustree.tree";
	String posListFilename = "PostingList.data";
	int blocksize = 52;
	private ExecuteQuery eq = null;
	private RandomAccessFile raf = null;
	
	public InvidxStatAPI (ExecuteQuery eq) throws FileNotFoundException {
		this.eq = eq;
		eq.tree.open("metafile", filepath+filename, blocksize, 10);
		raf = new RandomAccessFile(filepath+posListFilename, "r");
	}

	@Override
	public int get_pages(int termid) throws Exception {
		return eq.tree.search(termid);
	}

	@Override
	public int get_doc_count(int termid) throws Exception {
		raf.seek(eq.tree.search(termid));
		raf.readInt(); // --> # of blocks
		return raf.readInt();
	}

	@Override
	public int get_min_docid(int termid) throws Exception {
		raf.seek(eq.tree.search(termid));
		raf.readInt(); // --> # of blocks
		raf.readInt(); // --> doc count
		return raf.readInt();
	}

	@Override
	public int get_max_docid(int termid) throws Exception {
		raf.seek(eq.tree.search(termid));
		raf.readInt(); // --> # of blocks
		raf.readInt(); // --> doc count
		raf.readInt(); // --> min docid
		return raf.readInt();
	}

}
