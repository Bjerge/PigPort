package com.bj.pigport.entities.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.MainMenu;
import com.bj.pigport.entities.B2DSprite;

public class Player extends B2DSprite{
	
	public PlayerData data;
	
	public Player(Body body){
		super(body);
		
		data = MainMenu.data[0];
		
		this.data.tex = Game.res.getTexture("girlStillV2");
		TextureRegion[] sprites = TextureRegion.split(this.data.tex, 110, 118)[0];
		setAnimation(sprites, 1 / 2f);
	}
	
	public void playerExperienceCalculator()
	{
		
		this.data.currentXP = this.data.currentXP + this.data.gainedXP;
		this.data.gainedXP = 0;
		
		while(this.data.currentXP >= this.data.requiredXP)
		{
			this.data.holderXP = this.data.currentXP - this.data.requiredXP;
			this.data.currentXP = this.data.holderXP;
			this.data.requiredXP = (int) (this.data.requiredXP * 1.25);
			
			this.data.playerLevelUp = true;
			this.data.playerLevel++;
			this.data.playerPowerUpPoints++;

		}
	}
}
