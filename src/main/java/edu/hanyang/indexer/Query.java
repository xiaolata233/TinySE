package edu.hanyang.indexer;

public class Query {
	public int numOfWord;
	public int docID;
	public int[] query;
	
	public Query(int docID, int num) {
		numOfWord = num;
		this.docID = docID;
		query = new int[num];
		for(int i = 0; i < query.length; i++) {
			query[i] = -1;
		}
	}
	
	public void put(int termid) {
		for(int i = 0; i < query.length; i++) {
			if(query[i] == -1) {
				query[i] = termid;
			}
		}
	}
	
	public void put_random(int termid) {
		int position = (int) (Math.random() * numOfWord);
		while(query[position] != -1) {
			position = (int) (Math.random() * numOfWord);
		}
		query[position] = termid;
	}
	
	public boolean isEmpty() {
		for(int termid : query) {
			if(termid == -1) { return true; }
		}
		return false;
	}
	
	public boolean hasValue(int val) {
		for(int termid: query) {
			if(termid == val) { return true; }
		}
		return false;
	}
	
	public void introduce() {
		System.out.print(docID+": ");
		System.out.print("[");
		for(int value : query) {
			System.out.print(value+", ");
		}
		System.out.println("]");
	}
}
