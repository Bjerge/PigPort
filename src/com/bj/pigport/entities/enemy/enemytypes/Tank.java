package com.bj.pigport.entities.enemy.enemytypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class Tank extends EnemyData{

	public Tank() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		meleeWeaponAttack = true;
		groundLittleMovement = true;
		health = 100;
		maxHealth = 100;
		damage = 5;
		playerXP = 10;
		texEnemy = Game.res.getTexture("Tank");
		texWeapon = Game.res.getTexture("sword2h");
		weaponRotation = -30;
		
		name = "Tank";
	}
}
