package com.bj.pigport.entities.mapobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.main.Game;

public class Trap extends B2DSprite{
	
	public Trap(Body body) {
		
		super(body);
		
		Texture tex = Game.res.getTexture("trap");
		TextureRegion[] sprites = TextureRegion.split(tex, 64, 64)[0];
		
		setAnimation(sprites, 1 / 3f);
	}
	
	
	

}
