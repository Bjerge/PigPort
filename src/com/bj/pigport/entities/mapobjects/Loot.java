package com.bj.pigport.entities.mapobjects;

//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.bj.pigport.handlers.GameButtonStandard;
import com.bj.pigport.handlers.LootButton;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.states.Play;

public class Loot extends B2DSprite{
	
	public Texture tex;
	
	public static Loot [] loot = new Loot[200];
	public static int lootNumber;
	public static LootButton [] lootButton = new LootButton [200];
	
	public int itemNumber = 0;
	public int itemType = 0;
	private Items lootItem;
	private String lootName;
	//public float buttonYLocation	
	
	public Loot(Body body, int number, Items item) {
		super(body);
		
		tex = Game.res.getTexture("fireBall");
		generateItem(item);
		
		TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth(), tex.getHeight())[0];
		setAnimation(sprites, 1 / 3f);
		
		itemNumber = number;
	}
	
	private void generateItem(Items item)
	{
		if(item == null)
		{
			itemType = (int) (Math.random()*5);
			
			if(itemType == 1)itemType = 1;
			else if(itemType == 2)itemType = 2;
			else itemType = 0;
			
			if(itemType == 0) 
			{
				tex = Game.res.getTexture("Energy");
				lootItem = new Items(0);
				lootName = lootItem.itemName;
			}
			if(itemType == 1)
			{
				tex = Game.res.getTexture("Meat");
				lootItem = new Items(1);
				lootName = lootItem.itemName;
			}
			if(itemType == 2) 
			{
				tex = Game.res.getTexture("Coins");
				lootName = "Coins";
			}
		}
		else
		{
			lootItem = item;
			lootName = item.itemName;
			if(item.itemName == "Meat") tex = Game.res.getTexture("Meat");
			else tex = Game.res.getTexture("Energy");
		}
	}
	
	public void LootButtonController(OrthographicCamera cam)
	{
		int buttonY = 0;
		float thisX = this.getBody().getPosition().x;
		float thisY = this.getBody().getPosition().y;
		
		for(int i = 0; i < itemNumber; i++)
		{
			float otherX = Loot.loot[i].getBody().getPosition().x;
			float otherY = Loot.loot[i].getBody().getPosition().y;
			
			if((Math.sqrt(((thisX - otherX) * (thisX - otherX)) + 
					((thisY - otherY) * (thisY - otherY)))) < 100f/100f) buttonY++;
		}
		
		double distance = Math.sqrt(((thisX - Play.player.getBody().getPosition().x) * (thisX - Play.player.getBody().getPosition().x)) + ((thisY - Play.player.getBody().getPosition().y) * (thisY - Play.player.getBody().getPosition().y)));
		if(lootButton[itemNumber] != null) 
			if(lootButton[itemNumber].isClicked() && distance < 2) LootController();
		
		float x = this.getBody().getPosition().x * 100;
		float y = (this.getBody().getPosition().y * 100) + 20 + (buttonY * 16);
		lootButton[itemNumber] = new LootButton(lootName, x, y, cam, 0.4f);
	}
	
	public void LootController()
	{
		if(Play.player.data.inventorySize < 20)
		{
			if(itemType == 0)Items.addItemToList(lootItem);
			else if(itemType == 1)Items.addItemToList(lootItem);
			else Play.player.data.gold += 10;
			getBody().setTransform(1000, 1000, 0);
			getBody().setGravityScale(0);
		}
	}
	
	public static void createItem(float x, float y, Items item){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//create player
		bdef.position.set( x, y);
		bdef.type = BodyType.DynamicBody;
		Body body = Play.getWorld().createBody(bdef);
		
		if(item == null)loot[lootNumber] = new Loot(body, lootNumber, null);
		else loot[lootNumber] = new Loot(body, lootNumber, item);
		
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