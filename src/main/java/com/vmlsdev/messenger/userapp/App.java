package com.vmlsdev.messenger.userapp;

import static com.vmlsdev.messenger.userapp.net.Request.Types.SHUTDOWN;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vmlsdev.messenger.userapp.gui.MessagePrinter;
import com.vmlsdev.messenger.userapp.gui.MessageReader;
import com.vmlsdev.messenger.userapp.net.IncomingRequestReceiver;
import com.vmlsdev.messenger.userapp.net.OutcomingRequestSender;
import com.vmlsdev.messenger.userapp.net.Request;

/**
 *
 */
public final class App {

	// Server information.
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_OUTCOMING_PORT = 2000;
	private static final int SERVER_INCOMING_PORT = 2000;

	// Limitations for the request queues.
	private static final int MAX_INCOMING_REQUESTS = 10;
	private static final int MAX_OUTCOMING_REQUESTS = 10;
	private static final int MAX_SERVICE_REQUESTS = 10;
	private static final int MAX_INCOMING_MESSAGES = 10;

	// The queues for service threads communication.
	private static final BlockingQueue<Request> incomingRequests = new ArrayBlockingQueue<>(MAX_INCOMING_REQUESTS);
	private static final BlockingQueue<Request> outcomingRequests = new ArrayBlockingQueue<>(MAX_OUTCOMING_REQUESTS);
	private static final BlockingQueue<Request> serviceRequests = new ArrayBlockingQueue<>(MAX_SERVICE_REQUESTS);
	private static final BlockingQueue<Request> incomingMessages = new ArrayBlockingQueue<>(MAX_INCOMING_MESSAGES);

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Starting service threads.
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new IncomingRequestReceiver(SERVER_IP, SERVER_OUTCOMING_PORT, incomingRequests));
		executor.execute(new OutcomingRequestSender(SERVER_IP, SERVER_INCOMING_PORT, outcomingRequests));
		executor.execute(new IncomingRequestHandler());
		executor.execute(new MessagePrinter());
		executor.execute(new MessageReader());

		// Handling service requests.
		try {
			while (true) {
				handleServiceRequest(serviceRequests.take());
			}
		} catch (InterruptedException ignore) {
			// Normal termination.
		}

		executor.shutdownNow();
	}

	/*
	 * Handles service requests.
	 */
	private static void handleServiceRequest(Request serviceRequest) {
		switch (serviceRequest.getType()) {
		case SHUTDOWN:
			Thread.currentThread().interrupt();
			break;
		}
	}
}
