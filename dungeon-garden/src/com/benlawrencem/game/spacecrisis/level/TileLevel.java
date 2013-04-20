package com.benlawrencem.game.spacecrisis.level;

import com.benlawrencem.game.spacecrisis.Direction;

public interface TileLevel extends Level {
	Tile getTile(Tile source, Direction dir);
	int getTileWidth();
	int getTileHeight();
}