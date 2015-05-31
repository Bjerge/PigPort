package com.bj.pigport.entities.player.abilities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class TimeStop {
	
	public static boolean timeStopStarted = false;
	public static float timeStopPlayerVelocityX;
	public static float timeStopPlayerVelocityY;
	public static float timeStopBallVelocityX;
	public static float timeStopBallVelocityY;
	public static int timeStopTimer = 0;
	
	public TimeStop()
	{
		
	}
	
	
	
	public static void TimeStopControls()
	{

		if(MyInput.isPressed(MyInput.BUTTON3) || MyInput.isPressed(MyInput.BUTTON6))
		{
			if(Play.timeStop != true && Player.numFairyDust > -1)
				{
				Play.timeStop = true;
					//Player.numFairyDust--;
				}
			else Play.timeStop = false;
		}
		
		if(Play.timeStop)
		{
			timeStopTimer++;
			if(timeStopTimer == (30 + (20 * Player.timeStopLevel))) Play.timeStop = false;
		}
		
			
		if(Play.timeStop)
		{
			if(timeStopStarted != true)
			{
				timeStopPlayerVelocityX = PlayerData.player.getBody().getLinearVelocity().x;
				timeStopPlayerVelocityY = PlayerData.player.getBody().getLinearVelocity().y;
				PlayerData.player.getBody().setGravityScale(0f);

				timeStopStarted = true;
			}
			PlayerData.player.getBody().setLinearVelocity(0, 0);
			
			if(PlayerData.player.tex.equals(Game.res.getTexture("girlRightPig"))){
				PlayerData.player.tex = Game.res.getTexture("girlRightPig");
				TextureRegion[] spritesRight = TextureRegion.split(PlayerData.player.tex, 91, 114)[0];
				PlayerData.player.setAnimation(spritesRight, 1 / 0.1f);
			}
			
			if(PlayerData.player.tex.equals(Game.res.getTexture("girlLeftPig"))){
				PlayerData.player.tex = Game.res.getTexture("girlLeftPig");
				TextureRegion[] spritesLeft = TextureRegion.split(PlayerData.player.tex, 91, 114)[0];
				PlayerData.player.setAnimation(spritesLeft, 1 / 0.1f);
			}
			
		}
		else if(Play.timeStop != true && timeStopStarted == true)
		{
			
			PlayerData.player.getBody().setLinearVelocity(timeStopPlayerVelocityX , timeStopPlayerVelocityY );		
			PlayerData.player.getBody().setGravityScale(1);
			timeStopStarted = false;
			timeStopTimer = 0;

			if(PlayerData.player.tex.equals(Game.res.getTexture("girlRightPig"))){
				PlayerData.player.tex = Game.res.getTexture("girlRightPig");
				TextureRegion[] spritesRight = TextureRegion.split(PlayerData.player.tex, 91, 114)[0];
				PlayerData.player.setAnimation(spritesRight, 1 / 8f);
			}
			
			if(PlayerData.player.tex.equals(Game.res.getTexture("girlLeftPig"))){
				PlayerData.player.tex = Game.res.getTexture("girlLeftPig");
				TextureRegion[] spritesLeft = TextureRegion.split(PlayerData.player.tex, 91, 114)[0];
				PlayerData.player.setAnimation(spritesLeft, 1 / 8f);
			}
		}
	}

}
