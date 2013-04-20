package com.benlawrencem.game.spacecrisis.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public class RandomWalkEntity extends Entity {
	private int delayBeforeDecidingBehavior;

	public RandomWalkEntity(TileLevel level, Tile startingTile) {
		super(level, startingTile);
		delayBeforeDecidingBehavior = 0;
	}

	@Override
	public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
		if(visibility == Visibility.VISIBLE) {
			if(isMovingNorth())
				g.setColor(Color.red);
			else if(isMovingSouth())
				g.setColor(Color.green);
			else if(isMovingEast())
				g.setColor(Color.cyan);
			else if(isMovingWest())
				g.setColor(Color.yellow);
			else
				g.setColor(Color.white);
			g.fillRect(x - (16 * scale), y - (12 * scale), 32 * scale, 24 * scale);
		}
	}

	@Override
	protected void decideBehavior(int delta) {
		if(!isMoving()) {
			delayBeforeDecidingBehavior -= delta;
			if(delayBeforeDecidingBehavior < 0) {
				double r = Math.random();
				if(r < 0.25)
					moveNorth();
				else if(r < 0.50)
					moveSouth();
				else if(r < 0.75)
					moveEast();
				else
					moveWest();
				delayBeforeDecidingBehavior = (int) (50 + 250 * Math.random());
			}
		}
	}
}