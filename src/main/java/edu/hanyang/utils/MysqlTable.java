package edu.hanyang.utils;

import java.sql.*;

public class MysqlTable {
	private static final String dbhost = "localhost";

    private static Connection conn = null;
    
    private static PreparedStatement get_doc_stmt = null;
    
	public static void init_conn(String dbname, String dbuser, String dbpass) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);

		// prepare statements
		get_doc_stmt = conn.prepareStatement("select txt from docss where id=?");
	}
	
	public static void final_conn () throws Exception {
		conn.close();
	}
	
	public static String get_doc(long id) {
		String res = null;
		try {
			get_doc_stmt.setLong(1, id);
			ResultSet rs = get_doc_stmt.executeQuery();
			rs.next();
			res = rs.getString("txt");
			rs.close();
		} catch (SQLException e) {
			System.err.println("[db error] get_message: " + e.getMessage());
		}
		
		return res;
	}
}

