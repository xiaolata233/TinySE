package edu.hanyang.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {
	public static int port = 9000;
	
	private HttpServer server;

	public void Start(int port, String dbname, String dbuser, String dbpass) throws Exception {
		try {
			SimpleHttpServer.port = port;
			server = HttpServer.create(new InetSocketAddress(port), 0);
			System.out.println("server started at " + port);
			server.createContext("/", new Handlers.EchoGetHandler(dbname, dbuser, dbpass));
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Stop() {
		server.stop(0);
		System.out.println("server stopped");
	}

	public static void main(String[] args) throws Exception {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( "P", "port", true, "Port number" );
		options.addOption( "d", "db", true, "Document db name" );
		options.addOption( "u", "user", true, "Document db user name" );
		options.addOption( "p", "pass", true, "Document db user password" );
			
		CommandLine line = parser.parse( options, args );
		if( line.hasOption("P") && 
				line.hasOption("d") && 
				line.hasOption( "u" ) && 
				line.hasOption("p") ) {
			
			// start http server
			SimpleHttpServer httpServer = new SimpleHttpServer();
			httpServer.Start(
					Integer.parseInt(line.getOptionValue("P")),
					line.getOptionValue("d"),
					line.getOptionValue("u"),
					line.getOptionValue("p")
					);
	    }
//		System.out.println(System.getProperty("user.dir"));
//		System.out.println(Main.class.getClassLoader().getResource("").getPath());
	}
}