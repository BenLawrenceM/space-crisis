package com.benlawrencem.game.spacecrisis.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public class PillarEntity extends Entity {
	private static Image PILLAR_IMAGE;
	
	public static void loadResources() {
		try {
			PillarEntity.PILLAR_IMAGE = new Image("images/pillar.gif", false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			
		}
	}

	public PillarEntity(TileLevel level, Tile startingTile) {
		super(level, startingTile);
	}

	public PillarEntity(int id, TileLevel level, Tile startingTile) {
		super(id, level, startingTile);
	}

	@Override
	public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
		if(visibility == Visibility.VISIBLE)
			PillarEntity.PILLAR_IMAGE.draw(x - 11.5f * scale, y - 22 * scale, 23 * scale, 32 * scale);
	}
}