package com.bj.pigport.entities.player.abilities;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.handlers.MyInput;

public class Weapon extends B2DSprite{
	
	public Texture weaponTex;
	
	// attackDelay + attackTime + attackCD = attack speed. attackTime is the same for all weapons but delay and CD can vary greatly
	private int attackStage = 0;
	private int attackDelay = 0;
	private int attackTime = 20;
	private int attackCD = 0;
	
	private int attacker = 0;
	
	//passiveAngel is the angel at which the weapon is held at when not used. weapons with larger attackDelay will carry it lower to the ground
	private float passiveAngel = 0;
	private int range = 0;
	
	public Weapon(Body body, String string) {
		super(body);
		
		weaponTex = Game.res.getTexture(string);
		TextureRegion[] sprites = TextureRegion.split(weaponTex, 1, 1)[0];
		
		setAnimation(sprites, 1 / 12f);
		statGenerator(string);
		Play.player.data.weaponRotation = this.passiveAngel;
	}
	
	public void WeaponController()
	{
		this.getBody().setTransform(Play.player.getPosition().x - (40 * Play.player.data.direction / PPM), Play.player.getPosition().y, 
				(float) Math.toRadians(Play.player.data.weaponRotation * Play.player.data.direction));
		
		this.getBody().setLinearVelocity(0.001f, 0);
		
		if(MyInput.isPressed(MyInput.BUTTON2) && attackStage == 0)
		{
			attackStage = 1;
			Play.player.data.directionLock = true;
		}
		if(attackStage == 1)
		{
			Play.player.data.weaponRotation += (-30f - passiveAngel)/(float) (attackDelay);
			attacker++;
			if(attacker > attackDelay)
			{
				attackStage = 2;
				attacker = 0;
				collThinManager(1);
			}
		}
		else if(attackStage == 2)
		{
			Play.player.data.weaponRotation += 180f/(float) (attackTime);
			attacker++;
			if(attacker > attackTime)
			{
				attackStage = 3;
				attacker = 0;
				collThinManager(0);
			}
		}
		else if(attackStage == 3)
		{
			Play.player.data.weaponRotation += (passiveAngel - 160f)/(float) (attackCD);
			attacker++;
			if(attacker > attackCD)
			{
				attackStage = 0;
				attacker = 0;
				Play.player.data.weaponRotation = passiveAngel;
				Play.player.data.directionLock = false;
			}
		}
	}
	
	public void collThinManager(int i)
	{
		Filter filter = Play.player.data.weapon.getBody().getFixtureList().get(0).getFilterData();
		short bits = filter.maskBits;
		if(i == 0)bits = B2DVars.BIT_LOOT;
		if(i == 1)bits = B2DVars.BIT_OBJECTS | B2DVars.BIT_ENEMY;
		filter.maskBits = bits;
		Play.player.data.weapon.getBody().getFixtureList().get(0).setFilterData(filter);
	}
	
	private void statGenerator(String weapon)
	{
		if(weapon == "axe1h")
		{
			attackDelay = 5;
			passiveAngel = -20;
			attackCD = 20;
		}
		if(weapon == "axe2h")
		{
			attackDelay = 15;
			passiveAngel = 20;
			attackCD = 30;
		}
		if(weapon == "hammer1h")
		{
			attackDelay = 5;
			passiveAngel = -20;
			attackCD = 20;
		}
		if(weapon == "hammer2h")
		{
			attackDelay = 60;
			passiveAngel = 60;
			attackCD = 60;
		}
		if(weapon == "spear1h")
		{
			attackDelay = 5;
			passiveAngel = -20;
			attackCD = 20;
		}
		if(weapon == "spear2h")
		{
			attackDelay = 30;
			passiveAngel = 40;
			attackCD = 30;
		}
		if(weapon == "sword1h")
		{
			attackDelay = 5;
			passiveAngel = -20;
			attackCD = 20;
		}
		if(weapon == "sword2h")
		{
			attackDelay = 15;
			passiveAngel = 20;
			attackCD = 30;
		}		
	}
	
	public static void createWeapon(World world, String string){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//create ballEffect
		bdef.position.set(1000, 1000);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		Play.player.data.weapon = new Weapon(body, string);
		
		shape.setAsBox(Play.player.data.weapon.weaponTex.getWidth() / PPM / 4, Play.player.data.weapon.weaponTex.getHeight() / PPM / 4, new Vector2(0, Play.player.data.weapon.weaponTex.getHeight() / PPM / 4), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_WEAPON;
		fdef.filter.maskBits = B2DVars.BIT_ENEMY | B2DVars.BIT_OBJECTS;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("weapon");		
		
		body.setUserData(Play.player.data.weapon);
		
		body.setGravityScale(0);		
		shape.dispose();
	}	
}