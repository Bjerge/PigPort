package com.bj.pigport.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bj.pigport.entities.hud.Inventory;
import com.bj.pigport.entities.items.Items;
import com.bj.pigport.entities.player.abilities.Ball;
import com.bj.pigport.entities.player.abilities.BallEffect;
import com.bj.pigport.entities.player.abilities.Weapon;
import com.bj.pigport.main.Game;

public class PlayerData {
	
	public int gold = 0;
	
	public float playerSpawnX;
	public float playerSpawnY;
	
	public int health = 100;
	public int healthMax = 100;
	
	public int inventorySize = 0;
	
	public int collThinDisableTimer = 0;
	
	public boolean playerAttackingMelee = false;
	public int playerAttackingEnemyTimer = 0;
	
	public Weapon weapon;
	public boolean weaponEquipped = false;
	
	public Texture weaponTex  = Game.res.getTexture("weapon1");
	public float weaponRotation = 0;
	
	public int direction = 1;
	public boolean directionLock = false;
	
	public boolean ballFrozen = true;
	public boolean ballFire = true;
	public boolean ballWind = true;

	public Ball ball;
	public BallEffect ballEffect;
	public Inventory inventory;
	public Items [] playerItems = new Items[20];
	public Items [] playerEquippedItems = new Items [4];
	

	public float meleeDamageAmount = 30;
	public float mindDamageAmount = 40;
	
	
	
	
	public int fairyDustCollectedInFinishedMap = 0;
	public int fairyDustCollectedInAllMaps = 0;
	
	public int numFairyDust;
	public int totalFairyDust;
	//public int gold = 0;
	
	
	
	public Texture tex;
	
	//Player Stats:
	public int numFairyCage;
	public int totalFairyCage;
	
	
	
	public boolean damageContact = false;
	public int damageCD = 0;
	
	public boolean knockBackContact = false;
	public int knockBackCD = 0;
	public Vector2 knockBackSource = new Vector2(0,0);
	
	public int playerLevel = 1;
	public boolean playerLevelUp = false;
	public int gainedXP = 0;
	public int currentXP = 0;
	public int holderXP = 0;
	public int requiredXP = 10;
	
	public int playerPowerUpPoints = 0;
	public int timeStopLevel = 0;
	public int ballDotLevel = 0;
	public int playerAttackLevel = 0;
	
	public boolean shieldButtonClicked = false;
	
	public boolean playerShielded = false;
	public boolean playerShieldedCD = false;
	public int playerShieldedCDTimer = 0;
	
	public boolean playerManagingPowerUps = false;
	
	public Texture HealthTex = Game.res.getTexture("PlayerHealth");
	public Texture HealthLeftTex = Game.res.getTexture("PlayerHealthLeft");
	
	
	//TEMPORARY
	
	
	public boolean slotPredefined = false;
	public int slotPredefinedValue = 0;
	public boolean slotTypePredefined = false;
	public int slotTypePredefinedValue = 0;
	public boolean rarenessPredefined = false;
	public int rarenessPredefinedValue = 0;
	public boolean itemLevelPredefined = false;
	public int itemLevelPredefinedValue = 0;
	public PlayerData()
	{
		
	}

}
