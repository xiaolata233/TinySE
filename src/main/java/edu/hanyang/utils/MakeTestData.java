package edu.hanyang.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import edu.hanyang.indexer.Query;

public class MakeTestData {
	final int QUERY_MIN_SIZE = 4;
	final int QUERY_MAX_SIZE = 10;
	final double QUERY_GENERATION_RATIO = 0.0005;
	final double QUERY_ADDITION_RATIO = 0.2;
	String filepath = "../all-the-news/";
	String filename = "InvertedTripleList.data";
	
	DataInputStream dis = null;
	DataOutputStream dos = null;
	
	// make test query (termid) without quotation marks(")
	public void makeQuery(int number) {
		int numOfQuery = 0;
		int wordID = 0;
		int docID = 0;
		int pos = 0;
		boolean making = false;
		Query query = new Query(-1, 0); // initialization is meaningless but for <Errors: May not have been initialized>
		
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath+filename)));
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filepath+"TestQuery.data")));
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
					// out of document
						query = null;
						making = false;
					}
					else if(Math.random() < QUERY_ADDITION_RATIO) {
						if(query.isEmpty() && !query.hasValue(wordID)) {
							query.put_random(wordID);
							
							if(!query.isEmpty()) {
								for(int value : query.query) {
									dos.writeInt(value);
								}
								dos.writeInt(-1);
								numOfQuery++;
							}
						}
					}
					else {
						continue;
					}
				}
			}
			dis.close();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// make test query (termid) with quotation marks(")
	public void makeQueryWithQuotation(int number) {
		int numOfQuery = 0;
		int wordID = 0;
		int docID = 0;
		int pos = 0;
		boolean making = false;
		boolean quoteQueryMaking = false;
		Query query = new Query(-1, 0); // initialization is meaningless but for <Errors: May not have been initialized>
		
		int[] quoteQuery = null;
		int quoteQueryPos = 0;
		
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath+filename)));
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filepath+"TestQuery.data", true)));
			while(numOfQuery < number) {
				wordID = dis.readInt();
				docID = dis.readInt();
				pos = dis.readInt();
				
				if(!making && Math.random() < QUERY_GENERATION_RATIO && !quoteQueryMaking) {
					query = new Query(docID, (int) (Math.random() * (QUERY_MAX_SIZE - QUERY_MIN_SIZE + 1)) + QUERY_MIN_SIZE + 2);
					making = true;
					quoteQuery = new int[(int) (Math.random() * (query.query.length - 3)) + 2];
					quoteQueryMaking = true;
				}
				else if(making) {
					if(docID != query.docID) {
					// out of document
						query = null;
						making = false;
						quoteQueryPos = 0;
						quoteQueryMaking = false;
					}
					else if(quoteQueryMaking) {
						if(quoteQueryPos < quoteQuery.length) {
							quoteQuery[quoteQueryPos] = wordID;
							quoteQueryPos++;
						}
						else {
							query.put_quotation(quoteQuery);
							quoteQuery = new int[(int) (Math.random() * (QUERY_MAX_SIZE - QUERY_MIN_SIZE + 1))];
							quoteQueryMaking = false;
						}
					}
					else if(Math.random() < QUERY_ADDITION_RATIO) {
						if(query.isEmpty() && !query.hasValue(wordID)) {
							query.put_random(wordID);
							
							if(!query.isEmpty()) {
								for(int value : query.query) {
									dos.writeInt(value);
								}
								dos.writeInt(-1);
								numOfQuery++;
							}
						}
					}
					else {
						continue;
					}
				}
			}
			dis.close();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MakeTestData mtd = new MakeTestData();
		mtd.makeQuery(7000);
		mtd.makeQueryWithQuotation(3000);
	}
}
