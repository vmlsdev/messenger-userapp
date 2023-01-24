/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 *
 */
public final class ServerRequestReceiver implements Runnable {

	// Server information.
	private final String serverIp;
	private final int serverIncomingPort;

	// Queue to put received requests into.
	private final BlockingQueue<Request> serverRequests;

	/**
	 * @param serverIp
	 * @param serverIncomingPort
	 * @param serverRequests
	 */
	public ServerRequestReceiver(String serverIp, int serverIncomingPort, BlockingQueue<Request> serverRequests) {
		this.serverIp = serverIp;
		this.serverIncomingPort = serverIncomingPort;
		this.serverRequests = serverRequests;
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		// Creating socket instance.
		Socket socket = null;
		try {
			socket = new Socket(serverIp, serverIncomingPort);
		} catch (UnknownHostException e) {
			// TODO
		} catch (IOException e) {
			// TODO
		}

		// Getting input stream from socket.
		InputStream in = null;
		try {
			in = socket.getInputStream();
		} catch (IOException e) {
			// TODO
		}

		// Receiving requests from server and putting them into queue.
		try {
			while (true) {
				serverRequests.put(Request.decode(in));
			}
		} catch (InterruptedException e) {
			// Normal termination.
		}
	}
}
