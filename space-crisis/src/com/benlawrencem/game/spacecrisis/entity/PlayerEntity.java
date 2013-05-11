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
	private static Animation[] STAND_ANIMATIONS;
	private static Animation[] MOVE_ANIMATIONS;
	private static Animation[] BUMP_ANIMATIONS;

	public static void loadResources() {
		try {
			Image img = new Image("images/guysprite.gif", false, Image.FILTER_NEAREST);
			SpriteSheet sprite = new SpriteSheet(img, 23, 32);
			PlayerEntity.STAND_ANIMATIONS = new Animation[4];
			for(int i = 0; i < 4; i++) {
				PlayerEntity.STAND_ANIMATIONS[i] = new Animation(sprite, new int[] { 0,i }, new int[] { 1000 });
				PlayerEntity.STAND_ANIMATIONS[i].setAutoUpdate(false);
			}
			PlayerEntity.MOVE_ANIMATIONS = new Animation[4];
			int[] walkDurations = new int[] { 200, 200, 200, 200 };
			for(int i = 0; i < 4; i++) {
				PlayerEntity.MOVE_ANIMATIONS[i] = new Animation(sprite, new int[] { 3,i, 4,i, 3,i, 5,i }, walkDurations);
				PlayerEntity.MOVE_ANIMATIONS[i].setAutoUpdate(false);
			}
			PlayerEntity.BUMP_ANIMATIONS = new Animation[4];
			int[] bumpDurations = new int[] { 150, 150 };
			for(int i = 0; i < 4; i++) {
				PlayerEntity.BUMP_ANIMATIONS[i] = new Animation(sprite, new int[] { 16,i, 17,i }, bumpDurations);
				PlayerEntity.BUMP_ANIMATIONS[i].setAutoUpdate(false);
			}
		} catch (SlickException e) {
			
		}
	}

	private static int toIndex(Direction dir) {
		switch(dir) {
			case SOUTH: return 0;
			case NORTH: return 1;
			case WEST: return 2;
			case EAST: return 3;
		}
		return 0;
	}

	private Animation[] standAnim;
	private Animation[] moveAnim;
	private Animation[] bumpAnim;

	public PlayerEntity(TileLevel level, Tile startingTile) {
		super(level, startingTile);
		standAnim = new Animation[PlayerEntity.STAND_ANIMATIONS.length];
		for(int i = 0; i < PlayerEntity.STAND_ANIMATIONS.length; i++)
			standAnim[i] = PlayerEntity.STAND_ANIMATIONS[i].copy();
		moveAnim = new Animation[PlayerEntity.MOVE_ANIMATIONS.length];
		for(int i = 0; i < PlayerEntity.MOVE_ANIMATIONS.length; i++)
			moveAnim[i] = PlayerEntity.MOVE_ANIMATIONS[i].copy();
		bumpAnim = new Animation[PlayerEntity.BUMP_ANIMATIONS.length];
		for(int i = 0; i < PlayerEntity.BUMP_ANIMATIONS.length; i++)
			bumpAnim[i] = PlayerEntity.BUMP_ANIMATIONS[i].copy();
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		for(int i = 0; i < 4; i++) {
			standAnim[i].update(delta);
			moveAnim[i].update(delta);
			bumpAnim[i].update(delta);
		}
	}

	@Override
	public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
		if(visibility == Visibility.VISIBLE) {
			Animation anim;
			if(isMoving())
				anim = moveAnim[toIndex(getFacing())];
			else if(isCancelingMove())
				anim = bumpAnim[toIndex(getFacing())];
			else {
				anim = standAnim[toIndex(getFacing())];
				for(int i = 0; i < moveAnim.length; i++)
					moveAnim[i].restart();
			}
			anim.draw(x - 11.5f * scale, y - 22 * scale, 23 * scale, 32 * scale);
		}
	}
}