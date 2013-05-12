package com.benlawrencem.game.spacecrisis.level;

import org.newdawn.slick.Graphics;

public interface Level {
	void init(boolean isServer);
	void update(int delta);
	void render(Graphics g);
	void keyPressed(int key, char c);
	void keyReleased(int key, char c);
}