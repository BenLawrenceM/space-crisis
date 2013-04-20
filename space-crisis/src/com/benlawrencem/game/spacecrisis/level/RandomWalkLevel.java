package com.benlawrencem.game.spacecrisis.level;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.*;
import com.benlawrencem.game.spacecrisis.entity.Entity;
import com.benlawrencem.game.spacecrisis.entity.RandomWalkEntity;

public class RandomWalkLevel implements TileLevel {
	private Tile[][] tiles;
	private List<Entity> entities;
	private Perspective perspective;

	@Override
	public void init() {
		tiles = new Tile[99][99];
		for(int x = 0; x < tiles.length; x++)
			for(int y = 0; y < tiles[x].length; y++)
				tiles[x][y] = new Tile(this, x, y);

		entities = new ArrayList<Entity>();
		for(int i = 0; i < 999; i++)
			entities.add(new RandomWalkEntity(this, tiles[49][49]));

		Entity entity = new RandomWalkEntity(this, tiles[49][49]);
		entities.add(entity);
		perspective = new EntityPerspective(this, entity);
	}

	@Override
	public void update(int delta) {
		for(Entity entity : entities)
			entity.update(delta);
	}

	@Override
	public void render(Graphics g) {
		for(int r = 0; r < tiles.length; r++)
			for(int c = 0; c < tiles[r].length; c++)
				perspective.render(g, tiles[r][c]);

		for(Entity entity : entities)
			perspective.render(g, entity);
	}

	@Override
	public void keyPressed(int key, char c) {}

	@Override
	public void keyReleased(int key, char c) {}

	@Override
	public Tile getTile(Tile source, Direction dir) {
		int x = source.getX();
		int y = source.getY();
		switch(dir) {
			case NORTH:
				y--;
				break;
			case SOUTH:
				y++;
				break;
			case EAST:
				x++;
				break;
			case WEST:
				x--;
				break;
		}
		return tiles[x][y];
	}

	@Override
	public int getTileWidth() {
		return 32;
	}

	@Override
	public int getTileHeight() {
		return 24;
	}
}