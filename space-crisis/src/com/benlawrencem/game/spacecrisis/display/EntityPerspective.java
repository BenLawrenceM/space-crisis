package com.benlawrencem.game.spacecrisis.display;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.SpaceCrisisGame;
import com.benlawrencem.game.spacecrisis.entity.OldEntity;
import com.benlawrencem.game.spacecrisis.level.Tile;
import com.benlawrencem.game.spacecrisis.level.TileLevel;

public class EntityPerspective implements Perspective {
	private final float MAX_OBJECT_WIDTH = 100;
	private final float MAX_OBJECT_HEIGHT = 100;

	private TileLevel level;
	private OldEntity entity;
	private float scale;

	public EntityPerspective(TileLevel level, OldEntity entity) {
		this.level = level;
		this.entity = entity;
		scale = 3;
	}

	@Override
	public void render(Graphics g, OldEntity entity) {
		float x = SpaceCrisisGame.GAME_WIDTH / 2 + (entity.getX() - this.entity.getX()) * level.getTileWidth() * scale;
		float y = SpaceCrisisGame.GAME_HEIGHT / 2 + (entity.getY() - this.entity.getY()) * level.getTileHeight() * scale;
		Visibility visibility = Visibility.VISIBLE;
		if(x < -MAX_OBJECT_WIDTH * scale || x > SpaceCrisisGame.GAME_WIDTH + MAX_OBJECT_WIDTH * scale)
			visibility = Visibility.HIDDEN;
		if(y < -MAX_OBJECT_HEIGHT * scale || y > SpaceCrisisGame.GAME_HEIGHT + MAX_OBJECT_HEIGHT * scale)
			visibility = Visibility.HIDDEN;
		entity.render(g, visibility, x, y, scale);
	}

	@Override
	public void render(Graphics g, Tile tile) {
		float x = SpaceCrisisGame.GAME_WIDTH / 2 + (tile.getX() - entity.getX()) * level.getTileWidth() * scale;
		float y = SpaceCrisisGame.GAME_HEIGHT / 2 + (tile.getY() - entity.getY()) * level.getTileHeight() * scale;
		Visibility visibility = Visibility.VISIBLE;
		if(x < -MAX_OBJECT_WIDTH * scale || x > SpaceCrisisGame.GAME_WIDTH + MAX_OBJECT_WIDTH * scale)
			visibility = Visibility.HIDDEN;
		if(y < -MAX_OBJECT_HEIGHT * scale || y > SpaceCrisisGame.GAME_HEIGHT + MAX_OBJECT_HEIGHT * scale)
			visibility = Visibility.HIDDEN;
		tile.render(g, visibility, x, y, scale);
	}
}