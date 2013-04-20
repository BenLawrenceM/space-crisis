package com.benlawrencem.game.spacecrisis.display;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.entity.Entity;
import com.benlawrencem.game.spacecrisis.level.Tile;

public interface Perspective {
	void render(Graphics g, Entity entity);
	void render(Graphics g, Tile tile);
}