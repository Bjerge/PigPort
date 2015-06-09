package com.bj.pigport.entities.enemy.enemytypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class Warlock extends EnemyData{

	public Warlock() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		warlock = true;
		groundLittleMovement = true;
		health = 100;
		maxHealth = 100;
		damage = 5;
		playerXP = 10;
		texEnemy = Game.res.getTexture("Warlock");
		
		missilesNumber = 2;
		missileStretch = 4;
		
		name = "Warlock";
	}
}
