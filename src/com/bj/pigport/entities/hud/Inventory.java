package com.bj.pigport.entities.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.entities.items.Items;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.GameButtonStandard;

public class Inventory {

	private GameButtonStandard [] EquippedButton;
	private GameButtonStandard [] playerItemButtons;
	private GameButtonStandard [] ItemGemSlotButtons;
	
	private GameButtonStandard infoBox;
	
	public boolean itemSelected = false;
	public boolean equippedItemSelected = false;
	private int itemNumberSelected = 0;
	
	
	private GameButtonStandard InventoryBackground;
	private GameButtonStandard BackgroundButton;
	private GameButtonStandard SellButton;
	private GameButtonStandard SellCommonButton;
	private GameButtonStandard EquipButton;
	private GameButtonStandard UnEquipButton;
	
	private Vector2 dragStart;
	public boolean itemSelectedForDrag = false;
	public boolean itemEquippedSelectedForDrag = false; 
	public int itemSelectedForDragNumber = 0; 
	
	public boolean dragBegun = false;
	public int dragEndTimer = 0;
	
	private GameButtonStandard ItemDraggedButton;
		
	public Inventory(Player player)
	{
		itemSelected = false;
		//Player.playerTotalStats();
		
		playerItemButtons = new GameButtonStandard [PlayerData.playerItems.length];
		for(int i = 0; i < PlayerData.inventorySize; i ++)
		{
			if(i >= 10) playerItemButtons[i] = new GameButtonStandard(PlayerData.playerItems[i].itemName, 880, 650 - (58 * (i-10)), GameState.getHudCam(), 2.5f, 0.9f, 0.5f, null);
			else playerItemButtons[i] = new GameButtonStandard(PlayerData.playerItems[i].itemName, 720, 650 - (58 * i), GameState.getHudCam(), 2.5f, 0.95f, 0.5f, null);
		}
		
		EquippedButton = new GameButtonStandard [3];
		for(int i = 0; i < 3; i++)
		{
			if(PlayerData.playerEquippedItems[i]==null)EquippedButton[i] = new GameButtonStandard("", 1070, 600 - (i * 150), GameState.getHudCam(), 4f, 2f, 0.8f, null);
			else EquippedButton[i] = new GameButtonStandard(PlayerData.playerEquippedItems[i].itemName, 1070, 600 - (i * 150), GameState.getHudCam(), 4f, 2f, 0.8f, null);
		}
		
		InventoryBackground = new GameButtonStandard("", 700, 670, GameState.getHudCam(), 10.7f, 10.3f, 1, null);
		infoBox = new GameButtonStandard("Farm", 1000, 1000, GameState.getHudCam(), 3.5f, 1, 1, null);
		BackgroundButton = new GameButtonStandard("Farm", 1000, 1000, GameState.getHudCam(), 3.5f, 1, 1, null);
		SellButton = new GameButtonStandard("Sell", 150, 310, GameState.getHudCam(), 4f, 1, 0.8f, null);
		EquipButton = new GameButtonStandard("Equip", 150, 240, GameState.getHudCam(), 4f, 1, 0.8f, null);
		UnEquipButton = new GameButtonStandard("Unequip", 150, 240, GameState.getHudCam(), 4f, 1, 0.8f, null);
		
		SellCommonButton = new GameButtonStandard("Sell all common", 600, 600, GameState.getHudCam(), 4f, 1, 0.8f, null);
		
		
		
		
		itemSelected = false;
		equippedItemSelected = false;
		itemNumberSelected = 0;
	}
	
