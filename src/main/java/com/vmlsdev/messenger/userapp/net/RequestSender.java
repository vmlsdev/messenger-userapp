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

	private final String serverIp;
	private final int serverPort;

	private final BlockingQueue<Request> requestsForSend;

	/**
	 * @param serverIp
	 * @param requestForSend
	 */
	public RequestSender(String serverIp, int serverPort, BlockingQueue<Request> requestsForSend) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.requestsForSend = requestsForSend;
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		Request request = null;

		while (!Thread.currentThread().isInterrupted()) {

			try (Socket socket = new Socket(serverIp, serverPort)) {

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
		out.writeByte(request.getType());
		out.writeInt(request.getAddresserId());
		out.writeInt(request.getToken());
		out.writeLong(request.getSendTime());
		out.writeInt(request.getData());
		out.writeUTF(request.getMessage());
		out.flush();
	}
}
