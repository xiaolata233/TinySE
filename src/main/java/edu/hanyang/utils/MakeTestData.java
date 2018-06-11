package edu.hanyang.utils;

public class MakeTestData {
	final int QUERY_MIN_SIZE = 4;
	final int QUERY_MAX_SIZE = 10;
	
	public void makeQuery() {
		TestQuery query = new TestQuery((int) (Math.random() * (QUERY_MAX_SIZE - QUERY_MIN_SIZE + 1)) + QUERY_MIN_SIZE);
	}
	
	
	
	class TestQuery {
		int numOfWord;
		String[] query;
		
		public TestQuery(int num) {
			numOfWord = num;
			query = new String[num];
		}
		
		public void put(String word) {
			for(int i = 0; i < query.length; i++) {
				if(query[i] == null) {
					query[i] = word;
				}
			}
		}
		
		public boolean isEmpty() {
			for(String word : query) {
				if(word == null) { return true; }
			}
			return false;
		}
	}

	public static void main(String[] args) {
		MakeTestData mtd = new MakeTestData();
		mtd.makeQuery();
	}
}
