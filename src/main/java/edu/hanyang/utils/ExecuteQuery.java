package edu.hanyang.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.hanyang.indexer.BPlusTree;
import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.DocumentCursor.LIST_TYPE;
import edu.hanyang.indexer.IntermediateList;
import edu.hanyang.indexer.QueryPlanTree;
import edu.hanyang.indexer.QueryPlanTree.NODE_TYPE;
import edu.hanyang.indexer.QueryPlanTree.QueryPlanNode;
import edu.hanyang.indexer.QueryProcess;

public class ExecuteQuery {
	final int BLOCK_SIZE = 52;

	String filepath = "../all-the-news/";
	String treeFilename = "bplustree.tree";
	String posListFilename = "PostingList.data";
	public BPlusTree tree = null;
	public InvidxStatAPI stat = null;

	private TreeMap<String, Integer> termids = new TreeMap<String, Integer>();

	public ExecuteQuery() throws Exception {
		// Class<?> cls = Class.forName("edu.hanyang.submit.TinySEBPlusTree");
		// tree = (BPlusTree) cls.newInstance();
		tree = new edu.hanyang.indexer.TinySEBPlusTree();
		tree.open("metapath", filepath + treeFilename, BLOCK_SIZE, 10);
		stat = new InvidxStatAPI(this);

		// load tree map
	}

	public void close() {
		// ...
	}

	// XXX: later --> web open
	public String translateQuery(String query) {
		// tokenize ->
		return null;
	}

	// qp:
	// query: e.g., "123 53994" 4939 23919 "49349 293 192"
	public DocumentCursor executeQuery(QueryProcess qp, String query) throws Exception {
		QueryPlanTree qptree = qp.parse_query(query, stat);
		return executeQuery(qp, qptree.root);
	}

	public DocumentCursor executeQuery(QueryProcess qp, QueryPlanNode node) throws Exception {
		if (node == null) {
			return null;
		}

		if (node.type == NODE_TYPE.OP_AND) {
			if (node.left == null || node.right == null) {
				throw new Exception("Invaild tree (a null child) : OP_AND is binary operation");
			}
			
			DocumentCursor left = executeQuery(qp, node.left);
			if (left.type == LIST_TYPE.POSLIST) {
				throw new Exception("Operation Error (list type mismatching) : left result is positional");
			}
			DocumentCursor right = executeQuery(qp, node.right);
			if (right.type == LIST_TYPE.POSLIST) {
				throw new Exception("Operation Error (list type mismatching) : right result is positional");
			}

			TestIntermediateList out = new TestIntermediateList();
			qp.op_and_wo_pos(left, right, out);
			TestDocCursor tdc = new TestDocCursor(out);
			return tdc;
		}

		else if (node.type == NODE_TYPE.OP_SHIFTED_AND) {
			if (node.left == null || node.right == null) {
				throw new Exception("Invaild tree (a null child) : OP_SHIFTED_AND is binary operation");
			}

			DocumentCursor left = executeQuery(qp, node.left);
			if (left.type == LIST_TYPE.NONPOSLIST) {
				throw new Exception("Operation Error (list type mismatching) : left result is non-positional");
			}

			DocumentCursor right = executeQuery(qp, node.right);
			if (right.type == LIST_TYPE.NONPOSLIST) {
				throw new Exception("Operation Error (list type mismatching) : right result is non-positional");
			}

			TestIntermediatePositionalList out = new TestIntermediatePositionalList();
			qp.op_and_w_pos(left, right, node.shift, out);
			return new TestDocCursor(out);
		}

		else if (node.type == NODE_TYPE.OP_REMOVE_POS) {
			QueryPlanNode child = null;
			if (node.left == null && node.right == null) {
				throw new IOException("Invaild tree (children is null) : OP_REMOVE_POS is unary operation");
			} else if (node.left == null) {
				child = node.right;
			} else {
				child = node.left;
			}

			if (child.type == NODE_TYPE.OPRAND) {
				if (child.termid < 0)
					throw new Exception("Invalid tree : termid is negative");
				return new InvidxDocCursor(child.termid, LIST_TYPE.NONPOSLIST, tree);
			} else if (child.type == NODE_TYPE.OP_SHIFTED_AND) {
				DocumentCursor left = executeQuery(qp, child);
				TestIntermediateList list = (TestIntermediateList) removePos(left);
				return new TestDocCursor(list);
			} else {
				throw new Exception("Invalid tree : child of op_remove_pos should be either oprand or op_shifted_and");
			}
		}

		else if (node.type == NODE_TYPE.OPRAND) {
			return new InvidxDocCursor(node.termid, LIST_TYPE.POSLIST, tree);
		}

		return null;
	}

	private static IntermediateList removePos(DocumentCursor cursor) throws IOException {
		IntermediateList list = new TestIntermediateList();
		while (!cursor.is_eol()) {
			list.put_docid(cursor.get_docid());
			cursor.go_next();
		}

		return list;
	}
}
