/**
 * 
 */
package com.vmlsdev.messenger.userapp.net;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 
 */
public final class Request {

	private final int addresserId;
	private final int addresseeId;
	private final long time;
	private final byte type;
	private final int hash;
	private final String message;

	/**
	 * @param addresserId
	 * @param addresseeId
	 * @param type
	 * @param hash
	 * @param message
	 */
	public Request(int addresserId, int addresseeId, long time, byte type, int hash, String message) {
		this.addresserId = addresserId;
		this.addresseeId = addresseeId;
		this.time = time;
		this.type = type;
		this.hash = hash;
		this.message = message;
	}

	/**
	 * 
	 * @param request
	 * @param out
	 */
	public static void encode(Request request, DataOutput out) throws IOException {
		out.writeInt(request.getAddresserId());
		out.writeInt(request.getAddresseeId());
		out.writeLong(request.getTime());
		out.writeByte(request.getType());
		out.writeInt(request.getHash());
		out.writeUTF(request.getMessage());
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	public static Request decode(DataInput in) throws IOException {
		return new Request(in.readInt(), in.readInt(), in.readLong(), in.readByte(), in.readInt(), in.readUTF());
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
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the serviceData
	 */
	public int getHash() {
		return hash;
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
		byte SHUTDOWN = 127;
	}
}
