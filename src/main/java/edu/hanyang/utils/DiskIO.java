package edu.hanyang.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	
	public static void read_array (DataInputStream in, 
			long offset, int nelements, 
			ArrayList<MutableTriple<Integer, Integer, Integer>> arr) throws IOException {
		for (int i=0; i<nelements; i++) {
			arr.get(i).setLeft(in.readInt());
			arr.get(i).setMiddle(in.readInt());
			arr.get(i).setRight(in.readInt());
		}
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
}
