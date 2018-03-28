package edu.hanyang.indexer;

import java.util.List;

public interface Tokenizer {
	public void setup();
	public List<String> split (String str);
	public void clean();
}
