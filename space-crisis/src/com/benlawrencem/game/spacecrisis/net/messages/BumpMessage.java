package com.benlawrencem.game.spacecrisis.net.messages;

public class BumpMessage extends Message {
	private int entityId;
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public BumpMessage() {
		entityId = -1;
		startX = -1;
		startY = -1;
		endX = -1;
		endY = -1;
	}

	@Override
	public String encode() {
		return Message.BUMP_PREFIX + " " + entityId + " " + startX + " " + startY + " " + endX + " " + endY;
	}

	@Override
	public Message decode(String message) {
		String[] tokens = message.split(" ");
		entityId = Integer.parseInt(tokens[1]);
		startX = Integer.parseInt(tokens[2]);
		startY = Integer.parseInt(tokens[3]);
		endX = Integer.parseInt(tokens[4]);
		endY = Integer.parseInt(tokens[5]);
		return this;
	}

	@Override
	public Type getType() {
		return Message.Type.BUMP;
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

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}
}