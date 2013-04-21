package com.benlawrencem.game.spacecrisis.entity;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.Renderable;
import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public abstract class Entity implements Renderable {
	private TileLevel level;
	private Tile currTile;
	private Tile nextTile;
	private Direction moveDirection;
	private Direction nextMoveDirection;
	private Direction mostRecentMoveDirection;
	private int timeSpentMoving;
	private int timeNeededToCommitMove;
	private int timeNeededToCompleteMove;

	public Entity(TileLevel level, Tile startingTile) {
		this.level = level;
		currTile = startingTile;
		nextTile = null;
		moveDirection = Direction.NONE;
		nextMoveDirection = Direction.NONE;
		mostRecentMoveDirection = Direction.NONE;
		timeSpentMoving = 0;
		timeNeededToCommitMove = 150;
		timeNeededToCompleteMove = 300;
	}

	public float getX() {
		float x = currTile.getX();
		if(moveDirection == Direction.EAST)
			x += (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		else if(moveDirection == Direction.WEST)
			x -= (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		return x;
	}

	public float getY() {
		float y = currTile.getY();
		if(moveDirection == Direction.NORTH)
			y -= (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		else if(moveDirection == Direction.SOUTH)
			y += (1.0 * timeSpentMoving / timeNeededToCompleteMove) - (hasCommittedMove() ? 1 : 0);
		return y;
	}

	public void update(int delta) {
		int remainder = updatePositionBasedOnMovement(delta);
		decideBehavior(delta);
		updatePositionBasedOnMovement(remainder);
	}

	private int updatePositionBasedOnMovement(int delta) {
		int remainder = delta;

		if(isMoving()) {
			//commit move
			if(timeSpentMoving < timeNeededToCommitMove && timeSpentMoving + delta >= timeNeededToCommitMove) {
				currTile = nextTile;
				nextTile = null;
			}

			//increment movement
			timeSpentMoving += delta;

			//complete move
			if(timeSpentMoving >= timeNeededToCompleteMove) {
				remainder = timeSpentMoving - timeNeededToCompleteMove;
				mostRecentMoveDirection = moveDirection;
				moveDirection = Direction.NONE;
				timeSpentMoving = 0;
				if(nextMoveDirection != Direction.NONE) {
					Direction dir = nextMoveDirection;
					nextMoveDirection = Direction.NONE;
					move(dir);
				}
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
		if(isMoving()) {
			nextMoveDirection = dir;
		}
		else {
			nextTile = level.getTile(currTile, dir);
			mostRecentMoveDirection = (dir != Direction.NONE ? dir : moveDirection);
			moveDirection = dir;
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
		return moveDirection != Direction.NONE;
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
}