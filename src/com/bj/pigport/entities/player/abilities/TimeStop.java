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

		if(MyInput.isPressed(MyInput.BUTTON3) || MyInput.isPressed(MyInput.BUTTON7))
		{
			if(Play.timeStop != true && Play.player.data.numFairyDust > -1)
				{
				Play.timeStop = true;
					//Player.numFairyDust--;
				}
			else Play.timeStop = false;
		}
		
		if(Play.timeStop)
		{
			timeStopTimer++;
			if(timeStopTimer == (30 + (20 * Play.player.data.timeStopLevel))) Play.timeStop = false;
		}
		
			
		if(Play.timeStop)
		{
			if(timeStopStarted != true)
			{
				timeStopPlayerVelocityX = Play.player.getBody().getLinearVelocity().x;
				timeStopPlayerVelocityY = Play.player.getBody().getLinearVelocity().y;
				Play.player.getBody().setGravityScale(0f);

				timeStopStarted = true;
			}
			Play.player.getBody().setLinearVelocity(0, 0);
			
			if(Play.player.data.tex.equals(Game.res.getTexture("girlRightPig"))){
				Play.player.data.tex = Game.res.getTexture("girlRightPig");
				TextureRegion[] spritesRight = TextureRegion.split(Play.player.data.tex, 91, 114)[0];
				Play.player.setAnimation(spritesRight, 1 / 0.1f);
			}
			
			if(Play.player.data.tex.equals(Game.res.getTexture("girlLeftPig"))){
				Play.player.data.tex = Game.res.getTexture("girlLeftPig");
				TextureRegion[] spritesLeft = TextureRegion.split(Play.player.data.tex, 91, 114)[0];
				Play.player.setAnimation(spritesLeft, 1 / 0.1f);
			}
			
		}
		else if(Play.timeStop != true && timeStopStarted == true)
		{
			
			Play.player.getBody().setLinearVelocity(timeStopPlayerVelocityX , timeStopPlayerVelocityY );		
			Play.player.getBody().setGravityScale(1);
			timeStopStarted = false;
			timeStopTimer = 0;

			if(Play.player.data.tex.equals(Game.res.getTexture("girlRightPig"))){
				Play.player.data.tex = Game.res.getTexture("girlRightPig");
				TextureRegion[] spritesRight = TextureRegion.split(Play.player.data.tex, 91, 114)[0];
				Play.player.setAnimation(spritesRight, 1 / 8f);
			}
			
			if(Play.player.data.tex.equals(Game.res.getTexture("girlLeftPig"))){
				Play.player.data.tex = Game.res.getTexture("girlLeftPig");
				TextureRegion[] spritesLeft = TextureRegion.split(Play.player.data.tex, 91, 114)[0];
				Play.player.setAnimation(spritesLeft, 1 / 8f);
			}
		}
	}

}
