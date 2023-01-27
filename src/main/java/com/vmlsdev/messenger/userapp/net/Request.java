/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import com.vmlsdev.messenger.userapp.LocalInfo;

/**
 * 
 */
public final class Request {

	private final byte type;
	private final int addresserId;
	private final int addresseeId;
	private final int token;
	private final long sendTime;
	private final int data;
	private final String message;

	/**
	 * @param type
	 * @param addresserId
	 * @param addresseeId
	 * @param token
	 * @param sendTime
	 * @param data
	 * @param message
	 */
	public Request(byte type, int addresserId, int addresseeId, int token, long sendTime, int data, String message) {
		this.type = type;
		this.addresserId = addresserId;
		this.addresseeId = addresseeId;
		this.token = LocalInfo.userToken;
		this.sendTime = sendTime;
		this.data = data;
		this.message = message;
	}

	/**
	 * @param type
	 * @param addresseeId
	 * @param data
	 * @param message
	 */
	public Request(byte type, int addresseeId, int data, String message) {
		this(type, LocalInfo.userId, addresseeId, LocalInfo.userToken, System.currentTimeMillis(), data, message);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the addresserId
	 */
	public int getAddresserId() {
		return addresserId;
	}

	/**
	 * @return the addresseeId
	 */
	public int getAddresseeId() {
		return addresseeId;
	}

	/**
	 * @return the token
	 */
	public int getToken() {
		return token;
	}

	/**
	 * @return the time
	 */
	public long getSendTime() {
		return sendTime;
	}

	/**
	 * @return the serviceData
	 */
	public int getData() {
		return data;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 */
	public interface Types {
		
		// For application initializing.
		byte USER_LOCAL_LISTENING_PORT = 0;
		
		
		// For user registration.
		
		
		
		// For transporting messages between users.
		
		
		
		
		byte SHUTDOWN = 127;
	}
}
