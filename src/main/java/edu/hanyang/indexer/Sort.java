package edu.hanyang.indexer;

import java.io.IOException;

public class Sort {
	
	private ExternalSort extsort = null;
	
	public Sort () throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		// external code binding
		Class<?> cls = Class.forName("edu.hanyang.submit.TinySEExternalSort");
		extsort = (ExternalSort) cls.newInstance();
	}
	
	public void run (String infile, String outfile, String tmpdir, int blocksize, int nblocks) throws IOException {
		extsort.sort(infile, outfile, tmpdir, blocksize, nblocks);
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		if (args.length != 5) {
			System.err.print("usage: <inputfile> <outputfile> <temporary directory> <block size> <number of available blocks>");
			System.exit(1);
		}
		
		int blocksize = Integer.parseInt(args[3]);
		int nblocks = Integer.parseInt(args[4]);
		
		Sort sort = new Sort ();
		sort.run(args[0], args[1], args[2], blocksize, nblocks);
	}

}
