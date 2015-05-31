package com.bj.pigport.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.main.Game;
import com.bj.pigport.entities.B2DSprite;

public class Player extends B2DSprite{
	
	public static int fairyDustCollectedInFinishedMap = 0;
	public static int fairyDustCollectedInAllMaps = 0;
	
	public static int numFairyDust;
	public static int totalFairyDust;
	//public int gold = 0;
	
	public Texture tex;
	
	public int health = 20;
	public int healthMax = 20;
	
	//Player Stats:
	public static int numFairyCage;
	public static int totalFairyCage;
	
	
	
	public boolean damageContact = false;
	public static int damageCD = 0;
	
	public boolean knockBackContact = false;
	public int knockBackCD = 0;
	public Vector2 knockBackSource = new Vector2(0,0);
	
	public static int playerLevel = 1;
	public static boolean playerLevelUp = false;
	public static int gainedXP = 0;
	public static int currentXP = 0;
	public static int holderXP = 0;
	public static int requiredXP = 10;
	
	public static int playerPowerUpPoints = 0;
	public static int timeStopLevel = 0;
	public static int ballDotLevel = 0;
	public static int playerAttackLevel = 0;
	
	public static int playerStartHealthLevel = 0;
	public static int playerStartHealth = 20;
	public static boolean shieldButtonClicked = false;
	
	public static boolean playerShielded = false;
	public static boolean playerShieldedCD = false;
	public static int playerShieldedCDTimer = 0;
	
	public static boolean playerManagingPowerUps = false;
	
	public static Texture HealthTex = Game.res.getTexture("PlayerHealth");;
	public static Texture HealthLeftTex = Game.res.getTexture("PlayerHealthLeft");
	
	
	//TEMPORARY
	
	
	public static boolean slotPredefined = false;
	public static int slotPredefinedValue = 0;
	public static boolean slotTypePredefined = false;
	public static int slotTypePredefinedValue = 0;
	public static boolean rarenessPredefined = false;
	public static int rarenessPredefinedValue = 0;
	public static boolean itemLevelPredefined = false;
	public static int itemLevelPredefinedValue = 0;
	//TEMPORARY
	
	
	public void setTexture(Texture tex){
		this.tex = tex;
	}
	
	public Texture getTexture(){
		return tex;
	}
	
	public Player(Body body){
		super(body);
		
		tex = Game.res.getTexture("girlStillV2");
		//tex = Game.res.getTexture("girlRight");
		//tex = Game.res.getTexture("GirlLeft");
		//TextureRegion[] sprites = TextureRegion.split(tex, 91, 114)[0];
		//TextureRegion[] sprites = TextureRegion.split(tex, 128, 128)[0];
		//TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		TextureRegion[] sprites = TextureRegion.split(tex, 110, 118)[0];
		
		//setAnimation(sprites, 1 / 12f);
		setAnimation(sprites, 1 / 2f);
	}
	
	public void collectFairyDust(){
		numFairyDust = numFairyDust+1;
	}
	
	public int getNumFairyDust(){
		return numFairyDust;
	}
	
	public void collectFairyCage(){
		numFairyCage++;
	}
	
	public int getNumFairyCage(){
		return numFairyCage;
	}
	
	public void setTotalFairyDust(int i){
		totalFairyDust = i;
	}
	
	public int getTotalFairyDust() {
		return totalFairyDust;
	}
	
	public void hitTrap(){
		if(health >= 1){
		health--;
		}
	}
	public static void playerExperienceCalculator()
	{
		
		currentXP = currentXP + gainedXP;
		gainedXP = 0;
		
		while(currentXP >= requiredXP)
		{
			holderXP = currentXP - requiredXP;
			currentXP = holderXP;
			requiredXP = (int) (requiredXP * 1.25);
			
			playerLevelUp = true;
			playerLevel++;
			playerPowerUpPoints++;

		}
	}
}
