package com.benlawrencem.game.spacecrisis.net.messages;

public abstract class Message {
	public static final String ID_REQUEST_PREFIX = "ID?";
	public static final String ID_RESPONSE_PREFIX = "ID";
	public static final String SPAWN_PREFIX = "SPAWN";
	public static final String DESPAWN_PREFIX = "DESPAWN";
	public static final String MOVE_PREFIX = "MOVE";
	public static final String BUMP_PREFIX = "BUMP";
	public static final String SYNC_PREFIX = "SYNC";
	public static enum Type { ID_REQUEST, ID_RESPONSE, SPAWN, DESPAWN, MOVE, BUMP, SYNC, UNKNOWN };

	public static Type parseType(String message) {
		String prefix = message.split(" ")[0];
		if(prefix.equals(Message.ID_REQUEST_PREFIX))
			return Type.ID_REQUEST;
		if(prefix.equals(Message.ID_RESPONSE_PREFIX))
			return Type.ID_RESPONSE;
		if(prefix.equals(Message.SPAWN_PREFIX))
			return Type.SPAWN;
		if(prefix.equals(Message.DESPAWN_PREFIX))
			return Type.DESPAWN;
		if(prefix.equals(Message.MOVE_PREFIX))
			return Type.MOVE;
		if(prefix.equals(Message.BUMP_PREFIX))
			return Type.BUMP;
		if(prefix.equals(Message.SYNC_PREFIX))
			return Type.SYNC;
		return Type.UNKNOWN;
	}

	public static Message parse(String message) {
		switch(Message.parseType(message)) {
			case ID_REQUEST:
				return new IdRequestMessage().decode(message);
			case ID_RESPONSE:
				return new IdResponseMessage().decode(message);
			case SPAWN:
				return new SpawnMessage().decode(message);
			case DESPAWN:
				return new DespawnMessage().decode(message);
			case MOVE:
				return new MoveMessage().decode(message);
			case BUMP:
				return new BumpMessage().decode(message);
			case SYNC:
				return new SyncMessage().decode(message);
			default:
				return null;
		}
	}

	public static IdRequestMessage createIdRequestMessage() {
		return new IdRequestMessage();
	}

	public static IdResponseMessage createIdResponseMessage(int id) {
		IdResponseMessage msg = new IdResponseMessage();
		msg.setId(id);
		return msg;
	}

	public static SpawnMessage createSpawnMessage(int entityId, int startX, int startY) {
		SpawnMessage msg = new SpawnMessage();
		msg.setEntityId(entityId);
		msg.setStartX(startX);
		msg.setStartY(startY);
		return msg;
	}

	public static DespawnMessage createDespawnMessage(int entityId) {
		DespawnMessage msg = new DespawnMessage();
		msg.setEntityId(entityId);
		return msg;
	}

	public static MoveMessage createMoveMessage(int entityId, int startX, int startY, int endX, int endY) {
		MoveMessage msg = new MoveMessage();
		msg.setEntityId(entityId);
		msg.setStartX(startX);
		msg.setStartY(startY);
		msg.setEndX(endX);
		msg.setEndY(endY);
		return msg;
	}

	public static BumpMessage createBumpMessage(int entityId, int startX, int startY, int endX, int endY) {
		BumpMessage msg = new BumpMessage();
		msg.setEntityId(entityId);
		msg.setStartX(startX);
		msg.setStartY(startY);
		msg.setEndX(endX);
		msg.setEndY(endY);
		return msg;
	}

	public static SyncMessage createSyncMessage(int entityId, int startX, int startY, int endX, int endY) {
		SyncMessage msg = new SyncMessage();
		msg.setEntityId(entityId);
		msg.setStartX(startX);
		msg.setStartY(startY);
		msg.setEndX(endX);
		msg.setEndY(endY);
		return msg;
	}

	public Message() {}

	public Message(String message) {
		decode(message);
	}

	public abstract String encode();

	public abstract Message decode(String message);

	public abstract Type getType();
}