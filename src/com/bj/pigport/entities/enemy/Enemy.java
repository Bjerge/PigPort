package com.bj.pigport.entities.enemy;

import static com.bj.pigport.handlers.B2DVars.PPM;

import java.security.InvalidParameterException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.enemy.bosstypes.FlyingBall;
import com.bj.pigport.entities.enemy.enemytypes.BasicFlyingMinion;
import com.bj.pigport.entities.enemy.enemytypes.BasicMinion;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.player.PlayerData;

public class Enemy extends B2DSprite{
	
	public EnemyData data;
	
	public Enemy(Body body, boolean boss,int type) {
		super(body);
		
		if(boss != true)
		{
			if(type == 1) data = new BasicMinion();	
			if(type == 2) data = new BasicFlyingMinion();	
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
			Vector2 vecEffect = new Vector2(PlayerData.ballEffect.getBody().getPosition().x, PlayerData.ballEffect.getBody().getPosition().y);
			Vector2 vecPlayer = new Vector2(PlayerData.player.getBody().getPosition().x, PlayerData.player.getBody().getPosition().y);
			
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
			
			
			
			
			if(this.data.hitByMelee && this.data.hitByMeleeTimer == 0 && Play.playerAttackingMelee)
			{
				this.data.hitByMeleeTimer = 50;
				this.data.health -= 20;
				
				getBody().setLinearVelocity(0 , 0);
				float forceX = (cosPlayer * 200);
				float forceY = (sinPlayer * 150);
				getBody().applyForceToCenter(forceX , forceY, true);
				
				if(this.data.flying)this.data.setFlyingState(0);
			}
			if(this.data.hitByMeleeTimer > 0) this.data.hitByMeleeTimer --;			
			
			
			
			
			
			if(this.data.hitByBallEffect && this.data.hitByBallEffectTimer == 0)
			{
				if(PlayerData.ballFrozen)
				{
					this.data.frozen = true;
					this.data.frozenTimer = 100;
				}
				
				if(PlayerData.ballFire)
				{
					this.data.fire = true;
					this.data.fireTimer = 200;
				}
				
				if(PlayerData.ballWind)
				{
					this.data.wind = true;
					this.data.windTimer = 101;
				}
				
				this.data.hitByBallEffectTimer = 20;
			}
			if(this.data.hitByBallEffectTimer > 0) this.data.hitByBallEffectTimer --;
			if(this.data.hitByBallEffectTimer == 0) this.data.hitByBallEffect = false;
			
			if(this.data.frozen)
			{
				this.data.frozenTimer--;
				if(this.data.frozenTimer == 0) this.data.frozen = false;
			}
			
			if(this.data.fire)
			{
				if(this.data.fireTimer % 50 == 0) this.data.health -= 5;
				
				this.data.fireTimer--;
				if(this.data.fireTimer == 0) this.data.fire = false;
			}
			
			if(this.data.wind)
			{
				if(this.data.windTimer == 100)
				{
					getBody().setLinearVelocity(0 , 0);
					float forceX = cosEffect * 200;
					float forceY = (sinEffect * 150) + 200;
					getBody().applyForceToCenter(forceX , forceY, true);
				}
				
				this.data.windTimer--;
				if(this.data.windTimer == 0) this.data.wind = false;
			}
			
			if(Play.timeStop == false && this.data.frozen != true && this.data.wind != true && this.data.hitByMeleeTimer == 0)
			{
				
				
				if(this.data.ground)
				{
					if(distance < 3)
					{
						
						if(this.data.lockTimer == 0)
						{
							if(vecEnemy.x - vecPlayer.x < 0) this.data.enemyWalkRightLeft = 1;
							else this.data.enemyWalkRightLeft = -1;
							this.data.lockTimer = 100;
						}
											
						if(this.data.lockTimer > 0) this.data.lockTimer--;
						getBody().setLinearVelocity((float) (1.3f * this.data.enemyWalkRightLeft), getBody().getLinearVelocity().y);
					}
					else
					{
						if(getPosition().x < this.data.SpawnX-2 && this.data.enemyWalkRightLeft == -1)
						{
							this.data.enemyWalkRightLeft = 1;
						}
						else if(getPosition().x > this.data.SpawnX+2 && this.data.enemyWalkRightLeft == 1)
						{
							this.data.enemyWalkRightLeft = -1;
						}
						getBody().setLinearVelocity(this.data.enemyWalkRightLeft * 1.3f, getBody().getLinearVelocity().y);
					}
				}
				
				if(this.data.flying)
				{
					
					if(this.data.getFlyingState() == 0)
					{
						this.data.setFlyPosX(getPosition().x);
						if(getPosition().y < PlayerData.player.getPosition().y) {this.data.setFlyPosY(getPosition().y + 2); this.data.setFlyRotation(270);}
						else 											  {this.data.setFlyPosY(getPosition().y - 2); this.data.setFlyRotation(90);}
						
						if(getPosition().y < PlayerData.player.getPosition().y && getPosition().x > PlayerData.player.getPosition().x)this.data.setFlyingState(1);
						if(getPosition().y > PlayerData.player.getPosition().y && getPosition().x > PlayerData.player.getPosition().x)this.data.setFlyingState(2);
						if(getPosition().y < PlayerData.player.getPosition().y && getPosition().x < PlayerData.player.getPosition().x)this.data.setFlyingState(3);
						if(getPosition().y > PlayerData.player.getPosition().y && getPosition().x < PlayerData.player.getPosition().x)this.data.setFlyingState(4);
					}
					if(this.data.getFlyingState() == 1 | this.data.getFlyingState() == 2 | this.data.getFlyingState() == 3 | this.data.getFlyingState() == 4)
					{
						getBody().setTransform( (float) ((this.data.getFlyPosX()) + (2f * Math.cos((this.data.getFlyRotation())/58f))) , (float) ((this.data.getFlyPosY()) + (2f * Math.sin((this.data.getFlyRotation())/58f))) , 0);
						if(this.data.getFlyingState() == 1 | this.data.getFlyingState() == 4) this.data.setFlyRotation(this.data
								.getFlyRotation() + 1);
						if(this.data.getFlyingState() == 2 | this.data.getFlyingState() == 3) this.data.setFlyRotation(this.data
								.getFlyRotation() - 1);
						if(this.data.getFlyingState() == 1 | this.data.getFlyingState() == 4) if(this.data.getFlyRotation() == 360) this.data.setFlyRotation(0);
						if(this.data.getFlyingState() == 2 | this.data.getFlyingState() == 3) if(this.data.getFlyRotation() == 0) this.data.setFlyRotation(360);
						if(this.data.getFlyingState() == 1 | this.data.getFlyingState() == 3) if(this.data.getFlyRotation() == 90) this.data.setFlyingState(5);
						if(this.data.getFlyingState() == 2 | this.data.getFlyingState() == 4) if(this.data.getFlyRotation() == 270) this.data.setFlyingState(5);
					}
					else if(this.data.getFlyingState() == 5)
					{
						float x = (PlayerData.player.getPosition().x - getPosition().x) * 0.8f;
						float y = (PlayerData.player.getPosition().y - getPosition().y) * 0.8f;
						
						getBody().setLinearVelocity(x, y);
						
						if( (Math.sqrt (Math.pow(x,2) + Math.pow(y,2)))  < 3) this.data.setFlyingState(6);
						
						if(this.data.getFlyingState() == 6)
						{
							getBody().setLinearVelocity(x, y);
							this.data.setFlyingState(7);
						}
					}
					
					if(this.data.getFlyingState() == 7)
					{
						this.data.setFlyChargeTimer(this.data
								.getFlyChargeTimer() + 1);
						if(this.data.getFlyChargeTimer() > 120)
						{
							this.data.setFlyingState(0);
							this.data.setFlyChargeTimer(0);
						}
					}
				}
				
				
				
				if(this.data.missile)
				{
					
					this.data.missileCDTimer++;
					if(this.data.missileCDTimer == 200)
					{
						this.data.missileShootTimer = (this.data.missilesNumber * 20) + 5;
						this.data.missilesNextToFire = this.data.missilesNumber - 1;
					}
					
					if(this.data.missileShootTimer > 0) 
					{
						this.data.missileShootTimer--;
					
						if(this.data.missileShootTimer % 20 == 0)
						{
							this.data.enemyMissiles[this.data.missilesNextToFire].getBody().setTransform(getBody().getPosition(), 0);
							this.data.enemyMissiles[this.data.missilesNextToFire].getBody().setLinearVelocity(0 , 0);
							
							float forceX = (cosPlayer * 150);
							float forceY = (sinPlayer * 150);
							this.data.enemyMissiles[this.data.missilesNextToFire].getBody().applyForceToCenter(-forceX , -forceY, true);
							
							this.data.missilesNextToFire--;
							
							if(this.data.missilesNextToFire == -1)
							{
								this.data.missileShootTimer = 0;
								this.data.missileCDTimer = 0;
							}
						}
					}
					
					
					
					
					
					if(this.data.enemyMissileReset)
					{
						//this.data.enemyMissiles.getBody().setTransform(1000, 1000, 0);
						//this.data.enemyMissiles.getBody().setLinearVelocity(0 , 0);
						//this.data.enemyMissileReset = false;
					}
				}
				
				
				
				
				if(this.data.boss[0] == true)
				{
					float x = (PlayerData.player.getPosition().x - getPosition().x) * 0.2f;
					float y = (PlayerData.player.getPosition().y - getPosition().y) * 0.2f;
					
					getBody().setLinearVelocity(x, y);
				}
				
				
				
				
				
				
			}
			
			
			if(this.data.health < 1)
			{
				Loot.createItem(getPosition().x , getPosition().y);
				
				getBody().setTransform(1000, 1000, 0);
				this.data.active = false;
				getBody().setGravityScale(0);
			}
		}
	}
}


