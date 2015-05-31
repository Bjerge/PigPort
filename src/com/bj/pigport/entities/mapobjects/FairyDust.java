package com.bj.pigport.entities.mapobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.main.Game;

public class FairyDust extends B2DSprite{
	
	public FairyDust(Body body) {
		
		super(body);
		
		Texture tex = Game.res.getTexture("fairyDust");
		TextureRegion[] sprites = TextureRegion.split(tex, 30, 30)[0];
		
		setAnimation(sprites, 1 / 3f);
	}
}
