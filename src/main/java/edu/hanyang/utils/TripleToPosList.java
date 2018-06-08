package edu.hanyang.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.MutableTriple;

public class TripleToPosList {
	String filepath = "../all-the-news/";
	int blocksize = 52;
	
	public void introduce(String filename) {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath+filename)));
			byte[] buf = new byte[blocksize];
			int cnt = 0;
			int numOfPos = 0;
			boolean newDoc = true;
			int numOfBlock = dis.readInt();
			System.out.println("<HEADER>");
			System.out.println("["+numOfBlock+", "+dis.readInt()+"]");
			System.out.println("<CONTENT>");
			
			while(cnt < numOfBlock) {
				dis.readFully(buf);
				DataInputStream pkt = new DataInputStream(new ByteArrayInputStream(buf));
				int capacity = 0;
				
				CheckIsItFull : while(capacity < blocksize) {
					if(newDoc) {
						System.out.print("【"+pkt.readInt()+"】\t");
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
	//	<Header> : word가 점유하는 블록 개수, 읽어야하는 doc개수 (전부 int)
	//	<Content> : Doc_id (int), # of pos, pos1, pos2, … (short)
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

			// <WordID, DocID, Position>
			while(currentWordID == 0) {
				int currentVal = dis.readInt();
				if (cnt%3 == 2) { // Here comes a new position
					content.add((short) currentVal);
					numOfPos++;
				}
				else if (cnt%3 == 0 && currentWordID != currentVal) { // Here comes a new word
					byteBufferWrite(numOfDoc, docID, content);
					currentWordID = currentVal;
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

	public void byteBufferWrite(int numOfDoc, List<Integer> docID, List<Short> content) throws IOException {
		int blockcnt = 1;
		int docIDCursor = 0;
		byte[] buf = new byte[blocksize];
		ByteBuffer bf = ByteBuffer.wrap(buf);
		RandomAccessFile raf = new RandomAccessFile(filepath+"PostingList.data", "rw");
	
		raf.writeInt(blockcnt); // Room for Header value 1
		raf.writeInt(numOfDoc); // Header value 2
		
		for(int i = 0; i < content.size(); i++) {
			if (bf.position() == bf.capacity() || (bf.position() == bf.capacity()-2 && content.get(i) == -1)) {
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
		raf.seek(0);
		raf.writeInt(blockcnt); // Header value 1

		raf.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		TripleToPosList ttp = new TripleToPosList();
//		ttp.readDataFile("SortedInvertedTripleList.data");
		ttp.introduce("PostingList.data");
	}
}
