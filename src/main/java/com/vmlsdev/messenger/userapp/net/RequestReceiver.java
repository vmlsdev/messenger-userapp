/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import static com.vmlsdev.messenger.userapp.net.Request.Types.SEND_LOCAL_LISTENING_PORT;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import com.vmlsdev.messenger.userapp.LocalInfo;

/**
 *
 */
public final class RequestReceiver implements Runnable {

	private static final int SO_TIMEOUT = 10000;

	// Server address for security checks.
	private final String serverIp;

	private final BlockingQueue<Request> receivedRequests;
	private final BlockingQueue<Request> requestsForSend;

	/**
	 * @param serverIp
	 * @param receivedRequests
	 * @param requestsForSend
	 */
	public RequestReceiver(String serverIp, BlockingQueue<Request> receivedRequests,
			BlockingQueue<Request> requestsForSend) {
		this.serverIp = serverIp;
		this.receivedRequests = receivedRequests;
		this.requestsForSend = requestsForSend;
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			try (ServerSocket serverSocket = new ServerSocket(0)) {

				serverSocket.setSoTimeout(SO_TIMEOUT);

				LocalInfo.listeningPort = serverSocket.getLocalPort();
				requestsForSend.put(new Request(SEND_LOCAL_LISTENING_PORT, 0, LocalInfo.listeningPort, ""));

				while (!Thread.currentThread().isInterrupted()) {

					try (Socket socket = serverSocket.accept()) {

						if (!checkSecurity(socket.getInetAddress())) {
							continue;
						}

						DataInput in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

						while (true) {
							receivedRequests.put(receiveRequest(in));
						}

					} catch (IOException e) {
						// TODO
					}
				}

			} catch (IOException e) {
				// TODO
			} catch (InterruptedException ignore) {
				// Normal termination.
			}
		}
	}

	/*
	 * 
	 */
	private Request receiveRequest(DataInput in) throws IOException {
		return new Request(in.readByte(), in.readInt(), in.readInt(), in.readInt(), in.readLong(), in.readInt(),
				in.readUTF());
	}

	/*
	 * 
	 */
	private boolean checkSecurity(InetAddress ip) {
		return serverIp.equals(ip.getHostAddress());
	}
}
