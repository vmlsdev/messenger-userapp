package com.lukesukhanov.messenger.userapp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lukesukhanov.messenger.userapp.gui.MessagePrinter;
import com.lukesukhanov.messenger.userapp.gui.MessageReader;
import com.lukesukhanov.messenger.userapp.net.ClientRequestSender;
import com.lukesukhanov.messenger.userapp.net.Request;
import com.lukesukhanov.messenger.userapp.net.ServerRequestReceiver;

/**
 *
 * @author Luke Sukhanov
 */
public final class App {
	
	// Service queues limitations.
	private static final int MAX_SERVER_REQUESTS = 10;
	private static final int MAX_CLIENT_REQUESTS = 10;
	private static final int MAX_SERVICE_REQUESTS = 10;
	private static final int MAX_INCOMING_MESSAGES = 10;
	
	// Queues for service threads communication.
	private static final BlockingQueue<Request> serverRequests = new ArrayBlockingQueue(MAX_SERVER_REQUESTS);
	private static final BlockingQueue<Request> clientRequests = new ArrayBlockingQueue(MAX_CLIENT_REQUESTS);
	private static final BlockingQueue<Request> serviceRequests = new ArrayBlockingQueue(MAX_SERVER_REQUESTS);
	private static final BlockingQueue<Request> incomingMessages = new ArrayBlockingQueue(MAX_INCOMING_MESSAGES);
	
	// Server information.
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_INCOMING_PORT = 2000;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Starting service threads.
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new ServerRequestReceiver(SERVER_IP, SERVER_INCOMING_PORT, serviceRequests));
		executor.execute(new ClientRequestSender());
		executor.execute(new ServerRequestHandler());
		executor.execute(new MessagePrinter());
		executor.execute(new MessageReader());
		
		// Handling service requests.
		try {
			while (!Thread.interrupted()) {
				handleServiceRequest(serviceRequests.take());
			}
		} catch (InterruptedException e) {
			// Abnormal termination.
		}
		
		executor.shutdownNow();
	}
	
	/* 
	 * Handles requests from serviceRequests queue.
	 * Supported request types: SHUTDOWN.
	 */
	private static void handleServiceRequest(Request serviceRequest) {
		
		switch (serviceRequest.getType()) {
			case SHUTDOWN :
				Thread.currentThread().interrupt();
				break;
		}
	}
}
