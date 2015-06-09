package com.bj.pigport.entities.enemy;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.entities.enemy.EnemyMissile;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class EnemyCreator {
	
	
	
	
	public EnemyCreator()
	{
		
	}
	
	public static void createEnemy()
	{
		Play.enemy = new Enemy[50];
		Play.enemyNumber = 0;
		
		createGroundEnemy("basicGround", false, 1);
		createGroundEnemy("warlock", false, 3);
		createGroundEnemy("tank", false, 4);
		createGroundEnemy("groundShooter", false, 5);
		
		createFlyingEnemy("basicFlying", false, 2);
		createFlyingEnemy("boss1", true, 1);
		
	}
	
	public static void createFlyingEnemy(String string, boolean boss, int type)
	{
		float xw = 0;
		float yh = 0;
		
		MapLayer ml = null;
		ml = Play.tileMap.getLayers().get(string);
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;
			bdef.position.set(1000, 1000);
			Body body = Play.getWorld().createBody(bdef);
			
			Play.enemy[Play.enemyNumber] = new Enemy(body, boss, type);
			
			xw = Play.enemy[Play.enemyNumber].data.getTexEnemy().getWidth() * Play.enemy[Play.enemyNumber].data.texStretch / 2;
			yh = Play.enemy[Play.enemyNumber].data.getTexEnemy().getHeight() * Play.enemy[Play.enemyNumber].data.texStretch / 2;
			
			CircleShape shape = new CircleShape();
			FixtureDef fdef = new FixtureDef();
			/*
			// enemy fixture
			shape.setRadius(xw / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("Enemy");
			*/
			shape.setRadius(xw / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemy");
			
			//enemy being attacked by player
			shape.setRadius(xw / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_WEAPON;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByPlayerMelee"+Play.enemyNumber);
			
			//enemy ballEffect sensor
			shape.setRadius(xw / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_BALL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByBallEffect"+Play.enemyNumber);
			
			body.setUserData(Play.enemy[Play.enemyNumber]);
			shape.dispose();
			
			Play.enemy[Play.enemyNumber].getBody().setGravityScale(0);
			
			if(mo instanceof RectangleMapObject){
				float x =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				float y =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				Play.enemy[Play.enemyNumber].getBody().setTransform(x, y, 0);
				Play.enemy[Play.enemyNumber].data.SpawnX = x;
				Play.enemy[Play.enemyNumber].data.SpawnY = y;				
			}
			else if(mo instanceof EllipseMapObject){
				float x =  ((EllipseMapObject) mo).getEllipse().x / PPM;
				float y =  ((EllipseMapObject) mo).getEllipse().y / PPM;
				Play.enemy[Play.enemyNumber].getBody().setTransform(x, y, 0);
				Play.enemy[Play.enemyNumber].data.SpawnX = x;
				Play.enemy[Play.enemyNumber].data.SpawnY = y;	
			}
			
			//if(Play.enemy[Play.enemyNumber].data.missile)
				
			if(Play.enemy[Play.enemyNumber].data.missilesNumber > 0)
				EnemyMissile.createEnemyMissiles(Play.enemy[Play.enemyNumber].data.missilesNumber, Play.enemy[Play.enemyNumber].data.missileStretch, Play.enemy[Play.enemyNumber].data.missileColission);
			
			Play.enemyNumber++;
		}	
	}
	
	
	public static void createGroundEnemy(String string, boolean boss, int type)
	{
		float xw = 0;
		float yh = 0;
		
		MapLayer ml = Play.tileMap.getLayers().get(string);
	//MapLayer ml = tileMap.getLayers().get("enemyEasy");
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;
			bdef.position.set(1000, 1000);
			Body body = Play.getWorld().createBody(bdef);
			
			Play.enemy[Play.enemyNumber] = new Enemy(body, false, type);
			
			xw = Play.enemy[Play.enemyNumber].data.getTexEnemy().getWidth() * Play.enemy[Play.enemyNumber].data.texStretch;
			yh = Play.enemy[Play.enemyNumber].data.getTexEnemy().getHeight() * Play.enemy[Play.enemyNumber].data.texStretch;
			
			PolygonShape shapeT = new PolygonShape();
			FixtureDef fdef = new FixtureDef();
			
			// enemy fixture
			shapeT.setAsBox(xw / 2 / PPM, yh / 2 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_COLLTHIN | B2DVars.BIT_WEAPON | B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("Enemy");
			
			//enemy being attacked by player
			shapeT.setAsBox(xw / 2 / PPM, yh / 2 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_WEAPON;// | B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByPlayerMelee"+Play.enemyNumber);
			
			//enemy ballEffect sensor
			shapeT.setAsBox(xw / 2 / PPM, yh / 2 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_BALL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByBallEffect"+Play.enemyNumber);
			
			//enemy right hand sensor
			shapeT.setAsBox(5 / PPM, (yh-4)/2  / PPM, new Vector2(xw / 2 / PPM, 0), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyRightHand"+Play.enemyNumber);
			
			//enemy Left hand sensor
			shapeT.setAsBox(5 / PPM, (yh-4)/2 / PPM, new Vector2(-xw / 2 / PPM, 0), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyLeftHand"+Play.enemyNumber);
			
			body.setUserData(Play.enemy[Play.enemyNumber]);
			shapeT.dispose();
			
			if(Play.enemyDirectionChanger)
			{
				Play.enemy[Play.enemyNumber].data.enemyWalkRightLeft = 1;
				Play.enemyDirectionChanger = false;
			}
			else
			{
				Play.enemy[Play.enemyNumber].data.enemyWalkRightLeft = -1;
				Play.enemyDirectionChanger = true;
			}
			
			if(mo instanceof RectangleMapObject){
				float x =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				float y =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				Play.enemy[Play.enemyNumber].getBody().setTransform(x, y, 0);
				Play.enemy[Play.enemyNumber].data.SpawnX = x;
				Play.enemy[Play.enemyNumber].data.SpawnY = y;				
			}
			else if(mo instanceof EllipseMapObject){
				float x =  ((EllipseMapObject) mo).getEllipse().x / PPM;
				float y =  ((EllipseMapObject) mo).getEllipse().y / PPM;
				Play.enemy[Play.enemyNumber].getBody().setTransform(x, y, 0);
				Play.enemy[Play.enemyNumber].data.SpawnX = x;
				Play.enemy[Play.enemyNumber].data.SpawnY = y;	
			}
			
			if(Play.enemy[Play.enemyNumber].data.missilesNumber > 0)
				EnemyMissile.createEnemyMissiles(Play.enemy[Play.enemyNumber].data.missilesNumber, Play.enemy[Play.enemyNumber].data.missileStretch, Play.enemy[Play.enemyNumber].data.missileColission);
			
			Play.enemyNumber++;
		}	
	}
}
