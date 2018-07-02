package edu.hanyang.httpserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		public EchoGetHandler(String dbname, String dbuser, String dbpass) throws Exception {
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
			System.out.println("Local: " + he.getLocalAddress());
			System.out.println("Remore: " + he.getRemoteAddress());
			String query = requestedUri.getRawQuery();
			he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

			System.out.println("row query: " + query);

			if (!parseQuery(query, parameters)) {
				he.sendResponseHeaders(400, -1);
				he.close();
				return;
			}

			//			String response = "";

			JSONObject responseJSON = new JSONObject();
			JSONObject dataJSON = new JSONObject();
			System.out.println("Query: ");
			System.out.println(parameters.get("query"));

			// process query
			DocumentCursor list = null;
			long start, end;
			try {
				String newQuery = eq.translateQuery(parameters.get("query"));
				System.out.println(newQuery);
				start = System.currentTimeMillis();
				list = eq.executeQuery(qp, newQuery);
				end = System.currentTimeMillis();
				dataJSON.put("time", (end - start) / 1000.0);
				dataJSON.put("nDoc", list.get_doc_count());
			} catch (IOException e) {
				dataJSON.put("time", 0.0);
				dataJSON.put("nDoc", 0);
			} catch (Exception e) {
				he.sendResponseHeaders(500, -1);
				he.close();
				return;
			}

			// send response
			List<String> docList = new ArrayList<>();
			List<Integer> docID = new ArrayList<>();
			if (list != null) {
				while (!list.is_eol()) {
					int docid = list.get_docid();
					docID.add(docid);
					String txt = MysqlTable.get_doc(docid + 1);
					docList.add(txt);
					if (docList.size() == 10) {
						break;
					}
					list.go_next();
				}
			}
			responseJSON.put("info", dataJSON.toJSONString());

			for (int i = 0; i < docList.size(); i++) {
				//				response += key + " = " + parameters.get(key) + "\n";
				responseJSON.put(docID.get(i), docList.get(i));
			}

			he.sendResponseHeaders(200, 0);
			try (BufferedOutputStream os = new BufferedOutputStream(he.getResponseBody())) {
	            try (ByteArrayInputStream bis = new ByteArrayInputStream(responseJSON.toJSONString().getBytes())) {
	                byte [] buffer = new byte [4096];
	                int count ;
	                while ((count = bis.read(buffer)) != -1) {
	                    os.write(buffer, 0, count);
	                }
	            }
	        }
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