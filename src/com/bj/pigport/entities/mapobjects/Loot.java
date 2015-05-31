package com.bj.pigport.entities.mapobjects;

//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.items.Items;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class Loot extends B2DSprite{
	
	public Texture tex;
	
	public static Loot [] loot = new Loot[20];
	public static int lootNumber;
	
	public int itemType = 0;
	
	public boolean pickedUp = false;
	
	
	public Loot(Body body) {
		super(body);
		
		tex = Game.res.getTexture("fireBall");
		generateItem();
		
		TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth(), tex.getHeight())[0];
		setAnimation(sprites, 1 / 3f);
		
	}
	
	private void generateItem()
	{
		itemType = (int) (Math.random()*3);
		
		if(itemType == 0) tex = Game.res.getTexture("Coins");
		if(itemType == 1) tex = Game.res.getTexture("Meat");
		if(itemType == 2) tex = Game.res.getTexture("Energy");
	}
	
	public void LootController()
	{
		if(this.pickedUp)
		{
			if(itemType == 0) PlayerData.gold += 10;
			if(itemType == 1)
			{
				PlayerData.player.health += 10;
				if(PlayerData.player.health > PlayerData.player.healthMax) PlayerData.player.health = PlayerData.player.healthMax;
			}
			if(itemType == 2) 
			{
				Items itemDrop = new Items();
				Items.addItemToList(itemDrop);
			}
			getBody().setTransform(1000, 1000, 0);
			getBody().setGravityScale(0);
			pickedUp = false;
		}
	}
	
	public static void createItem(float x, float y){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//create player
		bdef.position.set( x, y);
		bdef.type = BodyType.DynamicBody;
		Body body = Play.getWorld().createBody(bdef);
		
		loot[lootNumber] = new Loot(body);
		
		shape.setAsBox( 0.25f, 0.25f);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_LOOT; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL;
		body.createFixture(fdef).setUserData("loot");
		
		//Enemy Sensor
		shape.setAsBox( 0.25f, 0.25f);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_LOOT;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("loot"+lootNumber);
		
		// create player
		body.setUserData(loot[lootNumber]); 
		
		loot[lootNumber].getBody().setGravityScale(1f);
		
		shape.dispose();
		lootNumber++;
	}
}