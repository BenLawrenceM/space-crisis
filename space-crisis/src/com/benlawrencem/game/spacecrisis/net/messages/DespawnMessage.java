package com.benlawrencem.game.spacecrisis.net.messages;

public class DespawnMessage extends Message {
	private int entityId;

	public DespawnMessage() {
		entityId = -1;
	}

	@Override
	public String encode() {
		return Message.DESPAWN_PREFIX + " " + entityId;
	}

	@Override
	public Message decode(String message) {
		String[] tokens = message.split(" ");
		entityId = Integer.parseInt(tokens[1]);
		return this;
	}

	@Override
	public Type getType() {
		return Message.Type.DESPAWN;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
}