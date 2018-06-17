package edu.hanyang.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {
	public static int port = 9000;
	
	private HttpServer server;

	public void Start(int port) {
		try {
			SimpleHttpServer.port = port;
			server = HttpServer.create(new InetSocketAddress(port), 0);
			System.out.println("server started at " + port);
			//server.createContext("/", new Handlers.RootHandler());
			//server.createContext("/echoHeader", new Handlers.EchoHeaderHandler());
			server.createContext("/", new Handlers.EchoGetHandler());
			//server.createContext("/echoPost", new Handlers.EchoPostHandler());
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

	public static void main(String[] args) {
		// start http server
		SimpleHttpServer httpServer = new SimpleHttpServer();
		httpServer.Start(port);
		
//		System.out.println(System.getProperty("user.dir"));
//		System.out.println(Main.class.getClassLoader().getResource("").getPath());
	}
}