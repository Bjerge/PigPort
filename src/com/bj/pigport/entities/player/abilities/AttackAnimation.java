package com.bj.pigport.entities.player.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class AttackAnimation extends B2DSprite{

	public static Texture texAttack;
	
	public AttackAnimation(Body body) {
		
		super(body);
		Texture texAttack = Game.res.getTexture("attackAnimationStar");
		TextureRegion[] sprites = TextureRegion.split(texAttack, 200, 200)[0];
		setAnimation(sprites, 1 / 12f);
	}
	
	//public static void PlayerAttackReset()
	/*
	{
		Play.playerAttackingEnemy = false;
		MyContactListener.enemyBeingHitByPlayerAttack = false;
		Play.playerAttackingEnemyTimer = 0;
		Play.enemiesThatWillGetKilled = null;
		Play.enemiesThatWillGetKilled = new Array<Body>();
		
		Play.playerAttackCD = true;
		MyContactListener.enemyBeenHitByPlayerAttackAlready[Play.enemyNumberHitFirst] = false;
		
		Play.enemyNumberHitFirst = 100;
	
	
	}
	*/
	
	
}
