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
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class EnemyCreator {
	
	
	
	
	public EnemyCreator()
	{
		
	}
	
	public static void createEnemy()
	{
		Play.enemy = new Enemy[10];
		Play.enemyNumber = 0;
		
		createEnemyGround();
		createEnemyFlying();
		createFlyingBall();
	}
	
	public static void createEnemyGround()
	{
		MapLayer ml = null;
		if(Play.difficulty == 1)		ml = Play.tileMap.getLayers().get("enemyEasy");
		else if(Play.difficulty == 2)	ml = Play.tileMap.getLayers().get("enemyMedium");
		else if(Play.difficulty == 3)	ml = Play.tileMap.getLayers().get("enemyHard");
	//MapLayer ml = tileMap.getLayers().get("enemyEasy");
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;
			bdef.position.set(1000, 1000);
			Body body = Play.getWorld().createBody(bdef);
			
			PolygonShape shapeT = new PolygonShape();
			FixtureDef fdef = new FixtureDef();
			
			// enemy fixture
			shapeT.setAsBox(65 / 2 / PPM, 50 / 2 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			body.createFixture(fdef).setUserData("Enemy");
			
			//enemy being attacked by player
			shapeT.setAsBox(65 / 2 / PPM, 50 / 2 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByPlayerMelee"+Play.enemyNumber);
			
			//enemy ballEffect sensor
			shapeT.setAsBox(65 / 2 / PPM, 50 / 2 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_BALLEFFECT;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByBallEffect"+Play.enemyNumber);
			
			//enemy right hand sensor
			shapeT.setAsBox(5 / PPM, 23  / PPM, new Vector2(65 / 2 / PPM, 0), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyRightHand"+Play.enemyNumber);
			
			//enemy Left hand sensor
			shapeT.setAsBox(5 / PPM, 23 / PPM, new Vector2(-65 / 2 / PPM, 0), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyLeftHand"+Play.enemyNumber);
			
			Play.enemy[Play.enemyNumber] = new Enemy(body, false, 1);
			body.setUserData(Play.enemy[Play.enemyNumber]);
			shapeT.dispose();
			
			//Play.enemy[Play.enemyNumber].getBody().setGravityScale(0);
			
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
			
			Play.enemyNumber++;
		}	
	}
	
	
	
	
	public static void createEnemyFlying()
	{
		MapLayer ml = null;
		ml = Play.tileMap.getLayers().get("BasicFlyingMinion");
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;
			bdef.position.set(1000, 1000);
			Body body = Play.getWorld().createBody(bdef);
			
			CircleShape shape = new CircleShape();
			FixtureDef fdef = new FixtureDef();
			
			// enemy fixture
			shape.setRadius(25 / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("Enemy");
			
			//enemy being attacked by player
			shape.setRadius(25 / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByPlayerMelee"+Play.enemyNumber);
			
			//enemy ballEffect sensor
			shape.setRadius(25 / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_BALLEFFECT;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByBallEffect"+Play.enemyNumber);
			
			Play.enemy[Play.enemyNumber] = new Enemy(body, false, 2);
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
				
				EnemyMissile.createEnemyMissiles(Play.enemy[Play.enemyNumber].data.missilesNumber);
			
			Play.enemyNumber++;
		}	
	}	
	
	
	
	public static void createFlyingBall()
	{
		MapLayer ml = null;
		ml = Play.tileMap.getLayers().get("BossFlyingBall");
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.DynamicBody;
			bdef.position.set(1000, 1000);
			Body body = Play.getWorld().createBody(bdef);
			
			CircleShape shape = new CircleShape();
			FixtureDef fdef = new FixtureDef();
			
			// enemy fixture
			shape.setRadius(25 * 4 / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData("Enemy");
			
			//enemy being attacked by player
			shape.setRadius(25 * 4 / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByPlayerMelee"+Play.enemyNumber);
			
			//enemy ballEffect sensor
			shape.setRadius(25 * 4 / PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_BALLEFFECT;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("enemyHitByBallEffect"+Play.enemyNumber);
			
			Play.enemy[Play.enemyNumber] = new Enemy(body, true, 1);
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
				//EnemyMissile.createEnemyMissiles();
			
			EnemyMissile.createEnemyMissiles(Play.enemy[Play.enemyNumber].data.missilesNumber);
			
			Play.enemyNumber++;
		}	
	}	
}
