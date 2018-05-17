package edu.hanyang.indexer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.MutableTriple;

public class Index {
	
	private BPlusTree tree = null;
	
	public Index () throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// external code binding
		Class<?> cls = Class.forName("edu.hanyang.submit.TinySEBPlusTree");
		tree = (BPlusTree) cls.newInstance();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		if (args.length != 6) {
			System.err.print("usage: <inputfile> <directory metafile> <directory nodefile> <posting file> <block size> <number of available blocks>");
			System.exit(1);
		}
		
		int blocksize = Integer.parseInt(args[4]);
		int nblocks = Integer.parseInt(args[5]);
		
		Index index = new Index();
		index.run(args[0], args[1], args[2], args[3], blocksize, nblocks);
	}

	private void run(String inputfile, String dirmetafile, String dirnodefile, String postingfile, int blocksize, int nblocks) throws IOException {
		DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(inputfile)));
		
		int[] tuple = new int[3];
		
		int cnt = 0;
		int prev_word = -1;
		while (read_tuple(input, tuple)) {
			cnt++;
		}
	}

	private boolean read_tuple (DataInputStream in, int[] tuple) throws IOException {
		try {
			tuple[0] = in.readInt();
		}
		catch (EOFException e) {
			return false;
		}
		
		try {
			tuple[1] = in.readInt();
			tuple[2] = in.readInt();
		}
		catch (EOFException e) {
			throw new IOException("invalid input posting file; wrong alignment");
		}
		
		return true;
	}
}
