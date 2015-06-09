package com.bj.pigport.entities.enemy.enemytypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class GroundShooter extends EnemyData{

	public GroundShooter() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		groundLittleMovement = true;
		health = 100;
		maxHealth = 100;
		damage = 5;
		playerXP = 10;
		texEnemy = Game.res.getTexture("GroundShooter");
		
		missile = true;
		missileColission = true;
		missilesNumber = 1;
		missileStretch = 1;
		enemyMissileReset = new boolean [missilesNumber];
		
		name = "Ground Shooter";
	}
}
