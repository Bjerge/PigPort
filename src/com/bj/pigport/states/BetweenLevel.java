package com.bj.pigport.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.GameButton;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.main.Game;

public class BetweenLevel extends GameState{

	
	private GameButton nextLevelButton;
	private Texture nextLevelTex;
	
	private GameButton returnMenuButton;
	private Texture returnMenuTex;
	
	private GameButton restartMapButton;
	private Texture restartMapTex;
	
	private GameButton shieldButton;
	private Texture shieldInactiveTex;
	private Texture shieldActiveTex;
	
	private GameButton continueFromLevelUpScreenButton;
	private Texture continueFromLevelUpScreenTex;
	
	private GameButton powerUpsButton;
	private Texture powerUpsTex;
	private GameButton resetPowerUpsButton;
	private Texture resetPowerUpsTex;
	
	private GameButton improveAimButton;
	private Texture improveAimTex;
	private GameButton improveTimeStopButton;
	private Texture improveTimeStopTex;	
	private GameButton improvePlayerAttackButton;
	private Texture improvePlayerAttackTex;	
	
	private TextureRegion[] font;
	private TextureRegion levelCompleted;
	private TextureRegion levelUpScreen;
	
	public boolean powerUpsScreen = false;
	
	
	public BetweenLevel(GameStateManager gsm) {
		super(gsm);
		
		Texture tex = Game.res.getTexture("HUDNumbers");
		Texture texp = Game.res.getTexture("levelCompleted");
		Texture texu = Game.res.getTexture("levelUpScreen");
		
		levelCompleted = new TextureRegion(texp, 0, 0, 740, 640);
		levelUpScreen = new TextureRegion(texu, 0, 0, 400, 200);
		
		continueFromLevelUpScreenTex = Game.res.getTexture("continueFromLevelUpScreen");
		continueFromLevelUpScreenButton = new GameButton(new TextureRegion(continueFromLevelUpScreenTex, 0, 0, continueFromLevelUpScreenTex.getWidth(), continueFromLevelUpScreenTex.getHeight()), 660, 180, getCam());
		
		nextLevelTex = Game.res.getTexture("nextLevelButton");
		nextLevelButton = new GameButton(new TextureRegion(nextLevelTex, 0, 0, nextLevelTex.getWidth(), nextLevelTex.getHeight()), 950, 130, getCam());
		
		returnMenuTex = Game.res.getTexture("returnButton");
		returnMenuButton = new GameButton(new TextureRegion(returnMenuTex, 0, 0, returnMenuTex.getWidth(), returnMenuTex.getHeight()), 400, 130, getCam());
		
		restartMapTex = Game.res.getTexture("restartButton");
		restartMapButton = new GameButton(new TextureRegion(restartMapTex, 0, 0, restartMapTex.getWidth(), restartMapTex.getHeight()),800, 130, getCam());
		
		powerUpsTex = Game.res.getTexture("powerUpsButton");
		powerUpsButton = new GameButton(new TextureRegion(powerUpsTex, 0, 0, powerUpsTex.getWidth(), powerUpsTex.getHeight()), 600, 130, getCam());
		
		improveAimTex = Game.res.getTexture("improveAimButton");
		improveAimButton = new GameButton(new TextureRegion(improveAimTex, 0, 0, improveAimTex.getWidth(), improveAimTex.getHeight()), 530, 300, getCam());
		
		improveTimeStopTex = Game.res.getTexture("improveTimeStopButton");
		improveTimeStopButton = new GameButton(new TextureRegion(improveTimeStopTex, 0, 0, improveTimeStopTex.getWidth(), improveTimeStopTex.getHeight()), 790, 300, getCam());
		
		improvePlayerAttackTex = Game.res.getTexture("improvePlayerAttackButton");
		improvePlayerAttackButton = new GameButton(new TextureRegion(improvePlayerAttackTex, 0, 0, improvePlayerAttackTex.getWidth(), improvePlayerAttackTex.getHeight()), 660, 300, getCam());

		resetPowerUpsTex = Game.res.getTexture("resetPowerUpsButton");
		resetPowerUpsButton = new GameButton(new TextureRegion(resetPowerUpsTex, 0, 0, resetPowerUpsTex.getWidth(), resetPowerUpsTex.getHeight()), 660, 90, getCam());
		
		shieldInactiveTex = Game.res.getTexture("shieldInactiveButton");
		shieldButton = new GameButton(new TextureRegion(shieldInactiveTex, 0, 0, shieldInactiveTex.getWidth(), shieldInactiveTex.getHeight()), 200, 300, getCam());

		
		
		
		font = new TextureRegion[11];
		for(int i = 0; i < 6; i++){
			font[i] = new TextureRegion(tex, i * 18, 0, 18, 18);
		}
		for(int i = 0; i < 5; i++) {
			font[i + 6] = new TextureRegion(tex, i * 18, 18, 18, 18);
		}
		
		
		
		
		//Game.V_WIDTH/2*s, Game.V_HEIGHT/2*s
		getCam().setToOrtho(false, Game.V_WIDTH * Game.CAMZOOM, Game.V_HEIGHT * Game.CAMZOOM);
		

	}

