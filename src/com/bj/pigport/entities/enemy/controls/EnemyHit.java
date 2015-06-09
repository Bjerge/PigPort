package com.bj.pigport.entities.enemy.controls;

import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.entities.player.PlayerController;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.states.Play;

public class EnemyHit {
	public EnemyHit()
	{
		
	}
	
	
	public static void EnemyHitControls(Enemy enemy, float cosP, float sinP, float cosE, float sinE)
	{		
		if(enemy.data.hitByMelee && enemy.data.hitByMeleeTimer == 0)
		{
			enemy.data.hitByMeleeTimer = 50;
			enemy.data.health -= Play.player.data.meleeDamageAmount;
			
			enemy.getBody().setLinearVelocity(0 , 0);
			float forceX = (cosP * 200);
			float forceY = (sinP * 150);
			enemy.getBody().applyForceToCenter(forceX , forceY, true);
			
			if(enemy.data.flyingCharge)enemy.data.setFlyingState(0);
		}
		if(enemy.data.hitByMeleeTimer > 0) enemy.data.hitByMeleeTimer --;			
		
		
		
		
		
		if(enemy.data.hitByBallEffect && enemy.data.hitByBallEffectTimer == 0)
		{
			if(Play.player.data.ballFrozen)
			{
				enemy.data.frozen = true;
				enemy.data.frozenTimer = 100;
			}
			
			if(Play.player.data.ballFire)
			{
				enemy.data.fire = true;
				enemy.data.fireTimer = 200;
			}
			
			if(Play.player.data.ballWind)
			{
				enemy.data.wind = true;
				enemy.data.windTimer = 101;
			}
			
			enemy.data.hitByBallEffectTimer = 20;
		}
		if(enemy.data.hitByBallEffectTimer > 0) enemy.data.hitByBallEffectTimer --;
		if(enemy.data.hitByBallEffectTimer == 0) enemy.data.hitByBallEffect = false;
		
		if(enemy.data.frozen)
		{
			enemy.data.frozenTimer--;
			if(enemy.data.frozenTimer == 0) enemy.data.frozen = false;
		}
		
		if(enemy.data.fire)
		{
			if(enemy.data.fireTimer % 50 == 0) enemy.data.health -= (Play.player.data.mindDamageAmount) / 4;
			
			enemy.data.fireTimer--;
			if(enemy.data.fireTimer == 0) enemy.data.fire = false;
		}
		
		if(enemy.data.wind)
		{
			if(enemy.data.windTimer == 100)
			{
				enemy.getBody().setLinearVelocity(0 , 0);
				float forceX = cosE * 200;
				float forceY = (sinE * 150) + 200;
				enemy.getBody().applyForceToCenter(forceX , forceY, true);
			}
			
			enemy.data.windTimer--;
			if(enemy.data.windTimer == 0) enemy.data.wind = false;
		}
	}

}
