package com.benlawrencem.game.spacecrisis.net;

import com.benlawrencem.game.spacecrisis.net.messages.Message;
import com.benlawrencem.net.nightingale.Packet.CouldNotSendPacketException;
import com.benlawrencem.net.nightingale.Server;
import com.benlawrencem.net.nightingale.Server.CouldNotStartServerException;
import com.benlawrencem.net.nightingale.ServerListener;

public class PlayerMovementServer implements ServerListener {
	private static PlayerMovementServer instance = null;

	public static PlayerMovementServer getInstance() {
		if(instance == null)
			instance = new PlayerMovementServer();
		return instance;
	}

	private Server server;

	public PlayerMovementServer() {
		server = new Server(this);
	}

	public void startServer() {
		System.out.println("Starting server...");
		try {
			server.startServer(9876);
			System.out.println("Server started!");
		} catch (CouldNotStartServerException e) {
			System.out.println("Server could not be started: " + e.getMessage());
		}
	}

	public void stopServer() {
		server.stopServer();
	}

	public boolean isRunning() {
		return server.isRunning();
	}

	public void send(int clientId, Message msg) {
		System.out.println("Sending message to client " + clientId + ": \"" + msg.encode() + "\"");
		try {
			server.send(clientId, msg.encode());
		} catch (CouldNotSendPacketException e) {
			System.out.println("Could not send message to client " + clientId + ": " + e.getMessage());
		}
	}

	@Override
	public void onServerStopped() {
		System.out.println("Server stopped!");
	}

	@Override
	public boolean onClientConnected(int clientId, String address, int port) {
		System.out.println("Client " + clientId + " connected from " + address + ":" + port);
		return true;
	}

	@Override
	public void onClientDisconnected(int clientId, String reason) {
		System.out.println("Client " + clientId + " disconnected: " + reason);
		//TODO send despawn message
	}

	@Override
	public void onReceive(int clientId, String message) {
		System.out.println("Received message from client " + clientId + ": \"" + message + "\"");
		Message msg = Message.parse(message);
		switch(msg.getType()) {
			case ID_REQUEST:
				//TODO send id response, send spawn messages to client, send spawn message to all clients
				break;
			case MOVE:
				//TODO decide whether to move, bump, or sync
				break;
		}
	}

	@Override
	public void onMessageNotDelivered(int messageId, int resendMessageId, int clientId, String message) {
		System.out.println("Message not delivered to client " + clientId + ": \"" + message + "\" Resending...");
		try {
			server.resend(clientId, resendMessageId, message);
		} catch (CouldNotSendPacketException e) {
			System.out.println("Message could not be resent: " + e.getMessage());
		}
	}
}