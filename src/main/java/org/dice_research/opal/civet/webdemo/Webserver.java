package org.dice_research.opal.civet.webdemo;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpServer;

/**
 * Civet web server.
 * 
 * Dev note: On Eclipse com.sun.net.httpserver problems add access rule in
 * project libraries: com/sun/net/httpserver/**
 * https://stackoverflow.com/a/25945740
 *
 * @author Adrian Wilke
 */
public class Webserver {

	protected static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Main entry point
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length == 0) {
			System.err.println("Please provide a port for the webserver");
			System.exit(1);
		}

		int port = Integer.parseInt(args[0]);
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

		server.createContext("/", new StartHandler());
		server.createContext("/datasets", new DatasetHandler());
		server.createContext("/civet", new CivetHandler());
		server.start();

		LOGGER.info("Webserver startet at port " + port);
	}

}