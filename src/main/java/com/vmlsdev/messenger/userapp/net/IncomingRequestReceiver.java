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
		try (Socket socket = new Socket(addresserIp, addresserPort)) {
			
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			
			while (true) {
				incomingRequests.put(Request.decode(in));
			}
			
		} catch (UnknownHostException e) {
			// TODO
		} catch (InterruptedException ignore) {
			// Normal termination.
		} catch (IOException e) {
			// TODO
		}
	}
}
