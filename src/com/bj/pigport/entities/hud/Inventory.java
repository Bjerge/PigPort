package com.bj.pigport.entities.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.items.Items;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.abilities.Weapon;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.GameButtonStandard;

public class Inventory {

	private GameButtonStandard [] EquippedButton;
	private GameButtonStandard [] playerItemButtons;
	private GameButtonStandard [] ItemGemSlotButtons;
	
	private GameButtonStandard infoBox;
	
	public static boolean itemSelected = false;
	public boolean equippedItemSelected = false;
	private static int itemNumberSelected = 0;
	
	
	private GameButtonStandard InventoryBackground;
	private GameButtonStandard BackgroundButton;
	private GameButtonStandard SellCommonButton;
	private GameButtonStandard EquipButton;
	private GameButtonStandard EatButton;
	private GameButtonStandard DropButton;
	private GameButtonStandard UnEquipButton;
	
	private Vector2 dragStart;
	public boolean itemSelectedForDrag = false;
	public boolean itemEquippedSelectedForDrag = false; 
	public int itemSelectedForDragNumber = 0; 
	
	public boolean dragBegun = false;
	public int dragEndTimer = 0;
	
	private GameButtonStandard ItemDraggedButton;
	
	private Texture goldTex = Game.res.getTexture("Coins");
		
	public Inventory(Player player)
	{
		//itemSelected = false;
		//Player.playerTotalStats();
		
		playerItemButtons = new GameButtonStandard [Play.player.data.playerItems.length];
		for(int i = 0; i < Play.player.data.inventorySize; i ++)
		{
			String stack = "";
			if(Play.player.data.playerItems[i].stackable) stack = Play.player.data.playerItems[i].stackAmount + " x ";
			if(i >= 10) playerItemButtons[i] = new GameButtonStandard(stack + Play.player.data.playerItems[i].itemName, 880, 650 - (58 * (i-10)), GameState.getHudCam(), 2.5f, 0.9f, 0.5f, null);
			else playerItemButtons[i] = new GameButtonStandard(stack + Play.player.data.playerItems[i].itemName, 720, 650 - (58 * i), GameState.getHudCam(), 2.5f, 0.95f, 0.5f, null);
		}
		
		EquippedButton = new GameButtonStandard [3];
		for(int i = 0; i < 3; i++)
		{
			if(Play.player.data.playerEquippedItems[i]==null)EquippedButton[i] = new GameButtonStandard("", 1070, 600 - (i * 150), GameState.getHudCam(), 4f, 2f, 0.8f, null);
			else EquippedButton[i] = new GameButtonStandard(Play.player.data.playerEquippedItems[i].itemName, 1070, 600 - (i * 150), GameState.getHudCam(), 4f, 2f, 0.8f, null);
		}
		
		InventoryBackground = new GameButtonStandard("", 700, 670, GameState.getHudCam(), 10.7f, 10.3f, 1, null);
		infoBox = new GameButtonStandard("Farm", 1000, 1000, GameState.getHudCam(), 3.5f, 1, 1, null);
		BackgroundButton = new GameButtonStandard("Farm", 1000, 1000, GameState.getHudCam(), 3.5f, 1, 1, null);
		EquipButton = new GameButtonStandard("Equip", 150, 240, GameState.getHudCam(), 2.5f, 1, 0.8f, null);
		UnEquipButton = new GameButtonStandard("Unequip", 150, 240, GameState.getHudCam(), 2.5f, 1, 0.8f, null);
		
		EatButton = new GameButtonStandard("Eat", 150, 240, GameState.getHudCam(), 2.5f, 1, 0.8f, null);
		DropButton = new GameButtonStandard("Drop", 300, 240, GameState.getHudCam(), 2.5f, 1, 0.8f, null);
		
		SellCommonButton = new GameButtonStandard("Sell all common", 600, 600, GameState.getHudCam(), 4f, 1, 0.8f, null);
		
		itemSelected = false;
		equippedItemSelected = false;
		itemNumberSelected = 0;
	}
	
