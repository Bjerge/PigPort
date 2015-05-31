package com.bj.pigport.entities.items;

import com.badlogic.gdx.graphics.Texture;
import com.bj.pigport.entities.hud.Inventory;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class Items {
	
	// Slots: 0 = main-hand (100%), 1 = hat (75%), 2 = cape (75%)
	public int type = 0;
	public int itemLevel = 1;
	
	// ItemRareness: 1 = common, 2 = rare, 3 = mythical, 4 = godlike
	public int itemRareness = 1;
	public String RarenessName;
	
	// DamageType: if a main or off-hand, what is the damage type. 1 = strength, 2 = brain
	public int damageType = 0;
	public String damageTypeTxt;
	
	public int gemSlotNumber = 1;
	public Items [] gems = new Items [gemSlotNumber];
	
	// stats:
	public int statStrength = 0;
	public int statBrain = 0;
	public int statMind = 0;
	public int statHealth = 0;
	
	// the items name
	public String itemName;
	
	public int sellValue = 100;
	
	public boolean equipped = false; 
	
	public Texture itemTex;
	
	public Items()
	{
		itemTex = Game.res.getTexture("club");
		statGenerator();
		nameGenerator();
	}
	
	void statGenerator()
	{
		type = (int) (Math.random()*4);
		if(Player.slotPredefined) type = Player.slotPredefinedValue;
		
		itemLevel = (int) (Math.random() * Player.playerLevel) + 1;
		if(Player.itemLevelPredefined) itemLevel = Player.itemLevelPredefinedValue;
		itemLevel = 1;
		
		int randomNumber = (int) (Math.random()*1000) + 1;
		if(randomNumber > 999) itemRareness = 4;
		else if(randomNumber > 975) itemRareness = 3;
		else if (randomNumber > 850) itemRareness = 2;
		else itemRareness = 1;
		if(Player.rarenessPredefined) itemRareness = Player.rarenessPredefinedValue;
		
		statHealth = 4 + itemRareness;
		statMind = 4 + itemRareness;
		statStrength = 4 + itemRareness;
		statBrain = 4 + itemRareness;
		
		damageType = (int) (Math.random()*2) + 1;
	}
	
	void nameGenerator()
	{

		if(itemRareness == 1) RarenessName = "Common";
		else if(itemRareness == 2) RarenessName = "Rare";
		else if(itemRareness == 3) RarenessName = "Mythical";
		else RarenessName = "GodLike";
		
		if(damageType == 0) damageTypeTxt = "Strength";
		else if(damageType == 1)damageTypeTxt = "Brain";
		else damageTypeTxt = "Defence";
		
		String slotName = "";
		if(type == 0)
		{
			slotName = "Weapon";
			itemTex = Game.res.getTexture("Weapon");		
		}
		
		
		else if(type == 1)
		{
			slotName = "Hat";
			itemTex = Game.res.getTexture("PlayerShield");
		}
		else if(type == 2)slotName = "Cape";
		else if(type == 3)
		{
			gemSlotNumber = 0;
			
			int GemType = (int) (Math.random()*6);
			
			slotName = "Gem";
			
			if(GemType == 0) 
			{
				itemTex = Game.res.getTexture("FireGem");
				slotName = "Fire Gem";
			}
			if(GemType == 1) 
			{
				itemTex = Game.res.getTexture("FrostGem");
				slotName = "Frost Gem";
			}
			if(GemType == 2) 
			{
				itemTex = Game.res.getTexture("WindGem");
				slotName = "Wind Gem";
			}
			if(GemType == 3) 
			{
				itemTex = Game.res.getTexture("NatureGem");
				slotName = "Nature Gem";
			}
			if(GemType == 4) 
			{
				itemTex = Game.res.getTexture("LightGem");
				slotName = "Light Gem";
			}
			if(GemType == 5) 
			{
				itemTex = Game.res.getTexture("DarkGem");
				slotName = "Dark Gem";
			}
		}
		
		itemName = RarenessName + " " + slotName + " ~of " + damageTypeTxt;
		
	}
	
	public static void addItemToList(Items item)
	{
		if(PlayerData.inventorySize < 20)
		{
			PlayerData.playerItems[PlayerData.inventorySize] = item;
			PlayerData.inventorySize ++;
			PlayerData.inventory = new Inventory(PlayerData.player);
		}
	}
	
	public static void deleteItemFromList(int ItemNumber)
	{
		//Items items = PlayerData.playerItems[ItemNumber];
		
		PlayerData.inventorySize --;
		PlayerData.playerItems[ItemNumber] = null;
		
		for(int i = 0; i < PlayerData.playerItems.length; i ++)
		{
			if(PlayerData.playerItems[i] == null && i != 19) 
			{
				PlayerData.playerItems[i] = PlayerData.playerItems[i+1];
				PlayerData.playerItems[i+1] = null;
			}
		}
		PlayerData.inventory = new Inventory(PlayerData.player);
	}
}