	public void InputUpdater(GameStateManager gsm)
	{
		infoBoxGenerator(null, true);
		
		for(int i = 0; i < 3; i++) if(PlayerData.playerEquippedItems[i] != null)
		{
			if(EquippedButton[i].isHovered()) infoBoxGenerator(PlayerData.playerEquippedItems[i], true);
			if(EquippedButton[i].isReleased())
			{
				equippedItemSelected = true;
				itemNumberSelected = i;
				DrawBackgroundButton(i, PlayerData.playerEquippedItems);
				itemSelected = false;
			}
			if(EquippedButton[i].isClicked())
			{
				dragStart = new Vector2(Gdx.input.getX(), (Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY());
				itemEquippedSelectedForDrag = true;
				itemSelectedForDragNumber = i;
			}
		}
		
		for(int i = 0; i < PlayerData.inventorySize; i ++)
		{
			if(playerItemButtons[i].isHovered())infoBoxGenerator(PlayerData.playerItems[i], false);
			if(playerItemButtons[i].isReleased())
			{
				itemSelected = true;
				itemNumberSelected = i;
				DrawBackgroundButton(i, PlayerData.playerItems);
				equippedItemSelected = false;
			}
			if(playerItemButtons[i].isClicked())
			{
				dragStart = new Vector2(Gdx.input.getX(), (Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY());
				itemSelectedForDrag = true;
				itemSelectedForDragNumber = i;
			}
		}
		
		
		if(SellButton.isClicked())
		{
			PlayerData.gold += PlayerData.playerItems[itemNumberSelected].sellValue;
			Items.deleteItemFromList(itemNumberSelected);
		}
		
		if(SellCommonButton.isClicked())
		{
			for(int i = 19; i > -1; i--) if(PlayerData.playerItems[i] != null && PlayerData.playerItems[i].itemRareness == 1)
			{
				PlayerData.gold += PlayerData.playerItems[i].sellValue;
				Items.deleteItemFromList(i);
			}
		}
		
		
		if(EquipButton.isClicked() && itemSelected)
		{			
			Items item = PlayerData.playerEquippedItems[PlayerData.playerItems[itemNumberSelected].type];
			PlayerData.playerEquippedItems[PlayerData.playerItems[itemNumberSelected].type] = PlayerData.playerItems[itemNumberSelected];
			Items.deleteItemFromList(itemNumberSelected);
			itemNumberSelected = 0;
			if(item != null)Items.addItemToList(item);
		}
		
		if(UnEquipButton.isClicked() && equippedItemSelected)
		{			
			Items.addItemToList(PlayerData.playerEquippedItems[itemNumberSelected]);
			PlayerData.playerEquippedItems[itemNumberSelected] = null;
			equippedItemSelected = false;
			PlayerData.inventory = new Inventory(PlayerData.player);
		}
		
		if(itemSelected)
		{
			for(int i = 0; i < PlayerData.playerItems[itemNumberSelected].gemSlotNumber; i++)
			{
				if(ItemGemSlotButtons[i] != null) 
				{
					if(ItemGemSlotButtons[i].isHovered()) if(PlayerData.playerItems[itemNumberSelected].gems[i] != null) infoBoxGenerator(PlayerData.playerItems[itemNumberSelected].gems[i], false);
					if(ItemGemSlotButtons[i].isReleased() && dragBegun)
					{
						if(itemSelectedForDrag && PlayerData.playerItems[itemSelectedForDragNumber].type == 3)
						{
							PlayerData.playerItems[itemNumberSelected].gems[i] = PlayerData.playerItems[itemSelectedForDragNumber];
							Items.deleteItemFromList(itemSelectedForDragNumber);
						}
					}
				}
			}
		}
		
		if(equippedItemSelected)
		{
			for(int i = 0; i < PlayerData.playerEquippedItems[itemNumberSelected].gemSlotNumber; i++)
			{
				if(ItemGemSlotButtons[i] != null) 
				{
					if(ItemGemSlotButtons[i].isHovered()) if(PlayerData.playerEquippedItems[itemNumberSelected].gems[i] != null) infoBoxGenerator(PlayerData.playerEquippedItems[itemNumberSelected].gems[i], false);
					if(ItemGemSlotButtons[i].isReleased() && dragBegun)
					{
						if(itemSelectedForDrag && PlayerData.playerItems[itemSelectedForDragNumber].type == 3)
						{
							PlayerData.playerEquippedItems[itemNumberSelected].gems[i] = PlayerData.playerItems[itemSelectedForDragNumber];
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
		for(int i = 0; i < PlayerData.inventorySize; i ++) playerItemButtons[i].update(dt);
		for(int i = 0; i < 3; i++)EquippedButton[i].update(dt);
		infoBox.update(dt);
		//SellCommonButton.update(dt);
		InventoryBackground.update(dt);
		
		if(itemSelected)
		{
			BackgroundButton.update(dt);
			for(int i = 0; i < PlayerData.playerItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].update(dt);;
			SellButton.update(dt);
			EquipButton.update(dt);
			
		}
		if(equippedItemSelected)
		{
			BackgroundButton.update(dt);
			UnEquipButton.update(dt);
			for(int i = 0; i < PlayerData.playerEquippedItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].update(dt);
		}
		
		if(dragBegun)ItemDraggedButton.update(dt);
		
	}
	
	public void render(SpriteBatch sb)
	{
		sb.begin();
		//sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sb.end();
		InventoryBackground.render(sb);	
		for(int i = 0; i < PlayerData.inventorySize; i ++)playerItemButtons[i].render(sb);
		for(int i = 0; i < 3; i++)EquippedButton[i].render(sb);
		//SellCommonButton.render(sb);
		if(itemSelected)
		{
			BackgroundButton.render(sb);
			for(int i = 0; i < PlayerData.playerItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].render(sb);
			SellButton.render(sb);
			EquipButton.render(sb);
		}
		if(equippedItemSelected)
		{
			BackgroundButton.render(sb);
			UnEquipButton.render(sb);
			for(int i = 0; i < PlayerData.playerEquippedItems[itemNumberSelected].gemSlotNumber; i++) if(ItemGemSlotButtons[i] != null) ItemGemSlotButtons[i].render(sb);
		}
		
		if(dragBegun)ItemDraggedButton.render(sb);
		
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
		else if(PlayerData.playerEquippedItems[item.type] != null)
		{
			long diffStr = (item.statStrength - PlayerData.playerEquippedItems[item.type].statStrength);
			long diffBrn = (item.statBrain - PlayerData.playerEquippedItems[item.type].statBrain);
			long diffMnd = (item.statMind - PlayerData.playerEquippedItems[item.type].statMind);
			long diffHlh = (item.statHealth - PlayerData.playerEquippedItems[item.type].statHealth);
			
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
			else if(PlayerData.playerEquippedItems[slotNumber]!=null)
			{
				infoBox = new GameButtonStandard(PlayerData.playerEquippedItems[slotNumber].itemName + 
					"~~Strenght: " + PlayerData.playerEquippedItems[slotNumber].statStrength +
					"~Brain: " + PlayerData.playerEquippedItems[slotNumber].statBrain +
					"~Defence: " + PlayerData.playerEquippedItems[slotNumber].statMind +
					"~Item Level: " + PlayerData.playerEquippedItems[slotNumber].itemLevel +
					"~Item Rareness: " + PlayerData.playerEquippedItems[slotNumber].RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
			}
		}
		else if(PlayerData.playerEquippedItems[PlayerData.playerItems[slotNumber].type] != null)
		{
			long diffStr = (PlayerData.playerItems[slotNumber].statStrength - PlayerData.playerEquippedItems[PlayerData.playerItems[slotNumber].type].statStrength);
			long diffBrn = (PlayerData.playerItems[slotNumber].statBrain - PlayerData.playerEquippedItems[PlayerData.playerItems[slotNumber].type].statBrain);
			long diffMnd = (PlayerData.playerItems[slotNumber].statMind - PlayerData.playerEquippedItems[PlayerData.playerItems[slotNumber].type].statMind);
			long diffHlh = (PlayerData.playerItems[slotNumber].statHealth - PlayerData.playerEquippedItems[PlayerData.playerItems[slotNumber].type].statHealth);
			
			infoBox = new GameButtonStandard(PlayerData.playerItems[slotNumber].itemName + 
				"~~Strenght: " + PlayerData.playerItems[slotNumber].statStrength + " (" + diffStr + ")" +
				"~Brain: " + PlayerData.playerItems[slotNumber].statBrain + " (" + diffBrn + ")" +
				"~Mind: " + PlayerData.playerItems[slotNumber].statMind + " (" + diffMnd + ")" +
				"~Health: " + PlayerData.playerItems[slotNumber].statHealth + " (" + diffHlh + ")" +
				"~Item Level: " + PlayerData.playerItems[slotNumber].itemLevel +
				"~Item Rareness: " + PlayerData.playerItems[slotNumber].RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
		}
		else
		{
			infoBox = new GameButtonStandard(PlayerData.playerItems[slotNumber].itemName + 
					"~~Strenght: " + PlayerData.playerItems[slotNumber].statStrength + 
					"~Brain: " + PlayerData.playerItems[slotNumber].statBrain +
					"~Mind: " + PlayerData.playerItems[slotNumber].statMind +
					"~Health: " + PlayerData.playerItems[slotNumber].statHealth +
					"~Item Level: " + PlayerData.playerItems[slotNumber].itemLevel +
					"~Item Rareness: " + PlayerData.playerItems[slotNumber].RarenessName, MyInput.x, (Game.V_HEIGHT * Game.CAMZOOM) - MyInput.y, GameState.getHudCam(), 5f, 5f, 0.8f, null);
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
			
			System.out.println(itemList[slotNumber].gemSlotNumber);
			
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
