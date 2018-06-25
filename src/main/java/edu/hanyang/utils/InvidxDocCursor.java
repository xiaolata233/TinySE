package edu.hanyang.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import edu.hanyang.indexer.BPlusTree;
import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.PositionCursor;

public class InvidxDocCursor extends DocumentCursor {
	final int BLOCK_SIZE = 52;
	String filepath = "../all-the-news/";
	String posListFilename = "PostingList.data";
	byte[] buf = new byte[BLOCK_SIZE];
	ByteBuffer buffer = ByteBuffer.wrap(buf);
	
	int bufCapacity = BLOCK_SIZE;
	
	BPlusTree tree = null;
	TestPosCursor posCursor = null;
	RandomAccessFile raf = new RandomAccessFile(filepath + posListFilename, "r");
	public int termid = -1;
	public int currentDocID = 0;
	
	int numOfBlocks = 0;
	int numOfDocs = 0;
	int minDocID = 0;
	int maxDocID = 0;
	long offset = 0;
	
	int cnt = 0;
	
	public InvidxDocCursor (int termid, LIST_TYPE type, BPlusTree tree) throws IOException {
		this.termid = termid;
		this.type = type;
		
		this.tree = tree;
		offset = tree.search(termid);
		raf.seek(offset);
		
		numOfBlocks = raf.readInt();
		numOfDocs = raf.readInt();
		minDocID = raf.readInt();
		maxDocID = raf.readInt();
		
		go_next();
	}

	@Override
	public boolean is_eol() throws IOException {
		if (cnt >= numOfDocs) {
//			System.out.println(currentDocID+" & "+maxDocID);
			return true;
		}
		return false;
	}

	@Override
	public int get_docid() throws IOException {
		return currentDocID;
	}

	@Override
	public void go_next() throws IOException  {
		if(is_eol()) {
			throw new IOException("end of posting list");
		}
		
		if(bufCapacity >= BLOCK_SIZE-2) {
			clearBuffer();
		}
		
		currentDocID = buffer.getInt(bufCapacity);
		bufCapacity += 4;
		
		if(bufCapacity == BLOCK_SIZE) {
			clearBuffer();
		}
		
		int numOfPos = buffer.getShort(bufCapacity);
		bufCapacity += 2;
		List<Integer> posList = new ArrayList<>();

		for(int i = 0; i < numOfPos; i++) {
			if(bufCapacity == BLOCK_SIZE) {
				clearBuffer();
			}
			posList.add((int) buffer.getShort(bufCapacity));
			bufCapacity += 2;
		}
		if(type == LIST_TYPE.POSLIST) { this.posCursor = new TestPosCursor(posList); }
		
		cnt++;
	}

	public void clearBuffer() throws IOException {
		buffer.clear();
		raf.read(buf);
		bufCapacity = 0;
	}

	@Override
	public PositionCursor get_position_cursor() throws IOException {
		if (type != LIST_TYPE.POSLIST) throw new IOException("errror: this cursor cannot access in-doc positions");
		return this.posCursor;
	}

	@Override
	public int get_doc_count() throws Exception {
		return numOfDocs;
	}

	@Override
	public int get_min_docid() throws Exception {
		return minDocID;
	}

	@Override
	public int get_max_docid() throws Exception {
		return maxDocID;
	}

}
