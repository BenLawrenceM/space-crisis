package com.benlawrencem.game.spacecrisis.net;

import com.benlawrencem.game.spacecrisis.net.messages.Message;
import com.benlawrencem.net.nightingale.ClientConnection;
import com.benlawrencem.net.nightingale.ClientConnection.CouldNotConnectException;
import com.benlawrencem.net.nightingale.ClientConnectionListener;
import com.benlawrencem.net.nightingale.Packet.CouldNotSendPacketException;

public class PlayerMovementClient implements ClientConnectionListener {
	private ClientConnection conn;

	public PlayerMovementClient() {
		conn = new ClientConnection(this);
	}

	public void connect() {
		System.out.println("Attempting to connect to server...");
		try {
			conn.connect("1.1.1.1", 9876);
		} catch (CouldNotConnectException e) {
			System.out.println("Could not connect: " + e.getMessage());
		}
	}

	public void disconnect() {
		conn.disconnect();
	}

	public long getLatency() {
		return conn.getLatency();
	}

	public void send(Message msg) {
		System.out.println("Sneding message: \"" + msg.encode() + "\"");
		try {
			conn.send(msg.encode());
		} catch (CouldNotSendPacketException e) {
			System.out.println("Could not send message: " + e.getMessage());
		}
	}

	@Override
	public void onConnected() {
		System.out.println("Connected!");
		send(Message.createIdRequestMessage());
	}

	@Override
	public void onCouldNotConnect(String reason) {
		System.out.println("Could not connect: " +reason);
	}

	@Override
	public void onDisconnected(String reason) {
		System.out.println("Disconnected: " +reason);
	}

	@Override
	public void onReceive(String message) {
		System.out.println("Received message: \"" + message + "\"");
		Message msg = Message.parse(message);
		switch(msg.getType()) {
			case ID_RESPONSE:
				//TODO handle id
				break;
			case SPAWN:
				//TODO spawn entity
				break;
			case DESPAWN:
				//TODO despawn entity
				break;
			case MOVE:
				//TODO move entity
				break;
			case BUMP:
				//TODO bump entity back
				break;
			case SYNC:
				//TODO sync entity
				break;
		}
	}

	@Override
	public void onMessageNotDelivered(int messageId, int resendMessageId, String message) {
		System.out.println("Message not delivered: \"" + message + "\" Resending...");
		try {
			conn.resend(resendMessageId, message);
		} catch (CouldNotSendPacketException e) {
			System.out.println("Message could not be resent: " + e.getMessage());
		}
	}
}