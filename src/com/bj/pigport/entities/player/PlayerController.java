package com.bj.pigport.entities.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bj.pigport.entities.player.Player;
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
		
		
		// player jump(W) or (space)
		if(MyInput.isPressed(MyInput.BUTTON1)){
			if(MyContactListener.isPlayerOnGround() && Play.timeStop != true){
				PlayerData.player.getBody().applyForceToCenter(0, 400, true);
			}
		}
		
		if (MyInput.isDown(MyInput.BUTTON5)) {   
			if(MyContactListener.isPlayerTouchingWallRight()){
				PlayerData.player.getBody().setLinearVelocity(0, PlayerData.player.getBody().getLinearVelocity().y); 
				}
				//else if(cl.isPlayerOnGround())
				else
				{
					PlayerData.player.getBody().setLinearVelocity(2, PlayerData.player.getBody().getLinearVelocity().y); 
					Play.playerJumpVelocityX = 2;
				}
				
				if(!PlayerData.player.tex.equals(Game.res.getTexture("girlRightShielded")) && Play.timeStop != true && Player.playerShielded){
					PlayerData.player.tex = Game.res.getTexture("girlRightShielded");
					TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 97, 118)[0];
					PlayerData.player.setAnimation(sprites, 1 / 8f);
				}
				if(!PlayerData.player.tex.equals(Game.res.getTexture("girlRightPig")) && Play.timeStop != true && Player.playerShielded != true && Player.playerShieldedCD != true){
					PlayerData.player.tex = Game.res.getTexture("girlRightPig");
					TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 91, 114)[0];
					PlayerData.player.setAnimation(sprites, 1 / 8f);
				}
				if(!PlayerData.player.tex.equals(Game.res.getTexture("girlRightShieldedCD")) && Play.timeStop != true && Player.playerShieldedCD){
					PlayerData.player.tex = Game.res.getTexture("girlRightShieldedCD");
					TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 97, 118)[0];
					PlayerData.player.setAnimation(sprites, 1 / 8f);
				}
			}
			// player moving left(A)
		else if (MyInput.isDown(MyInput.BUTTON4)) {          
			if(MyContactListener.isPlayerTouchingWallLeft()){
				PlayerData.player.getBody().setLinearVelocity(0, PlayerData.player.getBody().getLinearVelocity().y); 
			}
			//else if(cl.isPlayerOnGround())
			else
			{
				PlayerData.player.getBody().setLinearVelocity(-2, PlayerData.player.getBody().getLinearVelocity().y);
				Play.playerJumpVelocityX = -2;
			}
			if(!PlayerData.player.tex.equals(Game.res.getTexture("girlLeftShielded")) && Play.timeStop != true && Player.playerShielded){
				PlayerData.player.tex = Game.res.getTexture("girlLeftShielded");
				TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 97, 118)[0];
				PlayerData.player.setAnimation(sprites, 1 / 8f);;
			}
			if(!PlayerData.player.tex.equals(Game.res.getTexture("girlLeftPig")) && Play.timeStop != true && Player.playerShielded != true && Player.playerShieldedCD != true){
				PlayerData.player.tex = Game.res.getTexture("girlLeftPig");
				TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 91, 114)[0];
				PlayerData.player.setAnimation(sprites, 1 / 8f);
			}
			if(!PlayerData.player.tex.equals(Game.res.getTexture("girlLeftShieldedCD")) && Play.timeStop != true && Player.playerShieldedCD){
				PlayerData.player.tex = Game.res.getTexture("girlLeftShieldedCD");
				TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 97, 118)[0];
				PlayerData.player.setAnimation(sprites, 1 / 8f);
			}
			// player not moving()
		}
		else {
			PlayerData.player.getBody().setLinearVelocity(0, PlayerData.player.getBody().getLinearVelocity().y);
		//if(cl.isPlayerOnGround() || player.tex.equals(Game.res.getTexture("CharSpinAnimation"))){
			if(!PlayerData.player.tex.equals(Game.res.getTexture("girlStillShielded")) && Player.playerShielded){
				PlayerData.player.tex = Game.res.getTexture("girlStillShielded");
				TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 118, 120)[0];
				PlayerData.player.setAnimation(sprites, 1 / 8f);
			}
			if(!PlayerData.player.tex.equals(Game.res.getTexture("girlStillV2")) && Player.playerShielded != true){
				PlayerData.player.tex = Game.res.getTexture("girlStillV2");
				TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 110, 118)[0];
				PlayerData.player.setAnimation(sprites, 1 / 8f);
			}
			if(!PlayerData.player.tex.equals(Game.res.getTexture("girlStillShieldedCD")) && Player.playerShieldedCD){
				PlayerData.player.tex = Game.res.getTexture("girlStillShieldedCD");
				TextureRegion[] sprites = TextureRegion.split(PlayerData.player.tex, 118, 120)[0];
				PlayerData.player.setAnimation(sprites, 1 / 8f);
			}

		}
	}
	

}