	public void InputUpdater(GameStateManager gsm)
	{
		infoBoxGenerator(null, true);
		
		for(int i = 0; i < 3; i++) if(Play.player.data.playerEquippedItems[i] != null)
		{
			if(EquippedButton[i].isHovered()) infoBoxGenerator(Play.player.data.playerEquippedItems[i], true);
			if(EquippedButton[i].isReleased())
			{
				equippedItemSelected = true;
				itemNumberSelected = i;
				DrawBackgroundButton(i, Play.player.data.playerEquippedItems);
				itemSelected = false;
			}
			if(EquippedButton[i].isClicked())
			{
				dragStart = new Vector2(Gdx.input.getX(), (Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY());
				itemEquippedSelectedForDrag = true;
				itemSelectedForDragNumber = i;
			}
		}
		
		for(int i = 0; i < Play.player.data.inventorySize; i ++)
		{
			if(playerItemButtons[i].isHovered())infoBoxGenerator(Play.player.data.playerItems[i], false);
			if(playerItemButtons[i].isReleased())
			{
				itemSelected = true;
				itemNumberSelected = i;
				DrawBackgroundButton(i, Play.player.data.playerItems);
				equippedItemSelected = false;
			}
			if(playerItemButtons[i].isClicked())
			{
				dragStart = new Vector2(Gdx.input.getX(), (Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY());
				itemSelectedForDrag = true;
				itemSelectedForDragNumber = i;
			}
		}
		
		if(SellCommonButton.isClicked())
		{
			for(int i = 19; i > -1; i--) if(Play.player.data.playerItems[i] != null && Play.player.data.playerItems[i].itemRareness == 1)
			{
				Play.player.data.gold += Play.player.data.playerItems[i].sellValue;
				Items.deleteItemFromList(i);
			}
		}
		
		
		if(EquipButton.isClicked() && itemSelected)
		{			
			Items item = Play.player.data.playerEquippedItems[Play.player.data.playerItems[itemNumberSelected].type];
			Play.player.data.playerEquippedItems[Play.player.data.playerItems[itemNumberSelected].type] = Play.player.data.playerItems[itemNumberSelected];
			
			if(Play.player.data.playerItems[itemNumberSelected].type == 0)
			{
				Play.player.data.weapon.getBody().setTransform(100, 100, 0);
				Weapon.createWeapon(Play.getWorld(), Play.player.data.playerItems[itemNumberSelected].weaponTypeName);
			}
			
			Items.deleteItemFromList(itemNumberSelected);
			itemNumberSelected = 0;
			if(item != null)Items.addItemToList(item);
		}
		
		if(EatButton.isClicked() && itemSelected)
		{			
			Play.player.data.health += Play.player.data.playerItems[itemNumberSelected].healAmount;
			if(Play.player.data.health > Play.player.data.healthMax) Play.player.data.health = Play.player.data.healthMax;
			
			if(Play.player.data.playerItems[itemNumberSelected].stackAmount > 1)
			{
				Play.player.data.playerItems[itemNumberSelected].stackAmount --;
				Play.player.data.inventory = new Inventory(Play.player);
			}
			else 
			{
				Items.deleteItemFromList(itemNumberSelected);
				itemNumberSelected = 0;
			}
		}
		
		if(DropButton.isClicked() && itemSelected)
		{	
			Loot.createItem(Play.player.getPosition().x , Play.player.getPosition().y, 
					Play.player.data.playerItems[itemNumberSelected]);
			Items.deleteItemFromList(itemNumberSelected);
			itemNumberSelected = 0;
		}
		
		if(UnEquipButton.isClicked() && equippedItemSelected)
		{			
			Items.addItemToList(Play.player.data.playerEquippedItems[itemNumberSelected]);
			Play.player.data.playerEquippedItems[itemNumberSelected] = null;
			equippedItemSelected = false;
			Play.player.data.inventory = new Inventory(Play.player);
		}
		
		if(itemSelected)
		{
			for(int i = 0; i < Play.player.data.playerItems[itemNumberSelected].gemSlotNumber; i++)
			{
				if(ItemGemSlotButtons[i] != null) 
				{
					if(ItemGemSlotButtons[i].isHovered()) if(Play.player.data.playerItems[itemNumberSelected].gems[i] != null) infoBoxGenerator(Play.player.data.playerItems[itemNumberSelected].gems[i], false);
					if(ItemGemSlotButtons[i].isReleased() && dragBegun)
					{
						if(itemSelectedForDrag && Play.player.data.playerItems[itemSelectedForDragNumber].type == 3)
						{
							Play.player.data.playerItems[itemNumberSelected].gems[i] = Play.player.data.playerItems[itemSelectedForDragNumber];
							Items.deleteItemFromList(itemSelectedForDragNumber);
						}
					}
				}
			}
		}
		
		if(equippedItemSelected)
		{
			for(int i = 0; i < Play.player.data.playerEquippedItems[itemNumberSelected].gemSlotNumber; i++)
			{
				if(ItemGemSlotButtons[i] != null) 
				{
					if(ItemGemSlotButtons[i].isHovered()) if(Play.player.data.playerEquippedItems[itemNumberSelected].gems[i] != null) infoBoxGenerator(Play.player.data.playerEquippedItems[itemNumberSelected].gems[i], false);
					if(ItemGemSlotButtons[i].isReleased() && dragBegun)
					{
						if(itemSelectedForDrag && Play.player.data.playerItems[itemSelectedForDragNumber].type == 3)
						{
							Play.player.data.playerEquippedItems[itemNumberSelected].gems[i] = Play.player.data.playerItems[itemSelectedForDragNumber];
							Items.deleteItemFromList(itemSelectedForDragNumber);
						}
					}
				}
			}
		}
		
		
		if(dragBegun) ManageDrag();
		if(MyInput.isDown() && itemSelectedForDrag || MyInput.isDown() && itemEquippedSelectedForDrag)CheckIfDragBegun();
		//if(MyInput.isReleased()) itemSelectedForDrag = false;
		
		if(MyInput.isReleased())dragEndTimer --;
		if(dragEndTimer < 3 && dragEndTimer > 0) dragEndTimer--;
		if(dragEndTimer == 0)
		{
			dragBegun = false;
			itemSelectedForDrag = false;
		}
	}
	
	public void Updater(float dt)
	{
		for(int i = 0; i < Play.player.data.inventorySize; i ++) playerItemButtons[i].update(dt);
		for(int i = 0; i < 3; i++)EquippedButton[i].update(dt);
		infoBox.update(dt);
		//SellCommonButton.update(dt);
		InventoryBackground.update(dt);
		
		if(itemSelected)
		{
			BackgroundButton.update(dt);
			for(int i = 0; i < Play.player.data.playerItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].update(dt);
			if(Play.player.data.playerItems[itemNumberSelected].equipable)EquipButton.update(dt);
			if(Play.player.data.playerItems[itemNumberSelected].healAmount > 0)EatButton.update(dt);
			DropButton.update(dt);
		}
		if(equippedItemSelected)
		{
			BackgroundButton.update(dt);
			UnEquipButton.update(dt);
			for(int i = 0; i < Play.player.data.playerEquippedItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].update(dt);
		}
		
		if(dragBegun)ItemDraggedButton.update(dt);
		
	}
	
	public void render(SpriteBatch sb)
	{
		
		InventoryBackground.render(sb);	
		for(int i = 0; i < Play.player.data.inventorySize; i ++)playerItemButtons[i].render(sb);
		for(int i = 0; i < 3; i++)EquippedButton[i].render(sb);
		//SellCommonButton.render(sb);
		if(itemSelected)
		{
			BackgroundButton.render(sb);
			for(int i = 0; i < Play.player.data.playerItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].render(sb);
			if(Play.player.data.playerItems[itemNumberSelected].equipable)EquipButton.render(sb);
			if(Play.player.data.playerItems[itemNumberSelected].healAmount > 0)EatButton.render(sb);
			DropButton.render(sb);
		}
		if(equippedItemSelected)
		{
			BackgroundButton.render(sb);
			UnEquipButton.render(sb);
			for(int i = 0; i < Play.player.data.playerEquippedItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].render(sb);
		}
		
		if(dragBegun)ItemDraggedButton.render(sb);
		
		sb.begin();
		sb.draw(goldTex, 1060, 100);
		Play.drawLetter(sb, Play.player.data.gold + "", 1130 , 110, 1);
		sb.end();
		
		
		infoBox.render(sb);
	}
	
	
	
	void infoBoxGenerator(Items item, boolean EquippedItem)
	{
		if(item == null)
		{
			infoBox = new GameButtonStandard("", 1000 ,1000, GameState.getHudCam(), 1f, 1f, 1f, null);
		}
		else if(EquippedItem)
		{
			if(item!=null)
			{
				infoBox = new GameButtonStandard(item.itemName + 
					"~~Strenght: " + item.statStrength +
					"~Brain: " + item.statBrain +
					"~Defence: " + item.statMind +
					"~Item Level: " + item.itemLevel +
					"~Item Rareness: " + item.RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
			}
		}
		else if(Play.player.data.playerEquippedItems[item.type] != null)
		{
			long diffStr = (item.statStrength - Play.player.data.playerEquippedItems[item.type].statStrength);
			long diffBrn = (item.statBrain - Play.player.data.playerEquippedItems[item.type].statBrain);
			long diffMnd = (item.statMind - Play.player.data.playerEquippedItems[item.type].statMind);
			long diffHlh = (item.statHealth - Play.player.data.playerEquippedItems[item.type].statHealth);
			
			infoBox = new GameButtonStandard(item.itemName + 
				"~~Strenght: " + item.statStrength + " (" + diffStr + ")" +
				"~Brain: " + item.statBrain + " (" + diffBrn + ")" +
				"~Mind: " + item.statMind + " (" + diffMnd + ")" +
				"~Health: " + item.statHealth + " (" + diffHlh + ")" +
				"~Item Level: " + item.itemLevel +
				"~Item Rareness: " + item.RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
		}
		else
		{
			infoBox = new GameButtonStandard(item.itemName + 
					"~~Strenght: " + item.statStrength + 
					"~Brain: " + item.statBrain +
					"~Mind: " + item.statMind +
					"~Health: " + item.statHealth +
					"~Level: " + item.itemLevel +
					"~Quality: " + item.RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
		}
	}
	
	
	
	
	/*
	void infoBoxGenerator(int slotNumber, boolean Equipped)
	{
		if(Equipped)
		{
			if(slotNumber == 4)infoBox = new GameButtonStandard("Farm", 10000, 10000, GameState.getHudCam(), 3.5f, 1, 1, null);
			else if(Play.player.data.playerEquippedItems[slotNumber]!=null)
			{
				infoBox = new GameButtonStandard(Play.player.data.playerEquippedItems[slotNumber].itemName + 
					"~~Strenght: " + Play.player.data.playerEquippedItems[slotNumber].statStrength +
					"~Brain: " + Play.player.data.playerEquippedItems[slotNumber].statBrain +
					"~Defence: " + Play.player.data.playerEquippedItems[slotNumber].statMind +
					"~Item Level: " + Play.player.data.playerEquippedItems[slotNumber].itemLevel +
					"~Item Rareness: " + Play.player.data.playerEquippedItems[slotNumber].RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
			}
		}
		else if(Play.player.data.playerEquippedItems[Play.player.data.playerItems[slotNumber].type] != null)
		{
			long diffStr = (Play.player.data.playerItems[slotNumber].statStrength - Play.player.data.playerEquippedItems[Play.player.data.playerItems[slotNumber].type].statStrength);
			long diffBrn = (Play.player.data.playerItems[slotNumber].statBrain - Play.player.data.playerEquippedItems[Play.player.data.playerItems[slotNumber].type].statBrain);
			long diffMnd = (Play.player.data.playerItems[slotNumber].statMind - Play.player.data.playerEquippedItems[Play.player.data.playerItems[slotNumber].type].statMind);
			long diffHlh = (Play.player.data.playerItems[slotNumber].statHealth - Play.player.data.playerEquippedItems[Play.player.data.playerItems[slotNumber].type].statHealth);
			
			infoBox = new GameButtonStandard(Play.player.data.playerItems[slotNumber].itemName + 
				"~~Strenght: " + Play.player.data.playerItems[slotNumber].statStrength + " (" + diffStr + ")" +
				"~Brain: " + Play.player.data.playerItems[slotNumber].statBrain + " (" + diffBrn + ")" +
				"~Mind: " + Play.player.data.playerItems[slotNumber].statMind + " (" + diffMnd + ")" +
				"~Health: " + Play.player.data.playerItems[slotNumber].statHealth + " (" + diffHlh + ")" +
				"~Item Level: " + Play.player.data.playerItems[slotNumber].itemLevel +
				"~Item Rareness: " + Play.player.data.playerItems[slotNumber].RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
		}
		else
		{
			infoBox = new GameButtonStandard(Play.player.data.playerItems[slotNumber].itemName + 
					"~~Strenght: " + Play.player.data.playerItems[slotNumber].statStrength + 
					"~Brain: " + Play.player.data.playerItems[slotNumber].statBrain +
					"~Mind: " + Play.player.data.playerItems[slotNumber].statMind +
					"~Health: " + Play.player.data.playerItems[slotNumber].statHealth +
					"~Item Level: " + Play.player.data.playerItems[slotNumber].itemLevel +
					"~Item Rareness: " + Play.player.data.playerItems[slotNumber].RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
		}
	}
	
	*/
	
	
	void DrawBackgroundButton(int slotNumber, Items [] itemList)
	{
		if(itemList[slotNumber] != null)
		{
			BackgroundButton = new GameButtonStandard(itemList[slotNumber].itemName + 
				"~~Strenght: " + itemList[slotNumber].statStrength +
				"~Brain: " + itemList[slotNumber].statBrain +
				"~Mind: " + itemList[slotNumber].statMind +
				"~Health: " + itemList[slotNumber].statHealth +
				"~Item Level: " + itemList[slotNumber].itemLevel +
				"~Item Rareness: " + itemList[slotNumber].RarenessName, 130, 580, GameState.getHudCam(), 8f, 7f, 0.8f, null);
			
			if(itemList[slotNumber].gemSlotNumber > 0) 
			{
				ItemGemSlotButtons = new GameButtonStandard[itemList[slotNumber].gemSlotNumber];
				for(int g = 0; g < itemList[slotNumber].gemSlotNumber; g++)
				{
					if(itemList[slotNumber].gems[g] == null)ItemGemSlotButtons[g] = new GameButtonStandard("", 535, 570 - (g * 70), GameState.getHudCam(), 1f, 1f, 0.8f, null);
					else ItemGemSlotButtons[g] = new GameButtonStandard("", 535, 570 - (g * 70), GameState.getHudCam(), 1f, 1f, 0.8f, itemList[slotNumber].gems[g].itemTex);
				}
			}
		}
	}

	void CheckIfDragBegun()
	{
		Vector2 current = new Vector2(Gdx.input.getX(), (Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY());
		int distance = (int) (Math.sqrt(((current.x - dragStart.x) * (current.x - dragStart.x)) + ((current.y - dragStart.y) * (current.y - dragStart.y))));
		
		if(distance > 100) 
		{
			dragBegun = true;
			dragEndTimer = 3;
			ItemDraggedButton = new GameButtonStandard("", current.x, current.y, GameState.getHudCam(), 1f, 1f, 0.8f, null);
		}
	}
	
	void ManageDrag()
	{
		Vector2 current = new Vector2(Gdx.input.getX(), (Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY());
		
		ItemDraggedButton = new GameButtonStandard("", current.x, current.y, GameState.getHudCam(), 1f, 1f, 0.8f, null);
		
		
		
		
	}
}
