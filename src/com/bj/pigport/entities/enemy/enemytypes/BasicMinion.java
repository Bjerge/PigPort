package com.bj.pigport.entities.enemy.enemytypes;

import com.bj.pigport.entities.enemy.EnemyData;
import com.bj.pigport.main.Game;

public class BasicMinion extends EnemyData{

	public BasicMinion() {
		playerInRangeRadiusModifier = 1.4f;
		speed = -0.7f;
		ground = true;
		setDifficultyVariables(1);
	}
	
	public void setDifficultyVariables(int difficulty)
	{
		if (difficulty == 1) {
			health = 50;
			maxHealth = 50;
			damage = 5;
			playerXP = 10;
			texEnemy = Game.res.getTexture("BasicMinion");
		}
	}

}
