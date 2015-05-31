package com.bj.pigport.entities.mapobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class FireArrow extends B2DSprite{
		
	public FireArrow(Body body) {
		super(body);
		
		Texture texEnemy = Game.res.getTexture("fireBall");
		TextureRegion[] sprites = TextureRegion.split(texEnemy, 25, 25)[0];
		
		setAnimation(sprites, 1 / 8f);
	}
	
	
	public static void FireArrowMove()
	{
		if(Play.fireArrowNumber>1)
		{
			for(int i = 1; i < Play.fireArrowNumber; i++)
			{
				if(Play.fireArrowInitialised[i])
				{
					if(!(Play.fireArrow[i] == null) && Play.timeStop != true)
					{
						if(Play.difficulty == 1)Play.fireArrow[i].getBody().setLinearVelocity((1.5f * Play.fireArrowDirection[i]), Play.fireArrow[i].getBody().getLinearVelocity().y);
						if(Play.difficulty == 2)Play.fireArrow[i].getBody().setLinearVelocity((2f * Play.fireArrowDirection[i]), Play.fireArrow[i].getBody().getLinearVelocity().y);
						if(Play.difficulty == 3)Play.fireArrow[i].getBody().setLinearVelocity((2.5f * Play.fireArrowDirection[i]), Play.fireArrow[i].getBody().getLinearVelocity().y);
					}
					if(!(Play.fireArrow[i] == null) && Play.timeStop == true)
					{
						Play.fireArrow[i].getBody().setLinearVelocity(0f, Play.fireArrow[i].getBody().getLinearVelocity().y);
					}
				}
				if(Play.fireArrowInitialised[i] != true)
				{
					Play.fireArrow[i].getBody().setLinearVelocity(0f, Play.fireArrow[i].getBody().getLinearVelocity().y);
				}
			}
		}
	}
	
	/*
	public static void EnemyMove()
	{
		if(Play.enemyNumber>0)
		{
			for(int i = 0; i < Play.enemyNumber; i++)
			{
				if(!(Play.enemy[i] == null) && Play.timeStop == false)
				{
					if(Play.enemy[i].getPosition().x < Play.enemySpawnX[i]-2)
					{
						Play.enemyWalkRightLeft[i] = 1;
					}
					if(Play.enemy[i].getPosition().x > Play.enemySpawnX[i]+2)
					{
						Play.enemyWalkRightLeft[i] = 2;
					}
					if(Play.enemyWalkRightLeft[i] == 1)
					{
						Play.enemy[i].getBody().setLinearVelocity(1.3f, Play.enemy[i].getBody().getLinearVelocity().y);
					}
					if(Play.enemyWalkRightLeft[i] == 2)
					{
						Play.enemy[i].getBody().setLinearVelocity(-1.3f, Play.enemy[i].getBody().getLinearVelocity().y);
					}
				}
			}
		}	
	}
	*/
	
}
