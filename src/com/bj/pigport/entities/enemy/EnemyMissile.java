package com.bj.pigport.entities.enemy;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;


public class EnemyMissile extends B2DSprite{

	public EnemyMissile(Body body, float stretch) {
		super(body);
		
		Texture tex = Game.res.getTexture("fireBall");
		TextureRegion[] sprites = TextureRegion.split(tex, 25, 25)[0];
		
		//setAnimation(sprites, 1 / 12f);
		setAnimationStreched(sprites, 1 / 12f, 25 * stretch, 25 * stretch);
	}

	public static void createEnemyMissiles(int number, float stretch, boolean colission)
	{

		Play.enemy[Play.enemyNumber].data.enemyMissiles = new EnemyMissile [number];
					
		for(int i = 0; i < number; i++)
		{
			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			CircleShape shape = new CircleShape();
			
			bdef.position.set( 1000, 1000);
			bdef.type = BodyType.DynamicBody;
			Body body = Play.getWorld().createBody(bdef);
			
			Play.enemy[Play.enemyNumber].data.enemyMissiles[i] = new EnemyMissile(body, stretch);
			
			shape.setRadius(12 / PPM * stretch);
			fdef.shape = shape;
			//fdef.filter.categoryBits = B2DVars.BIT_MISSILE;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyMissileHitPlayer" + Play.enemyNumber + "." + i);
			
			if(colission)
			{
				shape.setRadius(12 / PPM * stretch);
				fdef.shape = shape;
				//fdef.filter.categoryBits = B2DVars.BIT_MISSILE;
				fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
				fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_PLAYER;
				fdef.isSensor = true;
				body.createFixture(fdef).setUserData("enemyMissileColission" + Play.enemyNumber + "." + i);
			}
			
			body.setUserData("enemyThrowingWeapon" + Play.enemyNumber);
			Play.enemy[Play.enemyNumber].data.enemyMissiles[i].getBody().setGravityScale(0);		
			
			shape.dispose();
		}
	}
	
	
	public static EnemyMissile[] createEnemyMissile(EnemyMissile [] throwing, int amount, float stretch){
		
		throwing = new EnemyMissile [amount];
		
		for(int i = 0; i < amount; i++)
		{
			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			PolygonShape shape = new PolygonShape();
			
			bdef.position.set( 1000, 1000);
			bdef.type = BodyType.DynamicBody;
			Body body = Play.getWorld().createBody(bdef);
			
			throwing[i] = new EnemyMissile(body, stretch);
			
			shape.setAsBox(22/PPM, 22/PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_MISSILE;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("Thrown");
			
			body.setUserData("enemyThrowingWeapon" + i);
			throwing[i].getBody().setGravityScale(0);		
			
			shape.dispose();
		}
		return throwing;
	}
}

