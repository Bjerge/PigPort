package com.bj.pigport.entities.enemy;

import static com.bj.pigport.handlers.B2DVars.PPM;

import java.security.InvalidParameterException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.enemy.bosstypes.FlyingBall;
import com.bj.pigport.entities.enemy.controls.EnemyHit;
import com.bj.pigport.entities.enemy.controls.FlyingChargeController;
import com.bj.pigport.entities.enemy.controls.GroundChargeController;
import com.bj.pigport.entities.enemy.controls.LittleGroundMovement;
import com.bj.pigport.entities.enemy.controls.MissileController;
import com.bj.pigport.entities.enemy.controls.WarlockAttack;
import com.bj.pigport.entities.enemy.controls.MeleeWeaponAttack;
import com.bj.pigport.entities.enemy.enemytypes.BasicFlyingMinion;
import com.bj.pigport.entities.enemy.enemytypes.BasicMinion;
import com.bj.pigport.entities.enemy.enemytypes.GroundShooter;
import com.bj.pigport.entities.enemy.enemytypes.Tank;
import com.bj.pigport.entities.enemy.enemytypes.Warlock;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.handlers.MyInput;

public class Enemy extends B2DSprite{
	
	public EnemyData data;
	private float movementModifier = 0.05f;
	
	public Enemy(Body body, boolean boss,int type) {
		super(body);
		
		if(boss != true)
		{
			if(type == 1) data = new BasicMinion();	
			if(type == 2) data = new BasicFlyingMinion();	
			if(type == 3) data = new Warlock();	
			if(type == 4) data = new Tank();
			if(type == 5) data = new GroundShooter();
		}
		else
		{
			if(type == 1) data = new FlyingBall();	
		}
				
		TextureRegion[] sprites = TextureRegion.split(data.texEnemy, data.texEnemy.getWidth(), data.texEnemy.getHeight())[0];
		
		//setAnimation(sprites, 1 / 3f);
		
		setAnimationStreched(sprites, 1 / 3f, data.texEnemy.getWidth() * data.texStretch, data.texEnemy.getHeight() * data.texStretch);
	}
	
	public void enemyControllerV2()
	{
		if(this.data.active)
		{
			Vector2 vecEnemy = new Vector2(getBody().getPosition().x, getBody().getPosition().y);
			Vector2 vecEffect = new Vector2(Play.player.data.ballEffect.getBody().getPosition().x, Play.player.data.ballEffect.getBody().getPosition().y);
			Vector2 vecPlayer = new Vector2(Play.player.getBody().getPosition().x, Play.player.getBody().getPosition().y);
			
			double distance = (Math.sqrt(((vecEnemy.x - vecPlayer.x) * (vecEnemy.x - vecPlayer.x)) + ((vecEnemy.y - vecPlayer.y) * (vecEnemy.y - vecPlayer.y))));
			double distanceX = vecEnemy.x - vecPlayer.x;
			int angleEffect = (int) (Math.toDegrees(Math.atan2(vecEnemy.y - vecEffect.y , vecEnemy.x - vecEffect.x)));
			if(angleEffect < 0) angleEffect = (360 + angleEffect);	
			
			float cosEffect = (float) (Math.cos(Math.toRadians(angleEffect)));
			float sinEffect = (float) (Math.sin(Math.toRadians(angleEffect)));
			
			int anglePlayer = (int) (Math.toDegrees(Math.atan2(vecEnemy.y - vecPlayer.y , vecEnemy.x - vecPlayer.x)));
			if(anglePlayer < 0) anglePlayer = (360 + anglePlayer);	
			float cosPlayer = (float) (Math.cos(Math.toRadians(anglePlayer)));
			float sinPlayer = (float) (Math.sin(Math.toRadians(anglePlayer)));
			
			
			EnemyHit.EnemyHitControls(this, cosPlayer, sinPlayer, cosEffect, sinEffect);
			
			if(Play.timeStop == false && this.data.frozen != true && this.data.wind != true)
			{
				if(this.data.hitByMeleeTimer == 0)
				{
					if(this.data.groundCharge)GroundChargeController.GroundChargeControls(this, distance, vecEnemy.x, vecPlayer.x);
					if(this.data.flyingCharge)FlyingChargeController.FlyingChargeControls(this);
					if(this.data.groundLittleMovement) LittleGroundMovement.LittleGroundMovementControls(this);
					
					if(this.data.boss[0] == true)
					{
						float x = (Play.player.getPosition().x - getPosition().x) * 0.2f;
						float y = (Play.player.getPosition().y - getPosition().y) * 0.2f;
						
						getBody().setLinearVelocity(x, y);
					}
				}
				
				if(this.data.meleeWeaponAttack) MeleeWeaponAttack.MeleeWeaponAttackControls(this, distance, distanceX, vecPlayer.y);
				if(this.data.missile) MissileController.MissileControls(this , cosPlayer, sinPlayer, distance);				
				if(this.data.warlock) WarlockAttack.WarlockAttackControls(this, vecEnemy, vecPlayer, distance);
			}
			
			
			if(this.data.health < 1)
			{
				Loot.createItem(getPosition().x , getPosition().y, null);
				
				getBody().setTransform(1000, 1000, 0);
				this.data.active = false;
				getBody().setGravityScale(0);
			}
		}
	}
	
	public void enemyRender(SpriteBatch sb)
	{
		this.render(sb);
		for(int ii = 0; ii < this.data.missilesNumber; ii++)this.data.enemyMissiles[ii].render(sb);
		
		if(this.data.meleeWeaponAttack)
		{
			sb.begin();
			sb.draw(this.data.texWeapon, (this.getPosition().x * 100) - (this.data.texWeapon.getWidth()/2), 
					(this.getPosition().y * 100) - (this.data.texWeapon.getHeight()/2),
					this.data.texWeapon.getWidth() / 2, 
					this.data.texWeapon.getHeight() / 2, 
					this.data.texWeapon.getWidth(), 
					this.data.texWeapon.getHeight(),  
					1, 1, this.data.weaponRotation * this.data.direction, 0, 0, 
					this.data.texWeapon.getWidth(), 
					this.data.texWeapon.getHeight(), true, false);
			sb.end();
		}
		
		sb.begin();
		sb.draw(this.data.enemyHealth, (this.getPosition().x * 100) - 50, (this.getPosition().y * 100) + (this.data.getTexEnemy().getHeight()/2), ((float) (((100) / (float) (this.data.getMaxHealth())) * (this.data.getHealth()))) , 8);
		sb.draw(this.data.enemyHealthLeft, (this.getPosition().x * 100) + 50, (this.getPosition().y * 100) + (this.data.getTexEnemy().getHeight()/2),	 -((float) (100) - (((100) / (float) (this.data.getMaxHealth())) * (this.data.getHealth()))) , 8);
		sb.end();
	}
}


