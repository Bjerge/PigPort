package com.bj.pigport.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.main.Game;

public abstract class GameState {
	protected GameStateManager gsm;
	protected Game game;
	
	protected SpriteBatch sb;
	protected SpriteBatch sbMiniMap;
	private OrthographicCamera cam;
	protected static OrthographicCamera hudCam;
	protected OrthographicCamera miniMapCam;
	
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		setCam(game.getCamera());
		setHudCam(game.getHUDCamera());
		miniMapCam = game.getMiniMapCam();
		sbMiniMap = game.getSpriteBatchMiniMap();
	}
		
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();

	public static OrthographicCamera getHudCam() {
		return hudCam;
	}

	public void setHudCam(OrthographicCamera hudCam) {
		GameState.hudCam = hudCam;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public void setCam(OrthographicCamera cam) {
		this.cam = cam;
	}
}
