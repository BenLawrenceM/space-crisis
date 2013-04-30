package com.benlawrencem.game.spacecrisis.net.messages;

public class IdResponseMessage extends Message {
	private int id;

	public IdResponseMessage() {
		id = -1;
	}

	@Override
	public String encode() {
		return Message.ID_RESPONSE_PREFIX + " " + id;
	}

	@Override
	public Message decode(String message) {
		String[] tokens = message.split(" ");
		id = Integer.parseInt(tokens[1]);
		return this;
	}

	@Override
	public Type getType() {
		return Message.Type.ID_RESPONSE;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}