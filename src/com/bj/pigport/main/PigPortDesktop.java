package com.bj.pigport.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class PigPortDesktop {

	public static void main(String [] args) {
		
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.title = Game.TITLE;
		cfg.width = Game.V_WIDTH * Game.SCALE;
		cfg.height = Game.V_HEIGHT * Game.SCALE;
		cfg.vSyncEnabled = Game.VSYNC;
		cfg.resizable = false;
		//cfg.vSyncEnabled = false; // Setting to false disables vertical sync
		//cfg.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
		//cfg.backgroundFPS = 0; // Setting to 0 disables background fps throttling
		cfg.useGL20 = false;
		
		new LwjglApplication(new Game(), cfg);
	}
	
}
