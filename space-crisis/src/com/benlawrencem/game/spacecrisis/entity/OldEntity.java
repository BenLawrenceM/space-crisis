package com.benlawrencem.game.spacecrisis.entity;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.Renderable;
import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public abstract class OldEntity implements Renderable {
	private TileLevel level;
	private Tile currTile;
	private Tile nextTile;
	private Direction moveDirection;
	private Direction nextMoveDirection;
	private Direction mostRecentMoveDirection;
	private int timeSpentMoving;
	private boolean isMoving;
	private int timeSpentCancelingMove;
	private boolean isCancelingMove;
	private int timeNeededToCommitMove;
	private int timeNeededToCompleteMove;
	private int timeNeededToCancelMove;

	public OldEntity(TileLevel level, Tile startingTile) {
		this.level = level;
		currTile = startingTile;
		nextTile = null;
		moveDirection = Direction.NONE;
		nextMoveDirection = Direction.NONE;
		mostRecentMoveDirection = Direction.NONE;
		timeSpentMoving = 0;
		isMoving = false;
		timeSpentCancelingMove = 0;
		isCancelingMove = false;
		timeNeededToCommitMove = 200;
		timeNeededToCompleteMove = 400;
		timeNeededToCancelMove = 600;
	}

	public float getX() {
		float x = currTile.getX();
		if(isMoving && moveDirection == Direction.EAST)
			x += (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		else if(isMoving && moveDirection == Direction.WEST)
			x -= (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		else if(isCancelingMove && moveDirection == Direction.EAST)
			x += (0.5 - 0.5 * timeSpentCancelingMove / timeNeededToCancelMove);
		else if(isCancelingMove && moveDirection == Direction.WEST)
			x -= (0.5 - 0.5 * timeSpentCancelingMove / timeNeededToCancelMove);
		return x;
	}

	public float getY() {
		float y = currTile.getY();
		if(isMoving && moveDirection == Direction.NORTH)
			y -= (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		else if(isMoving && moveDirection == Direction.SOUTH)
			y += (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		else if(isCancelingMove && moveDirection == Direction.NORTH)
			y -= (0.5 - 0.5 * timeSpentCancelingMove / timeNeededToCancelMove);
		else if(isCancelingMove && moveDirection == Direction.SOUTH)
			y += (0.5 - 0.5 * timeSpentCancelingMove / timeNeededToCancelMove);
		return y;
	}

	public void update(int delta) {
		int remainder = updatePositionBasedOnMovement(delta);
		decideBehavior(delta);
		updatePositionBasedOnMovement(remainder);
	}

	private int updatePositionBasedOnMovement(int delta) {
		int remainder = delta;

		if(isMoving) {
			if(timeSpentMoving < timeNeededToCommitMove && timeSpentMoving + delta >= timeNeededToCommitMove) {
				if(isCancelingMove) {
					//cancel move
					isMoving = false;
					timeSpentMoving = 0;
					timeSpentCancelingMove = 0;
				}
				else {
					//commit move
					currTile = nextTile;
					nextTile = null;
				}
			}

			if(isMoving) {
				//increment movement
				timeSpentMoving += delta;
	
				//complete move
				if(timeSpentMoving >= timeNeededToCompleteMove) {
					remainder = timeSpentMoving - timeNeededToCompleteMove;
					mostRecentMoveDirection = moveDirection;
					moveDirection = Direction.NONE;
					isMoving = false;
					timeSpentMoving = 0;
					if(nextMoveDirection != Direction.NONE) {
						Direction dir = nextMoveDirection;
						nextMoveDirection = Direction.NONE;
						move(dir);
					}
				}
				else remainder = 0;
			}
		}

		else if(isCancelingMove) {
			//increment cancelled movement
			timeSpentCancelingMove += delta;

			//complete cancelling move
			if(timeSpentCancelingMove >= timeNeededToCancelMove) {
				remainder = timeSpentCancelingMove - timeNeededToCancelMove;
				isCancelingMove = false;
				timeSpentCancelingMove = 0;
				mostRecentMoveDirection = moveDirection;
				moveDirection = Direction.NONE;
				nextMoveDirection = Direction.NONE;
			}
			else remainder = 0;
		}

		return remainder;
	}

	protected abstract void decideBehavior(int delta);

	private boolean hasCommittedMove() {
		return timeSpentMoving >= timeNeededToCommitMove;
	}

	public abstract void render(Graphics g, Visibility visibility, float x, float y, float scale);

	public void moveNorth() {
		move(Direction.NORTH);
	}

	public void moveSouth() {
		move(Direction.SOUTH);
	}

	public void moveEast() {
		move(Direction.EAST);
	}

	public void moveWest() {
		move(Direction.WEST);
	}

	protected void move(Direction dir) {
		if(isMoving || isCancelingMove)
			nextMoveDirection = dir;
		else {
			nextTile = level.getTile(currTile, dir);
			mostRecentMoveDirection = (dir != Direction.NONE ? dir : moveDirection);
			moveDirection = dir;
			isMoving = true;
			timeSpentMoving = 0;
		}
	}

	public Direction getMoveDirection() {
		return moveDirection;
	}

	public Direction getMostRecentMoveDirection() {
		return mostRecentMoveDirection;
	}

	public boolean isMoving() {
		return isMoving;
	}

	protected boolean isMovingNorth() {
		return moveDirection == Direction.NORTH;
	}

	protected boolean isMovingSouth() {
		return moveDirection == Direction.SOUTH;
	}

	protected boolean isMovingEast() {
		return moveDirection == Direction.EAST;
	}

	protected boolean isMovingWest() {
		return moveDirection == Direction.WEST;
	}

	public void cancelMove() {
		if(isMoving && !hasCommittedMove())
			isCancelingMove = true;
	}

	public boolean isCancelingMove() {
		return isCancelingMove && !isMoving;
	}
}