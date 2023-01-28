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
public final class RequestSender implements Runnable {

	private static final int SO_TIMEOUT = 10000;

	private final String adresseeIp;
	private final int addresseePort;

	private final BlockingQueue<Request> requestsForSend;

	/**
	 * @param adresseeIp
	 * @param requestForSend
	 */
	public RequestSender(String adresseeIp, int addresseePort, BlockingQueue<Request> requestsForSend) {
		this.adresseeIp = adresseeIp;
		this.addresseePort = addresseePort;
		this.requestsForSend = requestsForSend;
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		Request request = null;

		while (!Thread.currentThread().isInterrupted()) {

			try (Socket socket = new Socket(adresseeIp, addresseePort)) {

				socket.setSoTimeout(SO_TIMEOUT);
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

				while (true) {

					if (request == null) {
						request = requestsForSend.take();
					}

					try {
						sendRequest(request, out);
						request = null;
					} catch (IOException e) {
						break;
					}
				}

			} catch (UnknownHostException e) {
				// TODO: Critical error.
			} catch (IOException ignore) {
				// Creating new socket.
			} catch (InterruptedException e) {
				// Normal termination.
				break;
			}
		}
	}

	private void sendRequest(Request request, DataOutputStream out) throws IOException {
		out.writeUTF(request.getMessage());
		out.flush();
	}
}
