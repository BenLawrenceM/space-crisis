package com.benlawrencem.game.spacecrisis.net.messages;

public class IdRequestMessage extends Message {
	@Override
	public String encode() {
		return Message.ID_REQUEST_PREFIX;
	}

	@Override
	public Message decode(String message) {
		return this;
	}

	@Override
	public Type getType() {
		return Message.Type.ID_REQUEST;
	}
}