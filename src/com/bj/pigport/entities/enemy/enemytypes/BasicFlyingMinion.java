package com.bj.pigport.entities.enemy.enemytypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class BasicFlyingMinion extends EnemyData{

	public BasicFlyingMinion() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		flying = true;
		missile = true;
		health = 1000;
		maxHealth = 1000;
		damage = 5;
		playerXP = 10;
		texEnemy = Game.res.getTexture("BasicFlyingMinion");
	}

}
