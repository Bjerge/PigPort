package com.bj.pigport.entities.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.bj.pigport.handlers.GameButton;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.abilities.Ball;

public class HUD {
	private TextureRegion fairyDust;
	private TextureRegion fairy;
	private TextureRegion[] font;
	private TextureRegion dot;
	private TextureRegion inGamePanel;
	//private TextureRegion tpAnimation;
	public static GameButton returnButton;
	public static GameButton restartButton;
	Timer timer;
	private TextureRegion timeStopFog;
	
	public HUD(Player player){

		
		Texture texp = Game.res.getTexture("inGamePanel");
		Texture tex = Game.res.getTexture("HUD");
		Texture texf = Game.res.getTexture("fairyDust");
		Texture texc = Game.res.getTexture("fairy");
		Texture texd = Game.res.getTexture("dot");
		
		Texture texfog = Game.res.getTexture("timeStopFog");
	    timeStopFog = new TextureRegion(texfog, 0, 0, 2000, 2000);
	    
		//Texture texTp = Game.res.getTexture("tpAnimation");
		
		
		Texture tex1 = Game.res.getTexture("returnButton");
		returnButton = new GameButton(new TextureRegion(tex1, 0, 0, tex1.getWidth(), tex1.getHeight()), 115, 50, GameState.getHudCam());
		
		Texture tex2 = Game.res.getTexture("restartButton");
		restartButton = new GameButton(new TextureRegion(tex2, 0, 0, tex2.getWidth(), tex2.getHeight()), 1270, 50, GameState.getHudCam());
				
		
		
		font = new TextureRegion[11];
		for(int i = 0; i < 6; i++){
			font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
		}
		for(int i = 0; i < 5; i++) {
			font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
		}
		
		inGamePanel = new TextureRegion(texp, 0, 0, 1460, 100);
		fairyDust = new TextureRegion(texf, 0, 0, 50, 50);
		fairy = new TextureRegion(texc, 0, 0, 64, 64);
		dot = new TextureRegion(texd, 0, 0, 6, 6);
		//tpAnimation = new TextureRegion(texTp, 0,0,100,100);
		
		}
	
	public void render(SpriteBatch sb){
		
		// draw return-button
		returnButton.render(sb);
		// draw restart-button
		restartButton.render(sb);
		
		sb.begin();
		/*
		if(Play.playerTPAnimationReady){
			sb.draw(tpAnimation, Play.playerTPPos1X *100+ 628, Play.playerTPPos1Y *100+340);
			System.out.println(Play.playerTPPos1X + " " + Play.playerTPPos1Y);
			sb.draw(tpAnimation, Play.playerTPPos2X *100+ 628, Play.playerTPPos2Y *100+340);
			Play.playerTPAnimationReady = true;
			
		}
		*/
		// draw icon
		sb.draw(inGamePanel, -10, 620);
		
		sb.draw(fairyDust, 150, 660);
		
		sb.draw(fairy, 35, 655);
		
		//Draws Dot Aim
		if (MyInput.isDown())
		{
			for(float i = 1; i < (6.5f + (0.5 * Player.ballDotLevel)) ;)
			{
				Ball.getDrawBall(i);
				sb.draw(dot,Ball.xBallCoordinate, Ball.yBallCoordinate);
				i += 0.5f;
			}
		}
		
		
		// draw icon amount
		
		drawString(sb, Player.numFairyDust + "", 170, 645);
		
		drawString(sb, Player.numFairyCage + " / " + Player.totalFairyCage,45, 645);
		
		if(Play.timeStop)sb.draw(timeStopFog, 0, 0);
		
		sb.end();
	}
	
	private void drawString(SpriteBatch sb, String s, float x, float y) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '/') c = 10;
			else if(c >= '0' && c <= '9') c -= '0';
			else continue;
			sb.draw(font[c], x + i * 9, y);
		}
	}
}
