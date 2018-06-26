package edu.hanyang.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import edu.hanyang.indexer.DocumentCursor;
import edu.hanyang.indexer.QueryProcess;
import edu.hanyang.utils.ExecuteQuery;
import edu.hanyang.utils.MysqlTable;

public class Handlers {

	public static class EchoGetHandler implements HttpHandler {
		private ExecuteQuery eq = null;
		private QueryProcess qp = null;
		
		public EchoGetHandler (String dbname, String dbuser, String dbpass) throws Exception {
			// connect db
			MysqlTable.init_conn(dbname, dbuser, dbpass);
			
			// create query processor
			eq = new ExecuteQuery();
			
			// load query processor class
			Class<?> cls = Class.forName("edu.hanyang.submit.TinySEQueryProcess");
			qp = (QueryProcess) cls.newInstance();
		}

		@Override
		public void handle(HttpExchange he) throws IOException {
			// parse request
			Map<String, String> parameters = new HashMap<String, String>();
			URI requestedUri = he.getRequestURI();
			String query = requestedUri.getRawQuery();
			he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
			
			if (! parseQuery(query, parameters)) {
				he.sendResponseHeaders(400,-1);
				he.close();
				return;
			}
			
//			String response = "";
			
			JSONObject responseJSON = new JSONObject();
			JSONObject dataJSON = new JSONObject();
			
			System.out.println(parameters.get("query"));
			
			// process query
			DocumentCursor list;
			long start, end;
			try {
				start = System.currentTimeMillis();
				String newQuery = eq.translateQuery(parameters.get("query"));
				System.out.println(newQuery);
				list = eq.executeQuery(qp, newQuery);
				end = System.currentTimeMillis();
			} catch (Exception e) {
				he.sendResponseHeaders(500,-1);
				he.close();
				return;
			}
			
	        JSONArray list = new JSONArray();
	        list.add("msg 1");
	        list.add("msg 2");
	        list.add("msg 3");
			
			// send response
			List<String> docList = new ArrayList<>();
			List<Integer> docID = new ArrayList<>();
			while (! list.is_eol()) {
				int docid = list.get_docid();
				docID.add(docid);
				String txt = MysqlTable.get_doc(docid);
				
				JSONObject obj = new JSONObject();
		        obj.put("name", "mkyong.com");
		        obj.put("age", new Integer(100));
				
				docList.add(txt);
				list.go_next();
			}
			
			dataJSON.put("time", (end - start)/1000.0);
			dataJSON.put("nDoc", docList.size());
			responseJSON.put("info", dataJSON.toJSONString());
			for (int i =0;i<docList.size();i++) {
//				response += key + " = " + parameters.get(key) + "\n";
				responseJSON.put(docList.get(i), docID.get(i));
			}
			
			he.sendResponseHeaders(200, responseJSON.toJSONString().length());
			OutputStream os = he.getResponseBody();
			
			os.write(responseJSON.toJSONString().getBytes());
			os.close();
		}

	}

	@SuppressWarnings("unchecked")
	public static boolean parseQuery(String query, Map<String, String> parameters) {

		if (query != null) {
			String pairs[] = query.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				if (param.length != 2) {
					return false;
				}

				String key = null;
				String value = null;
				
				try {
					key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
					value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				} catch (UnsupportedEncodingException e) {
					return false;
				}

				parameters.put(key, value);
			}
		}
		
		return true;
	}
}