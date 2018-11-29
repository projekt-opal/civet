package org.dice_research.opal.civet.access;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * I/O utilities
 *
 * @author Adrian Wilke
 */
public abstract class IoUtils {

	/**
	 * @see https://stackoverflow.com/a/3584332
	 */
	public static boolean pingHost(String host, int port, int timeout) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), timeout);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}