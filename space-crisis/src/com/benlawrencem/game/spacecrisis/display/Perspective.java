package com.benlawrencem.game.spacecrisis.display;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.entity.OldEntity;
import com.benlawrencem.game.spacecrisis.level.Tile;

public interface Perspective {
	void render(Graphics g, OldEntity entity);
	void render(Graphics g, Tile tile);
}