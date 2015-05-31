package com.bj.pigport.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.GameButton;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.main.Game;

public class LevelSelect extends GameState{
	
	private GameButton[][] buttons;
	private GameButton easyButton;
	private GameButton mediumButton;
	private GameButton hardButton;
	private Texture easyTex;
	private Texture mediumTex;
	private Texture hardTex;
	
	private GameButton powerUpsButton;
	private Texture powerUpsTex;
	
	public LevelSelect(GameStateManager gsm) {
		super(gsm);

		easyTex = Game.res.getTexture("easyButtonPressed");
		easyButton = new GameButton(new TextureRegion(easyTex, 0, 0, easyTex.getWidth(), easyTex.getHeight()), Game.V_WIDTH/2*Game.SCALE-(easyTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+easyTex.getHeight(), getCam());
		mediumTex = Game.res.getTexture("mediumButtonUnpressed");
		mediumButton = new GameButton(new TextureRegion(mediumTex, 0, 0, mediumTex.getWidth(), mediumTex.getHeight()), Game.V_WIDTH/2*Game.SCALE, Game.V_HEIGHT/2*Game.SCALE+mediumTex.getHeight(), getCam());
		hardTex = Game.res.getTexture("hardButtonUnpressed");
		hardButton = new GameButton(new TextureRegion(hardTex, 0, 0, hardTex.getWidth(), hardTex.getHeight()), Game.V_WIDTH/2*Game.SCALE+(hardTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+hardTex.getHeight(), getCam());
		
		TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("HUD"), 0, 0, 32, 32);
		buttons = new GameButton[3][5];
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col] = new GameButton(buttonReg, Game.V_WIDTH/2*Game.SCALE + col * 40, Game.V_HEIGHT/2*Game.SCALE - row * 40, getCam());
				buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
			}
		}
		
		powerUpsTex = Game.res.getTexture("powerUpsButton");
		powerUpsButton = new GameButton(new TextureRegion(powerUpsTex, 0, 0, powerUpsTex.getWidth(), powerUpsTex.getHeight()), 250, 200, getCam());
				
		//Game.V_WIDTH/2*s, Game.V_HEIGHT/2*s
		getCam().setToOrtho(false, Game.V_WIDTH * Game.CAMZOOM, Game.V_HEIGHT * Game.CAMZOOM);

	}

	@Override
	public void handleInput() {
		// mouse/touch input
		if(easyButton.isClicked()) {
			Play.difficulty = 1;
		}
		if(mediumButton.isClicked()) {
			Play.difficulty = 2;
		}
		if(hardButton.isClicked()) {
			Play.difficulty = 3;
		}
		if(powerUpsButton.isClicked()) {
			Player.playerManagingPowerUps = true;
			gsm.setState(GameStateManager.BETWEENLEVEL);
		}
		
	}

	@Override
	public void update(float dt) {
		handleInput();
		
		if(Play.difficulty == 1){
			easyTex = Game.res.getTexture("easyButtonPressed");
			easyButton = new GameButton(new TextureRegion(easyTex, 0, 0, easyTex.getWidth(), easyTex.getHeight()), Game.V_WIDTH/2*Game.SCALE-(easyTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+easyTex.getHeight(), getCam());
			mediumTex = Game.res.getTexture("mediumButtonUnpressed");
			mediumButton = new GameButton(new TextureRegion(mediumTex, 0, 0, mediumTex.getWidth(), mediumTex.getHeight()), Game.V_WIDTH/2*Game.SCALE, Game.V_HEIGHT/2*Game.SCALE+mediumTex.getHeight(), getCam());
			hardTex = Game.res.getTexture("hardButtonUnpressed");
			hardButton = new GameButton(new TextureRegion(hardTex, 0, 0, hardTex.getWidth(), hardTex.getHeight()), Game.V_WIDTH/2*Game.SCALE+(hardTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+hardTex.getHeight(), getCam());
		} else if (Play.difficulty == 2){
			easyTex = Game.res.getTexture("easyButtonUnpressed");
			easyButton = new GameButton(new TextureRegion(easyTex, 0, 0, easyTex.getWidth(), easyTex.getHeight()), Game.V_WIDTH/2*Game.SCALE-(easyTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+easyTex.getHeight(), getCam());
			mediumTex = Game.res.getTexture("mediumButtonPressed");
			mediumButton = new GameButton(new TextureRegion(mediumTex, 0, 0, mediumTex.getWidth(), mediumTex.getHeight()), Game.V_WIDTH/2*Game.SCALE, Game.V_HEIGHT/2*Game.SCALE+mediumTex.getHeight(), getCam());
			hardTex = Game.res.getTexture("hardButtonUnpressed");
			hardButton = new GameButton(new TextureRegion(hardTex, 0, 0, hardTex.getWidth(), hardTex.getHeight()), Game.V_WIDTH/2*Game.SCALE+(hardTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+hardTex.getHeight(), getCam());
		} else if (Play.difficulty == 3){
			easyTex = Game.res.getTexture("easyButtonUnpressed");
			easyButton = new GameButton(new TextureRegion(easyTex, 0, 0, easyTex.getWidth(), easyTex.getHeight()), Game.V_WIDTH/2*Game.SCALE-(easyTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+easyTex.getHeight(), getCam());
			mediumTex = Game.res.getTexture("mediumButtonUnpressed");
			mediumButton = new GameButton(new TextureRegion(mediumTex, 0, 0, mediumTex.getWidth(), mediumTex.getHeight()), Game.V_WIDTH/2*Game.SCALE, Game.V_HEIGHT/2*Game.SCALE+mediumTex.getHeight(), getCam());
			hardTex = Game.res.getTexture("hardButtonPressed");
			hardButton = new GameButton(new TextureRegion(hardTex, 0, 0, hardTex.getWidth(), hardTex.getHeight()), Game.V_WIDTH/2*Game.SCALE+(hardTex.getWidth()*1.5f), Game.V_HEIGHT/2*Game.SCALE+hardTex.getHeight(), getCam());
		}
		
		easyButton.update(dt);
		mediumButton.update(dt);
		hardButton.update(dt);
		powerUpsButton.update(dt);
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].update(dt);
				if(buttons[row][col].isClicked()) {
					Play.level = row * buttons[0].length + col + 1;
					gsm.setState(GameStateManager.PLAY);
					PlayerData.player.health = Player.playerStartHealth;
					Player.numFairyCage = 0;
					Player.numFairyDust = 0;
					MyContactListener.playerAtEnd = false;
					Play.isPlayerTeleporting = false;
				}
			}
		}
	}

	@Override
	public void render() {
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(getCam().combined);
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].render(sb);
			}
		}
		
		easyButton.render(sb);
		mediumButton.render(sb);
		hardButton.render(sb);
		powerUpsButton.render(sb);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}
