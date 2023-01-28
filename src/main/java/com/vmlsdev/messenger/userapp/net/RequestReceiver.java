/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 *
 */
public final class RequestReceiver implements Runnable {

	private static final int SO_TIMEOUT = 10000;

	private final BlockingQueue<Request> receivedRequests;

	/**
	 * @param addresserIp
	 * @param receivedRequests
	 */
	public RequestReceiver(BlockingQueue<Request> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			try (ServerSocket serverSocket = new ServerSocket(0)) {

				serverSocket.setSoTimeout(SO_TIMEOUT);

				int listeningPort = serverSocket.getLocalPort();
				System.out.println("Your local listening port: " + listeningPort);

				while (!Thread.currentThread().isInterrupted()) {

					try (Socket socket = serverSocket.accept()) {

						DataInput in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

						while (true) {
							receivedRequests.put(receiveRequest(in));
						}

					} catch (IOException ignore) {
						// Creating new socket.
					}
				}

			} catch (IOException ignore) {
				// Creating new server socket.
			} catch (InterruptedException e) {
				// Normal termination.
				break;
			}
		}
	}

	/*
	 * 
	 */
	private Request receiveRequest(DataInput in) throws IOException {
		return new Request(in.readUTF());
	}
}
