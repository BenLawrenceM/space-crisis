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
	private Direction[] moveDirectionPreferences;
	private int timeSpentMoving;
	private int timeNeededToCommitMove;
	private int timeNeededToCompleteMove;

	public Entity(TileLevel level, Tile startingTile) {
		this.level = level;
		currTile = startingTile;
		nextTile = null;
		moveDirection = null;
		moveDirectionPreferences = new Direction[4];
		for(int i = 0; i < moveDirectionPreferences.length; i++)
			moveDirectionPreferences[i] = null;
		timeSpentMoving = 0;
		timeNeededToCommitMove = 100;
		timeNeededToCompleteMove = 200;
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
		decideBehavior(delta);
		updatePositionBasedOnMovement(delta);
	}

	private void updatePositionBasedOnMovement(int delta) {
		if(isMoving()) {
			//commit move
			if(timeSpentMoving < timeNeededToCommitMove && timeSpentMoving + delta >= timeNeededToCommitMove) {
				currTile = nextTile;
				nextTile = null;
			}

			//increment movement
			timeSpentMoving += delta;

			//complete move and queue up next move
			if(timeSpentMoving >= timeNeededToCompleteMove) {
				moveDirection = null;
				if(moveDirectionPreferences[0] != null) {
					int newDelta = timeSpentMoving - timeNeededToCompleteMove;
					move(moveDirectionPreferences[0]);
					updatePositionBasedOnMovement(newDelta);
				}
				else timeSpentMoving = 0;
			}
		}
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

	private void move(Direction dir) {
		if(!isMoving()) {
			nextTile = level.getTile(currTile, dir);
			moveDirection = dir;
			timeSpentMoving = 0;
		}
	}

	public void startMovingNorth() {
		startMoving(Direction.NORTH);
	}

	public void startMovingSouth() {
		startMoving(Direction.SOUTH);
	}

	public void startMovingEast() {
		startMoving(Direction.EAST);
	}

	public void startMovingWest() {
		startMoving(Direction.WEST);
	}

	private void startMoving(Direction dir) {
		if(!isMoving())
			move(dir);
		int dirIndex = moveDirectionPreferences.length - 1;
		for(int i = 0; i < moveDirectionPreferences.length && dirIndex == -1; i++)
			if(moveDirectionPreferences[i] == dir)
				dirIndex = i;
		for(int i = dirIndex; i >= 1; i--)
			moveDirectionPreferences[i] = moveDirectionPreferences[i - 1];
		moveDirectionPreferences[0] = dir;
	}

	public void stopMovingNorth() {
		stopMoving(Direction.NORTH);
	}

	public void stopMovingSouth() {
		stopMoving(Direction.SOUTH);
	}

	public void stopMovingEast() {
		stopMoving(Direction.EAST);
	}

	public void stopMovingWest() {
		stopMoving(Direction.WEST);
	}

	public void stopMoving() {
		for(int i = 0; i < moveDirectionPreferences.length; i++)
			moveDirectionPreferences[i] = null;
	}

	private void stopMoving(Direction dir) {
		boolean foundDir = false;
		for(int i = 0; i < moveDirectionPreferences.length - 1; i++) {
			if(!foundDir && moveDirectionPreferences[i] == dir)
				foundDir = true;
			if(foundDir)
				moveDirectionPreferences[i] = moveDirectionPreferences[i + 1];
		}
		moveDirectionPreferences[moveDirectionPreferences.length - 1] = null;
	}

	protected boolean isMoving() {
		return moveDirection != null;
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