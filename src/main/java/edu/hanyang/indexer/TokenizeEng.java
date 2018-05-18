package edu.hanyang.indexer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.TreeMap;

public class TokenizeEng {
	
	private Tokenizer tokenizer = null;
	
	private int current_termid = 0;
	private TreeMap<String, Integer> termids = new TreeMap<String, Integer> ();
	
	public TokenizeEng () throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// external code binding
		Class<?> cls = Class.forName("edu.hanyang.submit.TinySETokenizer");
		tokenizer = (Tokenizer) cls.newInstance();
	}
	
	private int get_termid (String term) {
		if (termids.containsKey(term)) {
			return termids.get(term);
		}
		else {
			termids.put(term, current_termid);
			return current_termid++;
		}
	}
	
	private void write_termids (String termidfile) throws IOException {
		try (PrintStream os = new PrintStream(new BufferedOutputStream(new FileOutputStream(termidfile)) )) {
			for (String term: termids.keySet()) {
				os.println(term + "\t" + termids.get(term));
			}
		}
	}
	
	public void run (String dir, String outputfile, String termidfile) throws IOException {
		try (DataOutputStream postings = new DataOutputStream(new BufferedOutputStream (new FileOutputStream (outputfile)))) {
			// initialize tokenizer
			tokenizer.setup();
			
			File directory = new File(dir);
			File[] contents = directory.listFiles();
			
			for (File f : contents) {
				if (f.isFile()) {
					int docid = Integer.parseInt(f.getName());
					
					int pos = 0;
					try (BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()))) {
	
						String line = null;
						while ((line = br.readLine()) != null) {
							List<String> arr = tokenizer.split(line);
							
							for (String term: arr) {
								int id = get_termid (term);
								
								postings.writeInt(id);
								postings.writeInt(docid);
								postings.writeInt(pos++);
							}
						}
	
					} catch (IOException e) {
						System.err.println("[warn] fail to read file: " + f.getAbsolutePath());
					}
				}
			}
		}
		finally {
			// finalize tokenizer
			tokenizer.clean();
			
			// write term and termid pairs
			write_termids(termidfile);
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.print("usage: <directory to corpus> <output posting list file> <termid mapping file>");
			System.exit(1);
		}
		
		TokenizeEng indexer = new TokenizeEng();
		indexer.run(args[0], args[1], args[2]);
	}

}
