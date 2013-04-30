package com.benlawrencem.game.spacecrisis.net.messages;

public class SpawnMessage extends Message {
	private int entityId;
	private int startX;
	private int startY;

	public SpawnMessage() {
		entityId = -1;
		startX = -1;
		startY = -1;
	}

	@Override
	public String encode() {
		return Message.SPAWN_PREFIX + " " + entityId + " " + startX + " " + startY;
	}

	@Override
	public Message decode(String message) {
		String[] tokens = message.split(" ");
		entityId = Integer.parseInt(tokens[1]);
		startX = Integer.parseInt(tokens[2]);
		startY = Integer.parseInt(tokens[3]);
		return this;
	}

	@Override
	public Type getType() {
		return Message.Type.SPAWN;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}
}