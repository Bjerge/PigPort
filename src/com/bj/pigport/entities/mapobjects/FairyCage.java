package com.bj.pigport.entities.mapobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.main.Game;

public class FairyCage extends B2DSprite{
	
	public FairyCage(Body body) {
		
		super(body);
		
		Texture tex = Game.res.getTexture("fairyCage");
		TextureRegion[] sprites = TextureRegion.split(tex, 80, 80)[0];
		
		setAnimation(sprites, 1 / 2f);
	}
	
	

}
