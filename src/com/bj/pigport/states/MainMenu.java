package com.bj.pigport.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.GameButton;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.main.Game;

public class MainMenu extends GameState{

	//float s = Game.SCALE;
	float scale = 3;
	float zoom = 3;
	private GameButton playButton;
	private TextureRegion bg;
	
	public static PlayerData [] data = new PlayerData[1];
	
	public MainMenu(GameStateManager gsm) {
		super(gsm);
		
		data [0] = new PlayerData();
		
		Texture tex = Game.res.getTexture("baggrundMain");
		bg = new TextureRegion(tex, 0, -1, tex.getWidth(), tex.getHeight());
		
		tex = Game.res.getTexture("playButton7");
		playButton = new GameButton(new TextureRegion(tex, 0, 0, tex.getWidth(), tex.getHeight()), Game.V_WIDTH/2*scale, Game.V_HEIGHT/2*scale, getCam());
		
		getCam().setToOrtho(false, Game.V_WIDTH * zoom, Game.V_HEIGHT * zoom);
		
	}

	@Override
	public void handleInput() {
		// mouse/touch input
		if(playButton.isClicked()) {
			gsm.setState(GameStateManager.TOWN);
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		
		playButton.update(dt);
	}

	@Override
	public void render() {
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(getCam().combined);
		
		// draw background
		sb.begin();
		sb.draw(bg, 0, 0);
		sb.end();
		
		// draw button
		playButton.render(sb);
		
	}

	@Override
	public void dispose() {
		
	}

}
