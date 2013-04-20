package com.benlawrencem.game.spacecrisis;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.benlawrencem.game.spacecrisis.level.*;

public class LoadingState extends BasicGameState {
	private int stateId;

	public LoadingState(int stateId) {
		this.stateId = stateId;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString("Loading...", 40, 40);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		((GameplayState) game.getState(SpaceCrisisGame.GAMEPLAY_STATE)).init(container, game, new RandomWalkLevel());
		game.enterState(SpaceCrisisGame.GAMEPLAY_STATE);
	}

	@Override
	public int getID() {
		return stateId;
	}
}