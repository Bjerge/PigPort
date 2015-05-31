package com.bj.pigport.entities.enemy.bosstypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class FlyingBall extends EnemyData{

	public FlyingBall() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		flying = false;
		health = 200;
		maxHealth = 200;
		damage = 5;
		playerXP = 10;
		texEnemy = Game.res.getTexture("BasicFlyingMinion");
		texStretch = 4;
		
		missile = true;
		missilesNumber = 4;
		
		boss[0] = true;
	}

}
