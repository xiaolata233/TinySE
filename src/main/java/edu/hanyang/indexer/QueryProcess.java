package edu.hanyang.indexer;

import java.io.IOException;

public interface QueryProcess {
	public void op_and_wo_pos (InvertedFile op1, InvertedFile op2, InvertedFile out) throws IOException;
	public void op_and_w_pos (InvertedFile op1, InvertedFile op2, InvertedFile out) throws IOException;
	public QueryPlanTree parse_query(String query);
}
