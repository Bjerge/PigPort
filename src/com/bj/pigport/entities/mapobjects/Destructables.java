package com.bj.pigport.entities.mapobjects;

//import com.badlogic.gdx.Gdx;
import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class Destructables extends B2DSprite{
	
	public static float Location[][];
	public static int number = 0;
	public static Destructables destructables [];
	
	public boolean active = true;
	public int health = 5;
	public boolean playerMeleeContact = false;
	public int takingDamageCD = 0;
	public Texture tex;
		
	public Destructables(Body body) {
		super(body);
				
		int randomNumber = (int) (Math.random()*2);
		tex = Game.res.getTexture("mapObject1");
		if(randomNumber == 1)tex = Game.res.getTexture("mapObject2");
		
		TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth(), tex.getHeight())[0];
		setAnimation(sprites, 1 / 3f);
		
	}
	
	public void DestructablesController()
	{
		if(active)
		{
			if(playerMeleeContact){
				health -= 10;
			}
			
			if(health < 1)
			{
				for(int i = 0; i < 10; i++)Loot.createItem(getPosition().x , getPosition().y, null);
				getBody().setTransform(1000, 1000, 0);
				getBody().setGravityScale(0);
				active = false;
			}
		}
	}
	
	
	public static void getDestructablesSpawn()
	{
		int i = 0;
		Play.ml = Play.tileMap.getLayers().get("destructables"); 
		
		Location = new float[20][2];
		destructables = new Destructables [20];
		
		if(Play.ml != null)
		{
			for(MapObject mo : Play.ml.getObjects())
			{
				BodyDef cdef = new BodyDef();
				cdef.type = BodyType.StaticBody;
				if(mo instanceof RectangleMapObject){
					float x =  (((RectangleMapObject) mo).getRectangle().x / PPM);
					float y =  (((RectangleMapObject) mo).getRectangle().y / PPM);
					Location[i][0] = x;
					Location[i][1] = y;
				}
				else if(mo instanceof EllipseMapObject)
				{
					float x =  (((EllipseMapObject) mo).getEllipse().x / PPM);
					float y =  (((EllipseMapObject) mo).getEllipse().y / PPM);
					Location[i][0] = x;
					Location[i][1] = y;
				}
				i++;
			}
		}
		number = i;
	}
	
	public static void createDestructables(){
				
		for(int i = 0; i < number; i++)
		{			
			BodyDef bdef = new BodyDef();
			FixtureDef fdef = new FixtureDef();
			PolygonShape shape = new PolygonShape();
			
			bdef.position.set( Location[i][0], Location[i][1]);
			
			bdef.type = BodyType.DynamicBody;
			Body body = Play.getWorld().createBody(bdef);
			destructables[i] = new Destructables(body);
						
			shape.setAsBox( destructables[i].tex.getWidth() / 200f, destructables[i].tex.getHeight() / 200f);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_OBJECTS;
			fdef.filter.maskBits = B2DVars.BIT_COLL; 
			body.createFixture(fdef).setUserData("mapObjects");
			
			shape.setAsBox( destructables[i].tex.getWidth() / 200f, destructables[i].tex.getHeight() / 200f);
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_OBJECTS;
			fdef.filter.maskBits = B2DVars.BIT_WEAPON;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("destructables"+i);
			
			body.setUserData(destructables[i]);
			
			shape.dispose();
		}	
	}
}
