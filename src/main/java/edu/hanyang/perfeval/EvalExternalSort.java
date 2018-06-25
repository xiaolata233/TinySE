package edu.hanyang.perfeval;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import edu.hanyang.submit.TinySEExternalSort;

public class EvalExternalSort {
	public static void main(String[] args) throws IOException {
		String infile = "./test-10000000.data";
		String outfile = "./sort-10000000.data";
		String tmpdir = "./tmp";
		int blocksize = 4096;
		int nblocks = 100;

		TinySEExternalSort sort = new TinySEExternalSort();
		long s = System.currentTimeMillis();
		sort.sort(infile, outfile, tmpdir, blocksize, nblocks);
		long e = System.currentTimeMillis();

		String answerfile = "./result-10000000.data";
		DataInputStream answerdis = new DataInputStream(new BufferedInputStream(new FileInputStream(answerfile)));
		DataInputStream datadis = new DataInputStream(new BufferedInputStream(new FileInputStream(outfile)));

		for (int i = 0; i < 10000000 * 3; i++) {
			if (answerdis.readInt() != datadis.readInt()) {
				System.out.println("fail");
				System.exit(1);
			}
		}

		System.out.println((e - s) / 1000.0);
	}
}
