package com.benlawrencem.game.spacecrisis.display;

import org.newdawn.slick.Graphics;

public interface Renderable {
	void render(Graphics g, Visibility visibility, float x, float y, float scale);
}