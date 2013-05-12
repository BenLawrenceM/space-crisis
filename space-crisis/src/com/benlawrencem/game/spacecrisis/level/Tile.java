package com.benlawrencem.game.spacecrisis.level;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.spacecrisis.display.Renderable;
import com.benlawrencem.game.spacecrisis.display.Visibility;
import com.benlawrencem.game.spacecrisis.entity.Entity;

public class Tile implements Renderable {
	private TileLevel level;
	private int x;
	private int y;
	private List<Entity> entities;
	private List<Entity> occupying;
	private List<Entity> leaving;
	private List<Entity> entering;

	public Tile(TileLevel level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
		entities = new ArrayList<Entity>();
		occupying = new ArrayList<Entity>();
		leaving = new ArrayList<Entity>();
		entering = new ArrayList<Entity>();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean canEnter(Entity entity) {
		return entering.size() == 0 && occupying.size() == 0 && leaving.size() == 0;
	}

	public void onEntering(Entity entity) {
		entering.add(entity);
	}

	public void onNoLongerEntering(Entity entity) {
		entering.remove(entity);
	}

	public void onEntered(Entity entity) {
		entering.remove(entity);
		occupying.add(entity);
		entities.add(entity);
	}

	public void onLeaving(Entity entity) {
		occupying.remove(entity);
		leaving.add(entity);
	}

	public void onNoLongerLeaving(Entity entity) {
		leaving.remove(entity);
		occupying.add(entity);
	}

	public void onLeft(Entity entity) {
		leaving.remove(entity);
		entities.remove(entity);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	@Override
	public void render(Graphics g, Visibility visibility, float x, float y, float scale) {
		if(visibility == Visibility.VISIBLE) {
			boolean hasSomeEntities = occupying.size() > 0;
			boolean hasSomeEntering = entering.size() > 0;
			boolean hasSomeLeaving = leaving.size() > 0;
			if(hasSomeEntities) {
				if(hasSomeEntering && hasSomeLeaving)
					g.setColor(new Color(255, 150, 255));
				else if(hasSomeEntering)
					g.setColor(new Color(100, 255, 255));
				else if(hasSomeLeaving)
					g.setColor(new Color(255, 100, 100));
				else
					g.setColor(new Color(255, 255, 255));
			}
			else if(hasSomeEntering) {
				g.setColor(new Color(0, 85, 85));
			}
			else if(hasSomeLeaving) {
				g.setColor(new Color(85, 0, 0));
			}
			else {
				g.setColor(new Color(0, 0, 0));
			}
			g.fillRect(x - level.getTileWidth() /2 * scale, y - level.getTileHeight() / 2 * scale, level.getTileWidth() * scale, level.getTileHeight() * scale);
			g.setColor(Color.gray);
			g.setLineWidth(1);
			g.drawRect(x - level.getTileWidth() /2 * scale, y - level.getTileHeight() / 2 * scale, level.getTileWidth() * scale, level.getTileHeight() * scale);
		}
	}
}