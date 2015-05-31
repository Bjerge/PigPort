package com.bj.pigport.entities.player;

import com.bj.pigport.entities.hud.Inventory;
import com.bj.pigport.entities.items.Items;
import com.bj.pigport.entities.player.abilities.Ball;
import com.bj.pigport.entities.player.abilities.BallEffect;

public class PlayerData {
	
	public static int gold = 0;
	
	public static float playerSpawnX;
	public static float playerSpawnY;
	
	public static Items [] playerItems = new Items[20];
	public static Items [] playerEquippedItems = new Items [4];
	public static int inventorySize = 0;
	
	public static Ball ball;
	public static BallEffect ballEffect;
	
	public static Inventory inventory;
	
	public static boolean ballFrozen = true;
	public static boolean ballFire = true;
	public static boolean ballWind = true;

	public static Player player;
	
	public PlayerData()
	{
		
	}

}
