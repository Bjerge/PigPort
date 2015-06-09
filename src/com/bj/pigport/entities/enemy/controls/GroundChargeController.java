package com.bj.pigport.entities.enemy.controls;

import com.bj.pigport.entities.enemy.Enemy;

public class GroundChargeController {
	public GroundChargeController()
	{
		
	}
	
	public static void GroundChargeControls(Enemy enemy, double distance, float vecEX, float vecPX)
	{
		if(distance < 3)
		{
			
			if(enemy.data.lockTimer == 0)
			{
				if(vecEX - vecPX < 0) enemy.data.enemyWalkRightLeft = 1;
				else enemy.data.enemyWalkRightLeft = -1;
				enemy.data.lockTimer = 100;
			}
								
			if(enemy.data.lockTimer > 0) enemy.data.lockTimer--;
			enemy.getBody().setLinearVelocity((float) (1.3f * enemy.data.enemyWalkRightLeft), enemy.getBody().getLinearVelocity().y);
		}
		else
		{
			if(enemy.getPosition().x < enemy.data.SpawnX-2 && enemy.data.enemyWalkRightLeft == -1)
			{
				enemy.data.enemyWalkRightLeft = 1;
			}
			else if(enemy.getPosition().x > enemy.data.SpawnX+2 && enemy.data.enemyWalkRightLeft == 1)
			{
				enemy.data.enemyWalkRightLeft = -1;
			}
			enemy.getBody().setLinearVelocity(enemy.data.enemyWalkRightLeft * 1.3f, enemy.getBody().getLinearVelocity().y);
		}
	}
}