	@Override
	public void handleInput() {
		// mouse/touch input
		if(nextLevelButton.isClicked()) {
			Play.level++;
			gsm.setState(GameStateManager.PLAY);
			PlayerData.player.health = Player.playerStartHealth;
			Player.numFairyCage = 0;
			Player.numFairyDust = 0;
			MyContactListener.playerAtEnd = false;
			Play.isPlayerTeleporting = false;
		}
		
		if(restartMapButton.isClicked()) {
			gsm.setState(GameStateManager.PLAY);
			PlayerData.player.health = Player.playerStartHealth;
			Player.numFairyCage = 0;
			Player.numFairyDust = 0;
			MyContactListener.playerAtEnd = false;
			Play.isPlayerTeleporting = false;
		}
		
		if(returnMenuButton.isClicked()) {
			getCam().zoom = 1;
			gsm.setState(GameStateManager.LEVELSELECT);
		}
		
		if(continueFromLevelUpScreenButton.isClicked()) 
		{
			if(Player.playerLevelUp) Player.playerLevelUp = false;
			if(Player.playerManagingPowerUps)
			{
				gsm.setState(GameStateManager.LEVELSELECT);
				Player.playerManagingPowerUps = false;
			}
		}
		
		if(improveAimButton.isClicked() && Player.playerPowerUpPoints > 0) {
			Player.playerPowerUpPoints--;
			Player.ballDotLevel++;
		}
		if(improveTimeStopButton.isClicked() && Player.playerPowerUpPoints > 0) {
			Player.playerPowerUpPoints--;
			Player.timeStopLevel++;
		}
		if(improvePlayerAttackButton.isClicked() && Player.playerPowerUpPoints > 0) {
			Player.playerPowerUpPoints--;
			Player.playerAttackLevel++;
		}
		if(powerUpsButton.isClicked()) {
			Player.playerLevelUp = true;
		}
		if(resetPowerUpsButton.isClicked()) {
			Player.playerPowerUpPoints = Player.playerPowerUpPoints + Player.ballDotLevel + Player.timeStopLevel + Player.playerAttackLevel + Player.playerStartHealthLevel;
			Player.ballDotLevel = 0;
			Player.timeStopLevel = 0;
			Player.playerAttackLevel = 0;
			
			Player.shieldButtonClicked = false;
			Player.playerStartHealthLevel = 0;
			Player.playerStartHealth = 1;
		}
		
		if(shieldButton.isClicked() && Player.playerPowerUpPoints > 1 && Player.shieldButtonClicked != true) {
			Player.shieldButtonClicked = true;
			Player.playerPowerUpPoints -= 2;
			Player.playerStartHealthLevel += 2;
			Player.playerStartHealth = 2;
		}
		
		if(Player.shieldButtonClicked != true)
		{
			shieldInactiveTex = Game.res.getTexture("shieldInactiveButton");
			shieldButton = new GameButton(new TextureRegion(shieldInactiveTex, 0, 0, shieldInactiveTex.getWidth(), shieldInactiveTex.getHeight()), 200, 300, getCam());
		}
		if(Player.shieldButtonClicked)
		{
			shieldActiveTex = Game.res.getTexture("shieldActiveButton");
			shieldButton = new GameButton(new TextureRegion(shieldActiveTex, 0, 0, shieldActiveTex.getWidth(), shieldActiveTex.getHeight()), 200, 300, getCam());
		}
		
		
		
	}

	@Override
	public void update(float dt) {
		handleInput();
		if(Player.playerLevelUp || Player.playerManagingPowerUps)
		{
			continueFromLevelUpScreenButton.update(dt);
			improveAimButton.update(dt);
			improveTimeStopButton.update(dt);
			improvePlayerAttackButton.update(dt);
			resetPowerUpsButton.update(dt);
			shieldButton.update(dt);;
		}
		else
		{
			nextLevelButton.update(dt);
			restartMapButton.update(dt);
			returnMenuButton.update(dt);
		}
		powerUpsButton.update(dt);

	}

	@Override
	public void render() {
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(getCam().combined);
		
		sb.begin();
		
		if(Player.playerLevelUp || Player.playerManagingPowerUps) 
		{
			sb.draw(levelUpScreen, 450, 400);
			
			drawString(sb, Player.playerPowerUpPoints + "", 767, 438);
			
			drawString(sb, Player.ballDotLevel + "", 530, 230);
			
			drawString(sb, Player.timeStopLevel + "", 790, 230);
			
			drawString(sb, Player.playerAttackLevel + "", 660, 230);
		}
		else
		{
			//450
			sb.draw(levelCompleted, 300, 40);
		
			drawString(sb, Player.fairyDustCollectedInFinishedMap + "", 832, 350);
			
			drawString(sb, Player.fairyDustCollectedInAllMaps + "", 832, 300);
			
			drawString(sb, Player.playerLevel + "", 550, 477);
			
			drawString(sb, Player.currentXP + " / " + Player.requiredXP, 710, 477);
		}

		sb.end();
		
		
		if(Player.playerLevelUp || Player.playerManagingPowerUps)
		{
			continueFromLevelUpScreenButton.render(sb);
			improveAimButton.render(sb);
			improveTimeStopButton.render(sb);
			improvePlayerAttackButton.render(sb);
			resetPowerUpsButton.render(sb);
			shieldButton.render(sb);
		}
		else 
		{
			nextLevelButton.render(sb);
			restartMapButton.render(sb);
			returnMenuButton.render(sb);			
			powerUpsButton.render(sb);
		}
		
		
		
		
		
		
		
	}
	
	private void drawString(SpriteBatch sb, String s, float x, float y) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '/') c = 10;
			else if(c >= '0' && c <= '9') c -= '0';
			else continue;
			sb.draw(font[c], x + i * 18, y);
		}
	}
	
	
	

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}

