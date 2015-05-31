package com.bj.pigport.entities.player;

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
import com.badlogic.gdx.physics.box2d.World;
import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class PlayerCreator {
	
	
	
	
	public PlayerCreator()
	{
		
	}
	
	public static void createPlayer(World world)
	{
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//create player
		bdef.position.set( PlayerData.playerSpawnX, PlayerData.playerSpawnY);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		shape.setAsBox(33 / PPM, 56 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_DUST | B2DVars.BIT_CAGE | B2DVars.BIT_TRAP | B2DVars.BIT_END | B2DVars.BIT_MOVINGPLATFORM | B2DVars.BIT_FIREARROW | B2DVars.BIT_LOOT;
		body.createFixture(fdef).setUserData("player");
		
		//create enemy sensor
		shape.setAsBox(30 / PPM, 50 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_ENEMY;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerHitEnemy");
		
		//create enemy sensor
		shape.setAsBox(30 / PPM, 50 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_MISSILE;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerHitMissile");
		
		//create foot sensor
		shape.setAsBox(20 / PPM, 2 / PPM, new Vector2(0, -54 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		//create foot sensor for platforms
		shape.setAsBox(20 / PPM, 2 / PPM, new Vector2(0, -54 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("footPLF");
		//create right sensor
		shape.setAsBox(2 / PPM, 55 / PPM, new Vector2(33 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_COLL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("rightSensor");
		//create left sensor
		shape.setAsBox(2 / PPM, 55 / PPM, new Vector2(-33 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_COLL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("leftSensor");
		//create right sensor
		shape.setAsBox(2 / PPM, 50 / PPM, new Vector2(33 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("rightSensorPLF");
		//create left sensor
		shape.setAsBox(2 / PPM, 50 / PPM, new Vector2(-33 / PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("leftSensorPLF");
		
		//Create Attack Radius
		CircleShape shapeC = new CircleShape();
		shapeC.setRadius(95 / PPM);
		shapeC.setPosition(new Vector2(0, 1 / PPM));
		fdef.shape = shapeC;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_ENEMY | B2DVars.BIT_OBJECTS;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("playerAttack");
		
		
		// create player
		PlayerData.player = new Player(body);
		body.setUserData(PlayerData.player);
		shape.dispose();
	}

}
