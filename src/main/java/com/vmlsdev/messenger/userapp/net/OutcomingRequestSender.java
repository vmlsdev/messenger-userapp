/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

/**
 *
 */
public final class OutcomingRequestSender implements Runnable {

	// Addressee information.
	private final String addresseeIp;
	private final int addresseePort;

	// The queue for requests to encode.
	private final BlockingQueue<Request> outcomingRequests;

	/**
	 * @param addresseeIp
	 * @param addresseePort
	 * @param outcomingRequests
	 */
	public OutcomingRequestSender(String addresseeIp, int addresseePort, BlockingQueue<Request> outcomingRequests) {
		this.addresseeIp = addresseeIp;
		this.addresseePort = addresseePort;
		this.outcomingRequests = outcomingRequests;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		// Connecting socket to the addressee.
		Socket socket = null;
		try {
			socket = new Socket(addresseeIp, addresseePort);
		} catch (UnknownHostException e) {
			// TODO
		} catch (IOException e) {
			// TODO
		}

		// Creating output stream towards the addressee.
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO
		}

		// Taking requests from the queue and sending them to the addressee.
		try {
			while (true) {
				Request.encode(outcomingRequests.take(), out);
			}
		} catch (InterruptedException ignore) {
			// Normal termination.
		} catch (IOException e) {
			// TODO
		}
	}

}
