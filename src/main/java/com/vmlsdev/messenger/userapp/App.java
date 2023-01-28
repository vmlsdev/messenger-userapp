package com.vmlsdev.messenger.userapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vmlsdev.messenger.userapp.gui.MessagePrinter;
import com.vmlsdev.messenger.userapp.net.Request;
import com.vmlsdev.messenger.userapp.net.RequestReceiver;
import com.vmlsdev.messenger.userapp.net.RequestSender;

/**
 *
 */
public final class App {

	// Limitations for the request queues.
	private static final int MAX_RECEIVED_REQUESTS = 10;
	private static final int MAX_REQUESTS_FOR_SEND = 10;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		String publicIp = askForPublicIp();
		if (publicIp == null) {
			System.out.println("Failed to get your public IP address.");
			return;
		}
		*/
		
		String publicIp = "127.0.0.1";

		System.out.println("Your public IP address: " + publicIp);

		// The queues for service threads communication.
		BlockingQueue<Request> receivedRequests = new ArrayBlockingQueue<>(MAX_RECEIVED_REQUESTS);
		BlockingQueue<Request> requestsForSend = new ArrayBlockingQueue<>(MAX_REQUESTS_FOR_SEND);

		// Starting receiver.
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new RequestReceiver(receivedRequests));

		// Giving receiver time to print local listening port.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignore) {
			// Unlikely.
		}

		// Reading addressee information from console.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String adresseeIp = askForAddresseeIp(in);
		int adresseePort = askForAddresseePort(in);

		// Starting sender and printer.
		executor.execute(new RequestSender(adresseeIp, adresseePort, requestsForSend));
		executor.execute(new MessagePrinter(receivedRequests));

		// Reading messages from console until "exit".
		String message;
		while (true) {
			try {
				message = in.readLine();
			} catch (IOException e) {
				// Retrying.
				continue;
			}

			if (message.equalsIgnoreCase("exit")) {
				// Normal termination.
				break;
			}

			try {
				requestsForSend.put(new Request(message));
			} catch (InterruptedException ignore) {
				// Unlikely.
			}
		}

		executor.shutdownNow();
	}

	/*
	 * 
	 */
	private static String askForPublicIp() {

		String publicIp = null;
		URL url = null;

		try {
			url = new URL("http://checkip.amazonaws.com/");
		} catch (MalformedURLException ignore) {
			// Just ignoring.
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
			publicIp = in.readLine();
		} catch (IOException ignore) {
			// Just ignoring.
		}

		return publicIp;
	}

	/*
	 * 
	 */
	private static String askForAddresseeIp(BufferedReader in) {

		String adresseeIp = null;

		while (adresseeIp == null) {
			try {
				System.out.print("Adressee ip: ");
				adresseeIp = in.readLine();
			} catch (IOException ignore) {
				// Retrying.
			}
		}

		return adresseeIp;
	}

	/*
	 * 
	 */
	private static int askForAddresseePort(BufferedReader in) {

		int adresseePort = -1;

		while (adresseePort == -1) {
			try {
				System.out.print("Adressee port: ");
				adresseePort = Integer.parseInt(in.readLine());
			} catch (NumberFormatException ignore) {
				// Retrying.
			} catch (IOException ignore) {
				// Retrying.
			}
		}

		return adresseePort;
	}
}
