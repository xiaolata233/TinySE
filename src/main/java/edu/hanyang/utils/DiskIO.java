package edu.hanyang.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.MutableTriple;

public class DiskIO {
	public static DataInputStream open_input_run(String filepath, int buffersize) throws FileNotFoundException {
		return new DataInputStream(
				new BufferedInputStream(
						new FileInputStream (filepath), 
						buffersize));
	}
	
	public static DataOutputStream open_output_run(String filepath, int buffersize) throws FileNotFoundException {
		return new DataOutputStream(
				new BufferedOutputStream(
						new FileOutputStream (filepath, true), 
						buffersize));
	}
	
	public static int read_array (DataInputStream in, int nelements, 
			ArrayList<MutableTriple<Integer, Integer, Integer>> arr) throws IOException {
		int cnt = 0, readint = 0;
		
		try {
			for ( ; cnt<nelements; cnt++) {
				arr.get(cnt).setLeft(in.readInt());
				readint++;
				arr.get(cnt).setMiddle(in.readInt());
				readint++;
				arr.get(cnt).setRight(in.readInt());
				readint++;
			}
		}
		catch (EOFException e) {
			assert(readint % 3 == 0);
		}
		return cnt;
	}
	
	public static void sort_arr (List<MutableTriple<Integer, Integer, Integer>> arr, int nelements) {
		Collections.sort(arr.subList(0, nelements));
	}
	
	public static void append_arr (DataOutputStream out, List<MutableTriple<Integer, Integer, Integer>> arr, int nelements) throws IOException {
		for (int i=0; i<nelements; i++) {
			out.writeInt(arr.get(i).getLeft());
			out.writeInt(arr.get(i).getMiddle());
			out.writeInt(arr.get(i).getRight());
		}
	}
	
	public static void append_tuple (DataOutputStream out, MutableTriple<Integer, Integer, Integer> t) throws IOException {
		out.writeInt(t.getLeft());
		out.writeInt(t.getMiddle());
		out.writeInt(t.getRight());
	}
}
