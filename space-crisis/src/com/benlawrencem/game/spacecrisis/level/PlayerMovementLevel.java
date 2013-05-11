package com.benlawrencem.game.spacecrisis.level;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.benlawrencem.game.spacecrisis.Direction;
import com.benlawrencem.game.spacecrisis.display.*;
import com.benlawrencem.game.spacecrisis.entity.Entity;
import com.benlawrencem.game.spacecrisis.entity.PlayerEntity;

public class PlayerMovementLevel implements TileLevel {
	private Tile[][] tiles;
	private List<Entity> entities;
	private Perspective perspective;
	private PlayerEntity player;
	private int timeToNextBump;

	@Override
	public void init() {
		PlayerEntity.loadResources();

		tiles = new Tile[8][6];
		for(int x = 0; x < tiles.length; x++)
			for(int y = 0; y < tiles[x].length; y++)
				tiles[x][y] = new Tile(this, x, y);

		entities = new ArrayList<Entity>();
		player = new PlayerEntity(this, tiles[tiles.length/2][tiles[0].length/2]);
		entities.add(player);
		perspective = new EntityPerspective(this, player);

		timeToNextBump = 0;
	}

	@Override
	public void update(int delta) {
		if(timeToNextBump <= 0) {
			timeToNextBump = 1600 + (int) (500 * Math.random());
			player.cancelMove();
		}
		timeToNextBump -= delta;

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
				player.startMoving(Direction.NORTH);
				break;
			case Input.KEY_DOWN:
				player.startMoving(Direction.SOUTH);
				break;
			case Input.KEY_LEFT:
				player.startMoving(Direction.WEST);
				break;
			case Input.KEY_RIGHT:
				player.startMoving(Direction.EAST);
				break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch(key) {
		case Input.KEY_UP:
			player.stopMoving(Direction.NORTH);
			break;
		case Input.KEY_DOWN:
			player.stopMoving(Direction.SOUTH);
			break;
		case Input.KEY_LEFT:
			player.stopMoving(Direction.WEST);
			break;
		case Input.KEY_RIGHT:
			player.stopMoving(Direction.EAST);
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
		if(x < 0)
			x = tiles.length - 1;
		else if(x > tiles.length - 1)
			x = 0;
		if(y < 0)
			y = tiles[0].length -1;
		else if(y > tiles[0].length - 1)
			y = 0;
		return tiles[x][y];
	}

	@Override
	public int getTileWidth() {
		return 18;
	}

	@Override
	public int getTileHeight() {
		return 18;
	}
}