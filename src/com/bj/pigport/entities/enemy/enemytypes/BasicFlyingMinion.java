package com.bj.pigport.entities.enemy.enemytypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class BasicFlyingMinion extends EnemyData{

	public BasicFlyingMinion() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		flyingCharge = true;
		health = 50;
		maxHealth = 50;
		damage = 5;
		playerXP = 10;
		texEnemy = Game.res.getTexture("BasicFlyingMinion");
		name = "Basic Flying";
	}
}