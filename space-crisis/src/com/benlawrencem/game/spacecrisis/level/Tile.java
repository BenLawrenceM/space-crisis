package com.benlawrencem.game.spacecrisis.level;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.display.Renderable;
import com.benlawrencem.game.spacecrisis.display.Visibility;

public class Tile implements Renderable {
	private TileLevel level;
	private int x;
	private int y;

	public Tile(TileLevel level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
		if(visibility == Visibility.VISIBLE) {
			g.setColor(Color.gray);
			g.setLineWidth(1);
			g.drawRect(x - level.getTileWidth() /2 * scale, y - level.getTileHeight() / 2 * scale, level.getTileWidth() * scale, level.getTileHeight() * scale);
		}
	}
}