package com.bj.pigport.entities.mapobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class MovingPlatform extends B2DSprite{
	
	public MovingPlatform(Body body) {
		
		super(body);
		
		Texture tex = Game.res.getTexture("movingPlatform");
		TextureRegion[] sprites = TextureRegion.split(tex, 192, 64)[0];
		
		setAnimation(sprites, 1 / 3f);
	}
	
	public static void PlatformMove(){
		if(Play.platformNr>0)
		{
			for(int i = 0; i < Play.platformNr; i++)
			{
				if(!(Play.movingPlatform[i] == null))
				{
					if(Play.movingPlatform[i].getPosition().x < Play.platformSpawnX[i]-2 || Play.movingPlatform[i].getPosition().y < Play.platformSpawnY[i]-2)
					{
						Play.platformMovingRightLeft[i] = 1;
					}
					if(Play.movingPlatform[i].getPosition().x > Play.platformSpawnX[i]+2 || Play.movingPlatform[i].getPosition().y > Play.platformSpawnY[i]+2)
					{
						Play.platformMovingRightLeft[i] = 2;
					}
					if(Play.platformMovingRightLeft[i] == 1)
					{
						Play.movingPlatform[i].getBody().setLinearVelocity((1.3f * Play.platformDirectionX[i]), (1.3f * Play.platformDirectionY[i]));
						if(Play.timeStop)Play.movingPlatform[i].getBody().setLinearVelocity(0, 0);
					}
					if(Play.platformMovingRightLeft[i] == 2)
					{
						Play.movingPlatform[i].getBody().setLinearVelocity((-1.3f * Play.platformDirectionX[i]), (-1.3f * Play.platformDirectionY[i]));
						if(Play.timeStop)Play.movingPlatform[i].getBody().setLinearVelocity(0, 0);
					}
				}
			}
		}
	}
}
