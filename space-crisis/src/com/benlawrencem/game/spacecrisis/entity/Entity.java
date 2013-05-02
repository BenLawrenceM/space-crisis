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
		setMoveSpeed(1);
		currAction = Action.NONE;
		queuedAction = Action.NONE;
		timeSpentCompletingCurrentAction = 0;
		timeNeededToCompleteCurrentAction = 0;
		timeNeededToRecoverFromCanceledMove = 200;
	}

	public float getX() {
		float x = tile.getX();
		if(currAction == Action.MOVE || currAction == Action.MOVE_CANCELED || currAction == Action.MOVE_COMMITTED) {
			if(moveDirection == Direction.EAST)
				x += (currAction == Action.MOVE_COMMITTED ? -0.5 : 0.5) * (timeSpentCompletingCurrentAction / timeNeededToCompleteCurrentAction);
			else if(moveDirection == Direction.WEST)
				x -= (currAction == Action.MOVE_COMMITTED ? -0.5 : 0.5) * (timeSpentCompletingCurrentAction / timeNeededToCompleteCurrentAction);
		}
		return x;
	}

	public float getY() {
		float y = tile.getY();
		if(currAction == Action.MOVE || currAction == Action.MOVE_CANCELED || currAction == Action.MOVE_COMMITTED) {
			if(moveDirection == Direction.SOUTH)
				y += (currAction == Action.MOVE_COMMITTED ? -0.5 : 0.5) * (timeSpentCompletingCurrentAction / timeNeededToCompleteCurrentAction);
			else if(moveDirection == Direction.NORTH)
				y -= (currAction == Action.MOVE_COMMITTED ? -0.5 : 0.5) * (timeSpentCompletingCurrentAction / timeNeededToCompleteCurrentAction);
		}
		return y;
	}

	public void update(int delta) {
		while(performAction(delta) != 0);
	}

	private int performAction(int delta) {
		if(currAction == Action.NONE) {
			//use queued action if it's available and there's no other action to perform
			if(queuedAction != Action.NONE) {
				setCurrentAction(queuedAction);
				if(currAction == Action.MOVE) {
					moveDirection = queuedMoveDirection;
					if(moveDirection != Direction.NONE)
						moveToTile = level.getTile(tile, moveDirection);
					queuedMoveDirection = Direction.NONE;
				}
				queuedAction = Action.NONE;
			}

			//otherwise there's nothing to do
			else return 0;
		}

		timeSpentCompletingCurrentAction += delta;
		if(timeSpentCompletingCurrentAction >= timeNeededToCompleteCurrentAction) {
			int remainder = timeSpentCompletingCurrentAction - timeNeededToCompleteCurrentAction;
			switch(currAction) {
				case MOVE:
					if(willCancelMove)
						setCurrentAction(Action.MOVE_CANCELED);
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
					return 0;
			}
			return remainder;
		}
		return 0;
	}
	
	public abstract void render(Graphics g, Visibility visibility, float x, float y, float scale);

	public void move(Direction dir) {
		if(!isPerformingAction()) {
			setCurrentAction(Action.MOVE);
			moveDirection = dir;
			moveToTile = level.getTile(tile, dir);
		}
		else {
			queuedAction = Action.MOVE;
			queuedMoveDirection = dir;
		}
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
		timeNeededToCompleteMove = (int) (1 / tilesPerSecond);
		timeNeededToCommitMove = timeNeededToCompleteMove / 2;
		timeNeededToCancelMove = timeNeededToCompleteMove;
	}
}