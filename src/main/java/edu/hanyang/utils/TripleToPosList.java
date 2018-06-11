package edu.hanyang.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import edu.hanyang.submit.TinySEBPlusTree;

public class TripleToPosList {
	String filepath = "../all-the-news/";
	int blocksize = 52;
	
	public void introduce(String filename, int targetWord) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filepath+filename, "r");
			long offset = 0;
			int numOfBlock = raf.readInt();

			for(int i = 0; i < targetWord; i++) {
				offset += 16 + numOfBlock * blocksize;
				raf.seek(offset);
				numOfBlock = raf.readInt();
			}
			
			byte[] buf = new byte[blocksize];
			int cnt = 0;
			int numOfPos = 0;
			boolean newDoc = true;
		
			System.out.println("<HEADER>");
			System.out.println("Blocks: "+numOfBlock);
			System.out.println("Documents: "+raf.readInt());
			System.out.println("Min Doc: "+raf.readInt());
			System.out.println("Max Doc: "+raf.readInt());
			System.out.println("<CONTENT>");
			
			while(cnt < numOfBlock) {
				raf.readFully(buf);
				DataInputStream pkt = new DataInputStream(new ByteArrayInputStream(buf));
				int capacity = 0;
				
				CheckIsItFull : while(capacity < blocksize) {
					if(newDoc) {
						System.out.print("["+pkt.readInt()+"] ");
						capacity = capacity + 4;
						newDoc = false;
						continue CheckIsItFull;
					}
					else if(numOfPos == 0) {
						numOfPos = pkt.readShort();
						System.out.print("("+numOfPos+")\t");
						capacity = capacity + 2;
						continue CheckIsItFull;
					}
					else if(numOfPos > 1){
						System.out.print(pkt.readShort()+"\t");
						capacity = capacity + 2;
						numOfPos--;
						continue CheckIsItFull;
					}
					else {
						System.out.print(pkt.readShort()+"\t");
						capacity = capacity + 2;
						numOfPos--;
						newDoc = true;
						if (capacity == blocksize-2) { break CheckIsItFull; }
						continue CheckIsItFull;
					}
				}
				System.out.println();
				cnt++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Make posting list from sorted inverted list data
	//	<Header> : # of Blocks, # of Docs, Min Doc, Max Doc (all int)
	//	<Content> : Doc_id (int), # of pos, pos1, pos2, ... (short)
	public void readDataFile(String filename) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath+filename)));
			int currentWordID = 0;
			int currentDocID = -1;
			List<Short> content = new ArrayList<>();
			// -1, 0, # of pos, pos1, pos2, ..., -1, 1, # of pos, pos1, pos2, ... --> List<Short> content
			// <--------- Doc 0 ------------> <---------- Doc 1 ----------->
			List<Integer> docID = new ArrayList<>();
			int cnt = 0;
			int numOfDoc = 0;
			int numOfPos = 0;
			int currentVal = 0;

			// <WordID, DocID, Position>
			while((currentVal = dis.readInt()) != -1) {
				if (cnt%3 == 2) { // Here comes a new position
					content.add((short) currentVal);
					numOfPos++;
				}
				else if (cnt%3 == 0 && currentWordID != currentVal) { // Here comes a new word
					content.set(content.size()-numOfPos-1, (short) numOfPos);
					byteBufferWrite(numOfDoc, docID, content);
					currentWordID = currentVal;
					currentDocID = -1;
					numOfDoc = 0;
					docID.clear();
					content.clear();
				}
				else if (cnt%3 == 1 && currentDocID != currentVal) { // Here comes a new document
					if (!content.isEmpty()) { content.set(content.size()-numOfPos-1, (short) numOfPos); }
					content.add((short) -1); // -1 means starting of new document
					content.add((short) 0); // make a room for # of positions
					docID.add(currentVal);
					numOfPos = 0;
					currentDocID = currentVal;
					numOfDoc++;
				}
				cnt++;
			}
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Make B+Tree Posting List
	public void readDataFileToTree(String filename) {
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(filepath+filename, "r");
			long offset = 0;
			int currentWordID = 0;
			int numOfBlock = 0;
			
			TinySEBPlusTree tbt = new TinySEBPlusTree();
			tbt.open("metapath", filepath+"bplustree.tree", blocksize, 10);
			tbt.insert(currentWordID, numOfBlock);
	
			while((numOfBlock = raf.readInt()) != -1) {
				currentWordID++;
				offset += 16 + numOfBlock * blocksize;
				tbt.insert(currentWordID, (int)offset);
				raf.seek(offset);
			}
			tbt.close();
			raf.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void byteBufferWrite(int numOfDoc, List<Integer> docID, List<Short> content) throws IOException {
		int blockcnt = 1;
		int docIDCursor = 0;
		byte[] buf = new byte[blocksize];
		ByteBuffer bf = ByteBuffer.wrap(buf);
		RandomAccessFile raf = new RandomAccessFile(filepath+"PostingList.data", "rw");
		raf.seek(raf.length());
	
		raf.writeInt(blockcnt); // Room for Header value 1
		raf.writeInt(numOfDoc); // Header value 2
		raf.writeInt(docID.get(0)); // Header value 3
		raf.writeInt(docID.get(docID.size()-1)); // Header value 4
		
		for(int i = 0; i < content.size(); i++) {
			if (bf.position() == bf.capacity() || (bf.position() == bf.capacity()-2 && content.get(i) == -1)) {
			// ByteBuffer is full <OR> No room for DocID value (Integer)
				raf.write(buf);
				bf.clear();
				blockcnt++;
			}
			if(content.get(i) == -1) {
				bf.putInt(docID.get(docIDCursor));
				docIDCursor++;
			}
			else {
				bf.putShort(content.get(i));
			}
		}
		raf.write(buf);
		bf.clear();
		raf.seek(raf.length()-blockcnt*blocksize-16);
		raf.writeInt(blockcnt); // Header value 1

		raf.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		TripleToPosList ttp = new TripleToPosList();
//		ttp.readDataFile("SortedInvertedTripleList.data");
//		ttp.readDataFileToTree("PostingList_52.data");
		ttp.introduce("PostingList_52.data", 1);
	}
}
