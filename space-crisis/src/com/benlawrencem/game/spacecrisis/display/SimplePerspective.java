package com.benlawrencem.game.spacecrisis.display;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.entity.Entity;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public class SimplePerspective implements Perspective {
	private TileLevel level;
	private float scale;

	public SimplePerspective(TileLevel level) {
		this.level = level;
		scale = 1;
	}

	@Override
	public void render(Graphics g, Entity entity) {
		float x = entity.getX() * level.getTileWidth() * scale;
		float y = entity.getY() * level.getTileHeight() * scale;
		entity.render(g, Visibility.VISIBLE, x, y, scale);
	}

	@Override
	public void render(Graphics g, Tile tile) {
		float x = tile.getX() * level.getTileWidth() * scale;
		float y = tile.getY() * level.getTileHeight() * scale;
		tile.render(g, Visibility.VISIBLE, x, y, scale);
	}
}