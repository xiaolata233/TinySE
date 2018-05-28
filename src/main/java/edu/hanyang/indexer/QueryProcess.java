package edu.hanyang.indexer;

import java.io.IOException;

public interface QueryProcess {
	public void op_and_wo_pos (DocumentCursor op1, DocumentCursor op2, IntermediateList out) throws IOException;
	public void op_and_w_pos (DocumentCursor op1, DocumentCursor op2, int shift, IntermediateList out) throws IOException;
	public QueryPlanTree parse_query(String query);
}
