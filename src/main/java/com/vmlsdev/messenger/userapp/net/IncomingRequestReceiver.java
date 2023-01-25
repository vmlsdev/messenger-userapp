/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 *
 */
public final class IncomingRequestReceiver implements Runnable {

	// Addresser information.
	private final String addresserIp;
	private final int addresserPort;

	// The queue for decoded requests.
	private final BlockingQueue<Request> incomingRequests;

	/**
	 * @param addresserIp
	 * @param addresserPort
	 * @param incomingRequests
	 */
	public IncomingRequestReceiver(String addresserIp, int addresserPort, BlockingQueue<Request> incomingRequests) {
		this.addresserIp = addresserIp;
		this.addresserPort = addresserPort;
		this.incomingRequests = incomingRequests;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		// Connecting socket to the addresser.
		Socket socket = null;
		try {
			socket = new Socket(addresserIp, addresserPort);
		} catch (UnknownHostException e) {
			// TODO
		} catch (IOException e) {
			// TODO
		}

		// Creating input stream out of the addresser.
		DataInputStream in = null;
		try {
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			// TODO
		}

		// Receiving requests from the addresser and putting them into the queue.
		try {
			while (true) {
				incomingRequests.put(Request.decode(in));
			}
		} catch (InterruptedException ignore) {
			// Normal termination.
		} catch (IOException e) {
			// TODO
		}
	}
}
