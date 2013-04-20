package com.benlawrencem.game.spacecrisis.level;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.*;
import com.benlawrencem.game.spacecrisis.entity.Entity;

public class PlayerMovementLevel implements TileLevel {
	private Tile[][] tiles;
	private List<Entity> entities;
	private Perspective perspective;
	private PlayerEntity player;

	@Override
	public void init() {
		tiles = new Tile[99][99];
		for(int x = 0; x < tiles.length; x++)
			for(int y = 0; y < tiles[x].length; y++)
				tiles[x][y] = new Tile(this, x, y);

		entities = new ArrayList<Entity>();

		player = new PlayerEntity(this, tiles[49][49]);
		entities.add(player);
		perspective = new EntityPerspective(this, player);
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
	public void keyPressed(int key, char c) {
		switch(key) {
			case Input.KEY_UP:
				player.startMovingNorth();
				break;
			case Input.KEY_DOWN:
				player.startMovingSouth();
				break;
			case Input.KEY_LEFT:
				player.startMovingWest();
				break;
			case Input.KEY_RIGHT:
				player.startMovingEast();
				break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch(key) {
		case Input.KEY_UP:
			player.stopMovingNorth();
			break;
		case Input.KEY_DOWN:
			player.stopMovingSouth();
			break;
		case Input.KEY_LEFT:
			player.stopMovingWest();
			break;
		case Input.KEY_RIGHT:
			player.stopMovingEast();
			break;
	}
	}

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

	private static class PlayerEntity extends Entity {
		public PlayerEntity(TileLevel level, Tile startingTile) {
			super(level, startingTile);
		}

		@Override
		protected void decideBehavior(int delta) {}

		@Override
		public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
			if(visibility == Visibility.VISIBLE) {
				if(isMovingNorth())
					g.setColor(Color.red);
				else if(isMovingSouth())
					g.setColor(Color.green);
				else if(isMovingEast())
					g.setColor(Color.cyan);
				else if(isMovingWest())
					g.setColor(Color.yellow);
				else
					g.setColor(Color.white);
				g.fillRect(x - (4 * scale), y - (10 * scale), 8 * scale, 20 * scale);
			}
		}
	}
}