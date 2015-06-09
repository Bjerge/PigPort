package com.bj.pigport.entities.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Filter;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.player.abilities.TimeStop;

public class PlayerController {
	
	private static Play pl;
	private static MyContactListener cl;
	
	
	
	public PlayerController()
	{
		
	}
	
	
	public static void PlayerControls()
	{
		
				
		if(MyInput.isPressed(MyInput.BUTTON6) || Play.player.data.collThinDisableTimer == 1)
		{
			collThinManager(0);
			Play.player.data.collThinDisableTimer = 2;
		}
		if(Play.player.data.collThinDisableTimer > 0)
		{
			Play.player.data.collThinDisableTimer++;
			if(Play.player.data.collThinDisableTimer > 15)
			{
				collThinManager(1);
				Play.player.data.collThinDisableTimer = 0;
			}
		}
		
		// player jump(W) or (space)
		if(MyInput.isPressed(MyInput.BUTTON1)){
			if(MyContactListener.isPlayerOnGround() && Play.timeStop != true){
				Play.player.getBody().applyForceToCenter(0, 310, true);
			}
		}
		
		if (MyInput.isDown(MyInput.BUTTON5)) {   
		if(MyContactListener.isPlayerTouchingWallRight()){
			Play.player.getBody().setLinearVelocity(0, Play.player.getBody().getLinearVelocity().y); 
			}
			//else if(cl.isPlayerOnGround())
			else
			{
				Play.player.getBody().setLinearVelocity(2.5f, Play.player.getBody().getLinearVelocity().y); 
				Play.playerJumpVelocityX = 2;
			}
			if(Play.player.data.directionLock != true)
			{
				Play.player.data.direction = -1;
				
				if(!Play.player.data.tex.equals(Game.res.getTexture("girlRightPig")) && Play.timeStop != true){
					Play.player.data.tex = Game.res.getTexture("girlRightPig");
					TextureRegion[] sprites = TextureRegion.split(Play.player.data.tex, 91, 114)[0];
					Play.player.setAnimation(sprites, 1 / 8f);
				}
			}
		}
			// player moving left(A)
		else if (MyInput.isDown(MyInput.BUTTON4)) {          
			if(MyContactListener.isPlayerTouchingWallLeft()){
				Play.player.getBody().setLinearVelocity(0, Play.player.getBody().getLinearVelocity().y); 
			}
			//else if(cl.isPlayerOnGround())
			else
			{
				Play.player.getBody().setLinearVelocity(-2.5f, Play.player.getBody().getLinearVelocity().y);
				Play.playerJumpVelocityX = -2;
			}
			if(Play.player.data.directionLock != true)
			{
				Play.player.data.direction = 1;
				
				if(!Play.player.data.tex.equals(Game.res.getTexture("girlLeftPig")) && Play.timeStop != true){
					Play.player.data.tex = Game.res.getTexture("girlLeftPig");
					TextureRegion[] sprites = TextureRegion.split(Play.player.data.tex, 91, 114)[0];
					Play.player.setAnimation(sprites, 1 / 8f);
				}
			}
			// player not moving()
		}
		else {
			Play.player.getBody().setLinearVelocity(0, Play.player.getBody().getLinearVelocity().y);
			if(!Play.player.data.tex.equals(Game.res.getTexture("girlStillV2"))){
				Play.player.data.tex = Game.res.getTexture("girlStillV2");
				TextureRegion[] sprites = TextureRegion.split(Play.player.data.tex, 110, 118)[0];
				Play.player.setAnimation(sprites, 1 / 8f);
			}
		}
	}
	
	public static void collThinManager(int i)
	{
		Filter filter = Play.player.getBody().getFixtureList().first().getFilterData();
		short bits = filter.maskBits;
		
		if(i == 0)bits = B2DVars.BIT_COLL;
		if(i == 1)bits = B2DVars.BIT_COLLTHIN;
		
		// set player mask bits
		filter.maskBits = bits;
		Play.player.getBody().getFixtureList().get(0).setFilterData(filter);
	}
}
