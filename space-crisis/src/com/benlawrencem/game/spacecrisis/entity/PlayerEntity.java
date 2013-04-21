package com.benlawrencem.game.spacecrisis.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public class PlayerEntity extends Entity {
	private static Animation STAND_NORTH_ANIM, STAND_SOUTH_ANIM, STAND_EAST_ANIM, STAND_WEST_ANIM;
	private static Animation WALK_NORTH_ANIM, WALK_SOUTH_ANIM, WALK_EAST_ANIM, WALK_WEST_ANIM;

	public static void loadResources() {
		try {
			Image img = new Image("images/harvestmoon.gif", false, Image.FILTER_NEAREST);
			SpriteSheet sprite = new SpriteSheet(img, 23, 32);
			PlayerEntity.STAND_SOUTH_ANIM = new Animation(sprite, new int[] { 0,0 }, new int[] { 1000 });
			PlayerEntity.STAND_NORTH_ANIM = new Animation(sprite, new int[] { 0,1 }, new int[] { 1000 });
			PlayerEntity.STAND_WEST_ANIM = new Animation(sprite, new int[] { 0,2 }, new int[] { 1000 });
			PlayerEntity.STAND_EAST_ANIM = new Animation(sprite, new int[] { 0,3 }, new int[] { 1000 });
			int[] walkDurations = new int[] { 200, 200, 200, 200 };
			PlayerEntity.WALK_SOUTH_ANIM = new Animation(sprite, new int[] { 1,0, 0,0, 2,0, 0,0 }, walkDurations);
			PlayerEntity.WALK_NORTH_ANIM = new Animation(sprite, new int[] { 1,1, 0,1, 2,1, 0,1 }, walkDurations);
			PlayerEntity.WALK_WEST_ANIM = new Animation(sprite, new int[] { 1,2, 0,2, 2,2, 0,2 }, walkDurations);
			PlayerEntity.WALK_EAST_ANIM = new Animation(sprite, new int[] { 1,3, 0,3, 2,3, 0,3 }, walkDurations);
		} catch (SlickException e) {
			
		}
	}

	private Direction[] moveDirectionPreferences;

	public PlayerEntity(TileLevel level, Tile startingTile) {
		super(level, startingTile);
		moveDirectionPreferences = new Direction[4];
		for(int i = 0; i < moveDirectionPreferences.length; i++)
			moveDirectionPreferences[i] = null;
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

	@Override
	protected void decideBehavior(int delta) {
		if(!isMoving() && moveDirectionPreferences[0] != null)
			move(moveDirectionPreferences[0]);
	}

	@Override
	public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
		if(visibility == Visibility.VISIBLE) {
			Animation anim;
			if(isMoving()) {
				switch(getMoveDirection()) {
					case NORTH:
						anim = PlayerEntity.WALK_NORTH_ANIM;
						break;
					case SOUTH:
						anim = PlayerEntity.WALK_SOUTH_ANIM;
						break;
					case EAST:
						anim = PlayerEntity.WALK_EAST_ANIM;
						break;
					case WEST:
						anim = PlayerEntity.WALK_WEST_ANIM;
						break;
					default:
						anim = PlayerEntity.WALK_SOUTH_ANIM;
						break;
				}
			}
			else {
				switch(getMostRecentMoveDirection()) {
					case NORTH:
						anim = PlayerEntity.STAND_NORTH_ANIM;
						break;
					case SOUTH:
						anim = PlayerEntity.STAND_SOUTH_ANIM;
						break;
					case EAST:
						anim = PlayerEntity.STAND_EAST_ANIM;
						break;
					case WEST:
						anim = PlayerEntity.STAND_WEST_ANIM;
						break;
					default:
						anim = PlayerEntity.STAND_SOUTH_ANIM;
						break;
				}
			}
			anim.draw(x - 11.5f * scale, y - 22 * scale, 23 * scale, 32 * scale);
		}
	}
}