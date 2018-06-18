package edu.hanyang.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.PositionCursor;
import edu.hanyang.submit.TinySEBPlusTree;

public class InvidxDocCursor extends DocumentCursor {
	String filepath = "../all-the-news/";
	String filename = "bplustree.tree";
	String posListFilename = "PostingList.data";
	int blocksize = 52;
	TinySEBPlusTree btree = new TinySEBPlusTree();
	List<Integer> list = new ArrayList<>();
	public int termid = -1;
	
	public InvidxDocCursor (int termid, LIST_TYPE type) throws IOException {
		this.termid = termid;
		this.type = type;
		
		btree.open("metapath", filepath+filename, blocksize, 10);
		RandomAccessFile raf = new RandomAccessFile(filepath+posListFilename, "r");
		raf.seek(btree.search(termid));
		int numOfBlock = raf.readInt(); // numOfBlock
		raf.readInt(); // numOfDocuments
		raf.readInt(); // min Doc Number
		raf.readInt(); // MAX Doc Number
		
		byte[] buf = new byte[blocksize];
		int cnt = 0;
		int numOfPos = 0;
		boolean newDoc = true;
		
		while(cnt < numOfBlock) {
			raf.readFully(buf);
			DataInputStream pkt = new DataInputStream(new ByteArrayInputStream(buf));
			int capacity = 0;
			
			CheckIsItFull : while(capacity < blocksize) {
				if(newDoc) {
					list.add(pkt.readInt());
					capacity = capacity + 4;
					newDoc = false;
					continue CheckIsItFull;
				}
				else if(numOfPos == 0) {
					numOfPos = pkt.readShort();
					capacity = capacity + 2;
					continue CheckIsItFull;
				}
				else if(numOfPos > 1){
					pkt.readShort();
					capacity = capacity + 2;
					numOfPos--;
					continue CheckIsItFull;
				}
				else {
					pkt.readShort();
					capacity = capacity + 2;
					numOfPos--;
					newDoc = true;
					if (capacity == blocksize-2) { break CheckIsItFull; }
					continue CheckIsItFull;
				}
			}
			cnt++;
			pkt.close();
		}
		raf.close();
	}
	
	public InvidxDocCursor(TestIntermediateList list) {
		this.list = list.docList;
		this.type = LIST_TYPE.NONPOSLIST;
	}

	public InvidxDocCursor(TestIntermediatePositionalList list) {
		this.list = list.posList;
		this.type = LIST_TYPE.POSLIST;
	}

	@Override
	public boolean is_eol() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int get_docid() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void go_next() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PositionCursor get_position_cursor() throws IOException {
		if (type != LIST_TYPE.POSLIST) throw new IOException("errror: this cursor cannot access in-doc positions");
		return null;
	}

	
	// XXX: obsolete
	@Override
	public int get_doc_count() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_min_docid() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_max_docid() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		InvidxDocCursor idc = new InvidxDocCursor(1, LIST_TYPE.NONPOSLIST);
//		for(int i = 0; i < idc.list.size(); i++) {
//			System.out.print(idc.list.get(i)+", ");
//		}
	}

}
