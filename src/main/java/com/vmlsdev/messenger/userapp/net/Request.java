/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

/**
 * 
 */
public final class Request {

	private final String message;

	/**
	 * @param message
	 */
	public Request(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
