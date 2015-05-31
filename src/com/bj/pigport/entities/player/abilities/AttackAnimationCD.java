package com.bj.pigport.entities.player.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.main.Game;

public class AttackAnimationCD extends B2DSprite{

	public static Texture texAttack;
	
	public AttackAnimationCD(Body body) {
		
		super(body);
		Texture texAttack = Game.res.getTexture("attackAnimationStarCD");
		TextureRegion[] sprites = TextureRegion.split(texAttack, 200, 200)[0];
		setAnimation(sprites, 1 / 6f);
	}	
}