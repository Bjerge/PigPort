package com.bj.pigport.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bj.pigport.handlers.Content;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.handlers.MyInputProcessor;

public class Game implements ApplicationListener{
	
	public static final String TITLE = "Pig Port";
	public static final int V_WIDTH = 440;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 3;
	//How far it's zoomed in (increase to zoom-out / decrease to zoom-in)
	public static final int CAMZOOM = 3;
	public static final boolean VSYNC = true;
	
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private SpriteBatch sbMiniMap;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private OrthographicCamera miniMapCam;
	
	private GameStateManager gsm;
	
	public static Content res;
	
	public void create(){
		
		Texture.setEnforcePotImages(false);
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		res = new Content();
		res.loadTexture("res/images/GirlStillShieldedCD.png", "girlStillShieldedCD");
		res.loadTexture("res/images/GirlStillShielded.png", "girlStillShielded");
		res.loadTexture("res/images/girlStillPigV2.png", "girlStillV2");
		res.loadTexture("res/images/girlMoveRightPig.png", "girlRightPig");
		res.loadTexture("res/images/girlMoveRightShieldedCD.png", "girlRightShieldedCD");
		res.loadTexture("res/images/girlMoveRightShielded.png", "girlRightShielded");
		res.loadTexture("res/images/girlMoveLeftPig.png", "girlLeftPig");
		res.loadTexture("res/images/girlMoveLeftShieldedCD.png", "girlLeftShieldedCD");
		res.loadTexture("res/images/girlMoveLeftShielded.png", "girlLeftShielded");
		res.loadTexture("res/images/CharSpinAnimation.png", "CharSpinAnimation");
		res.loadTexture("res/images/Shoot.png", "ball");
		res.loadTexture("res/images/fireBall.png", "fireBall");
		res.loadTexture("res/images/fairyDust.png", "fairyDust");
		res.loadTexture("res/images/fairyCage.png", "fairyCage");
		res.loadTexture("res/images/fairy.png", "fairy");
		res.loadTexture("res/images/trap.png", "trap");
		res.loadTexture("res/images/enemy.png", "enemy");
		
		res.loadTexture("res/images/BasicMinion.png", "BasicMinion");
		res.loadTexture("res/images/BasicFlyingMinion.png", "BasicFlyingMinion");
		
		res.loadTexture("res/images/EnemyHit.png", "enemyHit");
		res.loadTexture("res/images/LeftAttack.png", "girlLeftAttack");
		res.loadTexture("res/images/RightAttack.png", "girlRightAttack");
		res.loadTexture("res/images/StandAttack.png", "girlStandAttack");
		
		res.loadTexture("res/images/LeftAttackShielded.png", "girlLeftAttackShielded");
		res.loadTexture("res/images/RightAttackShielded.png", "girlRightAttackShielded");
		res.loadTexture("res/images/StandAttackShielded.png", "girlStandAttackShielded");
		
		
		res.loadTexture("res/images/hud.png", "HUD");
		res.loadTexture("res/images/dot.png", "dot");
		res.loadTexture("res/images/inGamePanel.png", "inGamePanel");
		res.loadTexture("res/images/main_menu/playButton.png", "playButton");
		res.loadTexture("res/images/main_menu/playButton2.png", "playButton2");
		res.loadTexture("res/images/main_menu/playButton3.png", "playButton3");
		res.loadTexture("res/images/main_menu/playButton4.png", "playButton4");
		res.loadTexture("res/images/main_menu/playButton7.png", "playButton7");
		res.loadTexture("res/images/returnButton.png", "returnButton");
		res.loadTexture("res/images/restartButton.png", "restartButton");
		res.loadTexture("res/images/testBaggrund.png", "baggrundMain");
		res.loadTexture("res/images/movingPlatform.png", "movingPlatform");
		res.loadTexture("res/images/difficulty_buttons/easyUnpressed.png", "easyButtonUnpressed");
		res.loadTexture("res/images/difficulty_buttons/easyPressed.png", "easyButtonPressed");
		res.loadTexture("res/images/difficulty_buttons/mediumUnpressed.png", "mediumButtonUnpressed");
		res.loadTexture("res/images/difficulty_buttons/mediumPressed.png", "mediumButtonPressed");
		res.loadTexture("res/images/difficulty_buttons/hardUnpressed.png", "hardButtonUnpressed");
		res.loadTexture("res/images/difficulty_buttons/hardPressed.png", "hardButtonPressed");
		res.loadTexture("res/images/between_level/nextLevelButton.png", "nextLevelButton");
		res.loadTexture("res/images/between_level/levelCompleted.png", "levelCompleted");
		res.loadTexture("res/images/between_level/improveAimButton.png", "improveAimButton");
		res.loadTexture("res/images/between_level/improveTimeStopButton.png", "improveTimeStopButton");
		res.loadTexture("res/images/between_level/improvePlayerAttackButton.png", "improvePlayerAttackButton");
		res.loadTexture("res/images/between_level/HUDNumbers.png", "HUDNumbers");
		res.loadTexture("res/images/between_level/levelUpScreen.png", "levelUpScreen");
		res.loadTexture("res/images/between_level/continueFromLevelUpScreen.png", "continueFromLevelUpScreen");
		res.loadTexture("res/images/between_level/powerUpsButton.png", "powerUpsButton");
		res.loadTexture("res/images/between_level/resetPowerUpsButton.png", "resetPowerUpsButton");
		
		res.loadTexture("res/images/between_level/shieldActiveButton.png", "shieldActiveButton");
		res.loadTexture("res/images/between_level/shieldInactiveButton.png", "shieldInactiveButton");
		
		res.loadTexture("res/images/timeStopFog.png", "timeStopFog");
		
		res.loadTexture("res/images/tpAnimation.png", "tpAnimation");
		
		res.loadTexture("res/images/PlayerHealth.png", "PlayerHealth");
		res.loadTexture("res/images/PlayerHealthLeft.png", "PlayerHealthLeft");
		
		res.loadTexture("res/images/attackAnimationStar.png", "attackAnimationStar");
		res.loadTexture("res/images/attackAnimationStarCD.png", "attackAnimationStarCD");
		
		
		res.loadTexture("res/images/frozenChest.png", "mapObject1");
		res.loadTexture("res/images/runeStone.png", "mapObject2");
		
		res.loadTexture("res/images/Meat.png", "Meat");
		res.loadTexture("res/images/Coins.png", "Coins");
		res.loadTexture("res/images/Energy.png", "Energy");
		
		res.loadTexture("res/images/Letters.png", "Letters");
		res.loadTexture("res/images/PlainButton1.png", "PlainButton1");
		res.loadTexture("res/images/PlainButton2.png", "PlainButton2");
		
		res.loadTexture("res/images/Gems/Fire.png", "FireGem");
		res.loadTexture("res/images/Gems/Frost.png", "FrostGem");
		res.loadTexture("res/images/Gems/Wind.png", "WindGem");
		res.loadTexture("res/images/Gems/Nature.png", "NatureGem");
		res.loadTexture("res/images/Gems/Light.png", "LightGem");
		res.loadTexture("res/images/Gems/Dark.png", "DarkGem");
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH * CAMZOOM, V_HEIGHT * CAMZOOM);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH * CAMZOOM, V_HEIGHT * CAMZOOM);
		sbMiniMap = new SpriteBatch();
		
		gsm = new GameStateManager(this);
	}
	
	public void render(){
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
		
			gsm.update(Gdx.graphics.getDeltaTime());
			gsm.render();
			MyInput.update();
	}
	
	public void dispose(){
		
	}
	
	public SpriteBatch getSpriteBatch(){
		return sb;
	}
	
	public OrthographicCamera getCamera(){
		return cam;
	}
	
	public OrthographicCamera getHUDCamera(){
		return hudCam;
	}
	
	public OrthographicCamera getMiniMapCam(){
		return miniMapCam;
	}
	
	public void resize(int w, int h) {}
	public void pause() {}
	public void resume() {}

	public SpriteBatch getSpriteBatchMiniMap() {
		return sbMiniMap;
	}

}
