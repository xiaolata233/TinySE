package edu.hanyang.indexer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class Index {
	
	private final String tmppostingsfile = "tmppostings";
	
	public String dir = null;
	public String tmpdir = null;
	
	private Tokenizer tokenizer = null;
	
	private int current_termid = 0;
	private TreeMap<String, Integer> termids = new TreeMap<String, Integer> ();
	
	public Index (String dir, String tmpdir) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.dir = dir;
		this.tmpdir = tmpdir;
		
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
	
	public void run () throws IOException {
		File directory = new File(dir);
		File[] contents = directory.listFiles();
		
		tokenizer.setup();
		
		DataOutputStream postings = new DataOutputStream(
				new BufferedOutputStream (
						new FileOutputStream (
								dir + File.separator + tmppostingsfile)));
		
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
		
		postings.close();
		tokenizer.clean();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.print("usage: <directory to corpus> <temporary directory>");
			System.exit(1);
		}
		
		Index indexer = new Index(args[0], args[1]);
		indexer.run();
	}

}
