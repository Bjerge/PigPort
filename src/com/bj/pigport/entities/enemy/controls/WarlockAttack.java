package com.bj.pigport.entities.enemy.controls;

import com.badlogic.gdx.math.Vector2;
import com.bj.pigport.entities.enemy.Enemy;

public class WarlockAttack {

	public WarlockAttack()
	{
		
	}
	
	public static void WarlockAttackControls(Enemy enemy, Vector2 vecEnemy, Vector2 vecPlayer, double distance)
	{
		if(vecEnemy.y - vecPlayer.y < 2 && vecEnemy.y - vecPlayer.y > -2 && distance < 4 && enemy.data.missileCDTimer == 0)
		{
			for(int i = 0; i< 2; i++)
			{
				enemy.data.enemyMissiles[i].getBody().setTransform(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y , 0);
				enemy.data.enemyMissiles[i].getBody().setLinearVelocity(0 , 0);
				if(i == 0)enemy.data.enemyMissiles[i].getBody().applyForceToCenter(75 , 0, true);
				else enemy.data.enemyMissiles[i].getBody().applyForceToCenter(-75 , 0, true);
			}
			enemy.data.missileCDTimer = 250;
		}
		if(enemy.data.missileCDTimer > 0) enemy.data.missileCDTimer--;
		
		if(enemy.data.missileCDTimer == 100)
		{
			for(int i = 0; i< 2; i++)
			{
				enemy.data.enemyMissiles[i].getBody().setTransform(1000, 1000 , 0);
				enemy.data.enemyMissiles[i].getBody().setLinearVelocity(0 , 0);
			}
		}
	}
}
