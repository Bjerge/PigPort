package com.bj.pigport.entities.enemy.controls;

import com.bj.pigport.entities.enemy.Enemy;

public class MissileController {
	
	public MissileController()
	{
		
	}
	
	
	
	public static void MissileControls(Enemy enemy, float cosP, float sinP, double distance)
	{
		enemy.data.missileCDTimer++;
		if(enemy.data.missileCDTimer == 200)
		{
			enemy.data.missileShootTimer = (enemy.data.missilesNumber * 20) + 5;
			enemy.data.missilesNextToFire = enemy.data.missilesNumber - 1;
		}
		
		if(enemy.data.missileShootTimer > 0 && distance < 5) 
		{
			enemy.data.missileShootTimer--;
		
			if(enemy.data.missileShootTimer % 20 == 0)
			{
				enemy.data.enemyMissiles[enemy.data.missilesNextToFire].getBody().setTransform(enemy.getBody().getPosition(), 0);
				enemy.data.enemyMissiles[enemy.data.missilesNextToFire].getBody().setLinearVelocity(0 , 0);
				
				float forceX = (cosP * 150);
				float forceY = (sinP * 150);
				enemy.data.enemyMissiles[enemy.data.missilesNextToFire].getBody().applyForceToCenter(-forceX , -forceY, true);
				
				enemy.data.missilesNextToFire--;
				
				if(enemy.data.missilesNextToFire == -1)
				{
					enemy.data.missileShootTimer = 0;
					enemy.data.missileCDTimer = 0;
				}
			}
		}
		
		for(int i = 0; i < enemy.data.missilesNumber; i++)
		{
			if(enemy.data.enemyMissileReset[i])
			{
				enemy.data.enemyMissiles[i].getBody().setTransform(1000, 1000, 0);
				enemy.data.enemyMissiles[i].getBody().setLinearVelocity(0 , 0);
				enemy.data.enemyMissileReset[i] = false;
			}
		}
	}

}
