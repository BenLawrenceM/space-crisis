package com.benlawrencem.game.spacecrisis.level;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BasicLevel implements Level {
	private int lastKeyPressed;

	@Override
	public void init(boolean isServer) {
		lastKeyPressed = -1;
	}

	@Override
	public void update(int delta) {
		
	}

	@Override
	public void render(Graphics g) {
		g.setBackground(Color.black);
		g.setColor(Color.white);
		g.drawString("BasicLevel", 20, 30);
		g.drawString("Last key pressed: " + lastKeyPressed, 20, 45);
	}

	@Override
	public void keyPressed(int key, char c) {
		lastKeyPressed = key;
	}

	@Override
	public void keyReleased(int key, char c) {
		
	}
}