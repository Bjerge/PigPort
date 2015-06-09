package com.bj.pigport.entities.enemy.controls;

import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.entities.player.Player;

public class LittleGroundMovement {
	
	public LittleGroundMovement()
	{
		
	}
	
	public static void LittleGroundMovementControls(Enemy enemy)
	{
		//System.out.println(enemy.data.walkTimer + " " + enemy.data.standTimer);
		
		if(enemy.data.walkTimer == 1)enemy.data.standTimer = (int) (Math.random() * 150) + 50;
		if(enemy.data.standTimer > 0)enemy.data.standTimer--;
		
		if(enemy.data.standTimer == 1)
		{
			enemy.data.walkTimer = (int) (Math.random() * 100) + 50;
			enemy.data.enemyWalkRightLeft = enemy.data.enemyWalkRightLeft * (-1);
			enemy.data.direction = enemy.data.enemyWalkRightLeft * (-1);
		}
		if(enemy.data.walkTimer > 0)
		{
			enemy.data.walkTimer--;
			enemy.getBody().setLinearVelocity(enemy.data.enemyWalkRightLeft * 0.7f, enemy.getBody().getLinearVelocity().y);
		}
		
	}

}
