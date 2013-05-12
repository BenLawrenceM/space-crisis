package com.benlawrencem.game.spacecrisis;

import com.benlawrencem.game.spacecrisis.level.Level;
import com.benlawrencem.game.spacecrisis.level.PlayerMovementLevel;
import com.benlawrencem.game.spacecrisis.net.PlayerMovementServer;

public class SpaceCrisisGameServer {
	public static void main(String[] args) {
		new SpaceCrisisGameServer();
	}

	public SpaceCrisisGameServer() {
		Level level = new PlayerMovementLevel();
		level.init(true);
		PlayerMovementServer.getInstance().startServer();
		(new LevelRunner(level)).start();
	}

	private class LevelRunner extends Thread {
		private Level level;
		private boolean isRunning;

		public LevelRunner(Level level) {
			this.level = level;
			isRunning = true;
		}

		@Override
		public void run() {
			long then = System.currentTimeMillis();
			while(isRunning) {
				try {
					sleep(20);
				} catch (InterruptedException e) {}
				long now = System.currentTimeMillis();
				level.update((int) (now - then));
				then = now;
			}
		}
	}
}