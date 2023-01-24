/**
 * 
 */
package com.lukesukhanov.messenger.userapp.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Luke Sukhanov
 */
public final class ServerRequestReceiver implements Runnable {

	// Pause period while getting input stream from server.
	private static final int SLEEPING_TIME_MILLIS = 1000;

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

	/*
	 * Attempts to get input stream from socket.
	 */
	private static InputStream getInputStreamFrom(Socket socket) throws InterruptedException {

		while (!Thread.interrupted()) {
			try {
				return socket.getInputStream();
			} catch (IOException e) {
				Thread.sleep(SLEEPING_TIME_MILLIS);
			}
		}
		throw new InterruptedException();
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
		InputStream in;
		try {
			in = getInputStreamFrom(socket);
		} catch (InterruptedException e) {
			return; // Normal termination.
		}

		// Receiving requests from server and putting them into queue.
		try {
			while (!Thread.interrupted()) {
				serverRequests.put(Request.decode(in));
			}
		} catch (InterruptedException e) {
			// Normal termination.
		}
	}
}
