package com.bj.pigport.entities.items;

import com.badlogic.gdx.graphics.Texture;
import com.bj.pigport.entities.hud.Inventory;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class Items {
	
	public int type = 0;
	public int itemLevel = 1;
	public int itemRareness = 1;
	public String RarenessName;
	
	public String weaponTypeName = "";
	
	public int healAmount = 0;
	
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
	public boolean equipable = false; 
	
	public boolean stackable = false;
	public int stackAmount = 1;
	
	public Texture itemTex;
	
	public Items(int type)
	{
		if(type == 0)
		{
			statGenerator();
			nameGenerator();
		}
		else if(type == 1)
		{
			itemName = "Meat";
			stackable = true;
			healAmount = 20;
		}
		itemTex = Game.res.getTexture("club");
		
	}
	
	void statGenerator()
	{
		type = (int) (Math.random()*4);
		if(Play.player.data.slotPredefined) type = Play.player.data.slotPredefinedValue;
		
		itemLevel = (int) (Math.random() * Play.player.data.playerLevel) + 1;
		if(Play.player.data.itemLevelPredefined) itemLevel = Play.player.data.itemLevelPredefinedValue;
		itemLevel = 1;
		
		int randomNumber = (int) (Math.random()*1000) + 1;
		if(randomNumber > 999) itemRareness = 4;
		else if(randomNumber > 975) itemRareness = 3;
		else if (randomNumber > 850) itemRareness = 2;
		else itemRareness = 1;
		if(Play.player.data.rarenessPredefined) itemRareness = Play.player.data.rarenessPredefinedValue;
		
		statHealth = 4 + itemRareness;
		statMind = 4 + itemRareness;
		statStrength = 4 + itemRareness;
		statBrain = 4 + itemRareness;
		
	}
	
	void nameGenerator()
	{

		if(itemRareness == 1) RarenessName = "Common";
		else if(itemRareness == 2) RarenessName = "Rare";
		else if(itemRareness == 3) RarenessName = "Mythical";
		else RarenessName = "GodLike";
		
		String slotName = "";
		if(type == 0)
		{
			int weapon = (int) (Math.random()*8);
			if(weapon == 0) {weaponTypeName = "axe1h"; slotName = "Small Axe"; }
			if(weapon == 1) {weaponTypeName = "axe2h"; slotName = "Large Axe"; }
			if(weapon == 2) {weaponTypeName = "hammer1h"; slotName = "Small Hammer"; }
			if(weapon == 3) {weaponTypeName = "hammer2h"; slotName = "Large Hammer"; }
			if(weapon == 4) {weaponTypeName = "spear1h"; slotName = "Small Spear"; }
			if(weapon == 5) {weaponTypeName = "spear2h"; slotName = "Large Spear"; }
			if(weapon == 6) {weaponTypeName = "sword1h"; slotName = "Small Sword"; }
			if(weapon == 7) {weaponTypeName = "sword2h"; slotName = "Large Sword"; }
			
			itemTex = Game.res.getTexture(weaponTypeName);
			
			equipable = true;
		}
		
		
		else if(type == 1)
		{
			slotName = "Hat";
			itemTex = Game.res.getTexture("PlayerShield");
			equipable = true;
		}
		else if(type == 2)
		{
			slotName = "Cape";
			equipable = true;
		}
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
			
			RarenessName = "";
		}
		
		itemName = RarenessName + " " + slotName;
		
	}
	
	public static void addItemToList(Items item)
	{
		if(item.stackable)
		{
			for(int i = 0; i < Play.player.data.inventorySize; i ++)
			{
				if(Play.player.data.playerItems[i].itemName == item.itemName) 
				{
					Play.player.data.playerItems[i].stackAmount ++;
					return;
				}
			}
		}
		
		if(Play.player.data.inventorySize < 20)
		{
			Play.player.data.playerItems[Play.player.data.inventorySize] = item;
			Play.player.data.inventorySize ++;
			Play.player.data.inventory = new Inventory(Play.player);
		}
	}
	
	public static void deleteItemFromList(int ItemNumber)
	{
		//Items items = PlayerData.playerItems[ItemNumber];
		
		Play.player.data.inventorySize --;
		Play.player.data.playerItems[ItemNumber] = null;
		
		for(int i = 0; i < Play.player.data.playerItems.length; i ++)
		{
			if(Play.player.data.playerItems[i] == null && i != 19) 
			{
				Play.player.data.playerItems[i] = Play.player.data.playerItems[i+1];
				Play.player.data.playerItems[i+1] = null;
			}
		}
		Play.player.data.inventory = new Inventory(Play.player);
	}
}
