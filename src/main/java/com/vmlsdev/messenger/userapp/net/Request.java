/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 */
public final class Request {

	private final Type type;
	private final Status status;
	private final int addressantId;
	private final String addressantName;
	private final int addresseeId;
	private final int serviceData;
	private final String message;
	
	/**
	 * @param type
	 * @param status
	 * @param addressantId
	 * @param addressantName
	 * @param addresseeId
	 * @param serviceData
	 * @param message
	 */
	public Request(Type type, Status status, int addressantId, String addressantName, int addresseeId, int serviceData,
			String message) {
		this.type = type;
		this.status = status;
		this.addressantId = addressantId;
		this.addressantName = addressantName;
		this.addresseeId = addresseeId;
		this.serviceData = serviceData;
		this.message = message;
	}

	public static void encode(Request request, OutputStream out) {

	}

	public static Request decode(InputStream in) {
		return null;
	}
	
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the addressantId
	 */
	public int getAddressantId() {
		return addressantId;
	}

	/**
	 * @return the addressantName
	 */
	public String getAddressantName() {
		return addressantName;
	}

	/**
	 * @return the addresseeId
	 */
	public int getAddresseeId() {
		return addresseeId;
	}

	/**
	 * @return the serviceData
	 */
	public int getServiceData() {
		return serviceData;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	public enum Type { REGISTRATION, ACTIVIZATION, FRIENDSHIP, MESSAGE, SHUTDOWN }
	
	public enum Status { ACCEPTED, DECLINED, INCOMING }
	
}
