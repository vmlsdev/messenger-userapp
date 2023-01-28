/**
 * 
 */
package com.vmlsdev.messenger.userapp.gui;

import java.util.concurrent.BlockingQueue;

import com.vmlsdev.messenger.userapp.net.Request;

/**
 *
 */
public final class MessagePrinter implements Runnable {

	private final BlockingQueue<Request> receivedRequests;

	/**
	 * @param receivedRequests
	 */
	public MessagePrinter(BlockingQueue<Request> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			while (true) {
				System.out.println(receivedRequests.take().getMessage());
			}
		} catch (InterruptedException ignore) {
			// Normal termination.
		}
	}

}
