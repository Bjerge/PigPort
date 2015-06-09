package com.bj.pigport.entities.enemy.controls;

import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.states.Play;

public class MeleeWeaponAttack {
	
	public MeleeWeaponAttack()
	{
		
	}
	
	public static void MeleeWeaponAttackControls(Enemy enemy, double distance, double distanceX, float playerY)
	{
		if(distance < ((enemy.data.texWeapon.getHeight() / 2f) + 50f) / 100f && enemy.data.meleeWeaponAttackTimer == 0)
		{
			enemy.data.standTimer = 100;
			enemy.data.walkTimer = 0;
			enemy.data.meleeWeaponAttackTimer = 100;
			if(distanceX > 0) enemy.data.direction = 1;
			else  enemy.data.direction = -1;
		}
		
		if(enemy.data.meleeWeaponAttackTimer == 81 && 
				distance < ((enemy.data.texWeapon.getHeight() / 2f) + 50f) / 100f )
		{
			Play.player.data.damageContact = true;
			Play.player.data.knockBackContact = true;
			Play.player.data.knockBackSource = (enemy.getBody().getPosition());
		}
		if(enemy.data.meleeWeaponAttackTimer == 80)
		{
			Play.player.data.damageContact = false;
			Play.player.data.knockBackContact = false;
		}
		
		if(enemy.data.meleeWeaponAttackTimer > 0) enemy.data.meleeWeaponAttackTimer--;
		if(enemy.data.meleeWeaponAttackTimer > 80) enemy.data.weaponRotation += (120f/20f);
		if(enemy.data.meleeWeaponAttackTimer == 0) 
		{
			enemy.data.weaponRotation = -30;
			enemy.data.meleeWeaponAttackTimer = -100;
		}
		if(enemy.data.meleeWeaponAttackTimer < 0) enemy.data.meleeWeaponAttackTimer++;
		if(enemy.data.meleeWeaponAttackTimer == -1) enemy.data.meleeWeaponAttackTimer = 0;
	}

}
