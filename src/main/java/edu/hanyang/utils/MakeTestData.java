package edu.hanyang.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.hanyang.indexer.Query;

public class MakeTestData {
	final int QUERY_MIN_SIZE = 4;
	final int QUERY_MAX_SIZE = 10;
	final double QUERY_GENERATION_RATIO = 0.00005;
	final double QUERY_ADDITION_RATIO = 0.2;
	String filepath = "../all-the-news/";
	String filename = "InvertedTripleList.data";
	
	public void makeQuery(int number) {
		int numOfQuery = 0;
		int wordID = 0;
		int docID = 0;
		int pos = 0;
		boolean making = false;
		Query query = new Query(-1, 0); // initialization is meaningless but for <Errors: May not have been initialized>
		
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath+filename)));
			while(numOfQuery < number) {
				wordID = dis.readInt();
				docID = dis.readInt();
				pos = dis.readInt();
				
				if(!making && Math.random() < QUERY_GENERATION_RATIO) {
					query = new Query(docID, (int) (Math.random() * (QUERY_MAX_SIZE - QUERY_MIN_SIZE + 1)) + QUERY_MIN_SIZE);
					making = true;
				}
				else if(making) {
					if(docID != query.docID) {
						query = null;
						making = false;
					}
					else if(Math.random() < QUERY_ADDITION_RATIO) {
						if(query.isEmpty() && !query.hasValue(wordID)) {
							query.put_random(wordID);
							
							if(!query.isEmpty()) {
								query.introduce();
								numOfQuery++;
							}
						}
					}
					else {
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MakeTestData mtd = new MakeTestData();
		mtd.makeQuery(200);
	}
}
