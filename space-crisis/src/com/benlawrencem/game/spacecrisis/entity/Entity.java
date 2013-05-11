package com.benlawrencem.game.spacecrisis.entity;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public abstract class Entity {
	private enum Action { MOVE, MOVE_COMMITTED, MOVE_CANCELED, MOVE_CANCELED_RECOVERING, NONE };

	private TileLevel level;
	private Tile tile;
	private Direction facingDirection;
	private Direction moveDirection;
	private Direction queuedMoveDirection;
	private Direction[] queuedMoveDirectionPreferences;
	private Tile moveToTile;
	private float moveSpeedTilesPerSecond;
	private Action currAction;
	private Action queuedAction;
	private boolean willCancelMove;
	private int timeSpentCompletingCurrentAction;
	private int timeNeededToCompleteCurrentAction;
	private int timeNeededToCommitMove;
	private int timeNeededToCompleteMove;
	private int timeNeededToCancelMove;
	private int timeNeededToRecoverFromCanceledMove;

	public Entity(TileLevel level, Tile startingTile) {
		this.level = level;
		tile = startingTile;
		facingDirection = Direction.SOUTH;
		moveDirection = Direction.NONE;
		moveToTile = null;
		setMoveSpeed(2);
		currAction = Action.NONE;
		queuedAction = Action.NONE;
		queuedMoveDirectionPreferences = new Direction[4];
		for(int i = 0; i < queuedMoveDirectionPreferences.length; i++)
			queuedMoveDirectionPreferences[i] = null;
		timeSpentCompletingCurrentAction = 0;
		timeNeededToCompleteCurrentAction = 0;
		timeNeededToRecoverFromCanceledMove = 200;
	}

	public float getX() {
		float x = tile.getX();
		if(currAction == Action.MOVE || currAction == Action.MOVE_CANCELED || currAction == Action.MOVE_COMMITTED) {
			float percentComplete = (1.0f * timeSpentCompletingCurrentAction) / (1.0f * timeNeededToCompleteCurrentAction);
			if(currAction == Action.MOVE) {
				if(moveDirection == Direction.EAST)
					x += 0.5 * percentComplete;
				else if(moveDirection == Direction.WEST)
					x -= 0.5 * percentComplete;
			}
			else if(currAction == Action.MOVE_COMMITTED) {
				if(moveDirection == Direction.EAST)
					x -= 0.5 * (1 - percentComplete);
				else if(moveDirection == Direction.WEST)
					x += 0.5 * (1 - percentComplete);
			}
			else if(currAction == Action.MOVE_CANCELED) {
				if(moveDirection == Direction.EAST)
					x += 0.5 * (1 - percentComplete);
				else if(moveDirection == Direction.WEST)
					x -= 0.5 * (1 - percentComplete);
			}
		}
		return x;
	}

	public float getY() {
		float y = tile.getY();
		if(currAction == Action.MOVE || currAction == Action.MOVE_CANCELED || currAction == Action.MOVE_COMMITTED) {
			float percentComplete = (1.0f * timeSpentCompletingCurrentAction) / (1.0f * timeNeededToCompleteCurrentAction);
			if(currAction == Action.MOVE) {
				if(moveDirection == Direction.SOUTH)
					y += 0.5 * percentComplete;
				else if(moveDirection == Direction.NORTH)
					y -= 0.5 * percentComplete;
			}
			else if(currAction == Action.MOVE_COMMITTED) {
				if(moveDirection == Direction.SOUTH)
					y -= 0.5 * (1 - percentComplete);
				else if(moveDirection == Direction.NORTH)
					y += 0.5 * (1 - percentComplete);
			}
			else if(currAction == Action.MOVE_CANCELED) {
				if(moveDirection == Direction.SOUTH)
					y += 0.5 * (1 - percentComplete);
				else if(moveDirection == Direction.NORTH)
					y -= 0.5 * (1 - percentComplete);
			}
		}
		return y;
	}

	public void update(int delta) {
		while(performAction(delta) != -1);
	}

	private int performAction(int delta) {
		if(currAction == Action.NONE) {
			//use queued action if it's available and there's no other action to perform
			if(queuedAction != Action.NONE) {
				if(queuedAction == Action.MOVE)
					move(queuedMoveDirection);
				else
					setCurrentAction(queuedAction);
				queuedAction = Action.NONE;
				queuedMoveDirection = Direction.NONE;
			}

			//if startMoving was called, that takes precedence after any queued actions
			else if(queuedMoveDirectionPreferences[0] != null) {
				move(queuedMoveDirectionPreferences[0]);
			}

			//otherwise there's nothing to do
			else return -1;
		}
		else if(delta == 0)
			return -1;

		timeSpentCompletingCurrentAction += delta;
		if(timeSpentCompletingCurrentAction >= timeNeededToCompleteCurrentAction) {
			int remainder = timeSpentCompletingCurrentAction - timeNeededToCompleteCurrentAction;
			switch(currAction) {
				case MOVE:
					if(willCancelMove) {
						setCurrentAction(Action.MOVE_CANCELED);
						willCancelMove = false;
					}
					else {
						setCurrentAction(Action.MOVE_COMMITTED);
						tile = moveToTile;
					}
					moveToTile = null;
					break;
				case MOVE_COMMITTED:
					setCurrentAction(Action.NONE);
					moveDirection = Direction.NONE;
					break;
				case MOVE_CANCELED:
					setCurrentAction(Action.MOVE_CANCELED_RECOVERING);
					break;
				case MOVE_CANCELED_RECOVERING:
					setCurrentAction(Action.NONE);
					moveDirection = Direction.NONE;
					break;
				default:
					return -1;
			}
			return remainder;
		}
		return -1;
	}
	
	public abstract void render(Graphics g, Visibility visibility, float x, float y, float scale);

	public void move(Direction dir) {
		if(!isPerformingAction()) {
			setCurrentAction(Action.MOVE);
			facingDirection = dir;
			moveDirection = dir;
			moveToTile = level.getTile(tile, dir);
		}
		else {
			queuedAction = Action.MOVE;
			queuedMoveDirection = dir;
		}
	}

	public void startMoving(Direction dir) {
		move(dir);
		int last = queuedMoveDirectionPreferences.length - 1;
		int dirIndex = last;
		for(int i = 0; i < last && dirIndex == last; i++)
			if(queuedMoveDirectionPreferences[i] == dir)
				dirIndex = i;
		for(int i = dirIndex; i >= 1; i--)
			queuedMoveDirectionPreferences[i] = queuedMoveDirectionPreferences[i - 1];
		queuedMoveDirectionPreferences[0] = dir;
	}

	public void stopMoving(Direction dir) {
		boolean foundDir = false;
		for(int i = 0; i < queuedMoveDirectionPreferences.length - 1; i++) {
			if(!foundDir && queuedMoveDirectionPreferences[i] == dir)
				foundDir = true;
			if(foundDir)
				queuedMoveDirectionPreferences[i] = queuedMoveDirectionPreferences[i + 1];
		}
		queuedMoveDirectionPreferences[queuedMoveDirectionPreferences.length - 1] = null;
	}

	private void setCurrentAction(Action action) {
		currAction = action;
		timeSpentCompletingCurrentAction = 0;
		switch(action) {
			case MOVE:
				timeNeededToCompleteCurrentAction = timeNeededToCommitMove;
				break;
			case MOVE_COMMITTED:
				timeNeededToCompleteCurrentAction = timeNeededToCompleteMove;
				break;
			case MOVE_CANCELED:
				timeNeededToCompleteCurrentAction = timeNeededToCancelMove;
				break;
			case MOVE_CANCELED_RECOVERING:
				timeNeededToCompleteCurrentAction = timeNeededToRecoverFromCanceledMove;
				break;
			default:
				timeNeededToCompleteCurrentAction = 0;
				break;
		}
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public boolean canCancelMove() {
		return currAction == Action.MOVE;
	}

	public void cancelMove() {
		if(canCancelMove())
			willCancelMove = true;
	}

	public boolean isMoving() {
		return currAction == Action.MOVE || currAction == Action.MOVE_COMMITTED;
	}

	public boolean isCancelingMove() {
		return currAction == Action.MOVE_CANCELED;
	}

	public boolean isRecoveringFromCanceledMove() {
		return currAction == Action.MOVE_CANCELED_RECOVERING;
	}

	public boolean isPerformingAction() {
		return currAction != Action.NONE;
	}

	public Direction getFacing() {
		return (isMoving() || isCancelingMove() || isRecoveringFromCanceledMove() ? moveDirection : facingDirection);
	}

	public void setFacing(Direction facing) {
		facingDirection = facing;
	}

	public float getMoveSpeed() {
		return moveSpeedTilesPerSecond;
	}

	public void setMoveSpeed(float tilesPerSecond) {
		moveSpeedTilesPerSecond = tilesPerSecond;
		timeNeededToCompleteMove = (int) (500 / tilesPerSecond);
		timeNeededToCommitMove = timeNeededToCompleteMove;
		timeNeededToCancelMove = 2 * timeNeededToCommitMove;
	}
}