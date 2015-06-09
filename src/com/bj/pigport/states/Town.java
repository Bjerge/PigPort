package com.bj.pigport.states;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.bj.pigport.entities.map.MapCreator;
import com.bj.pigport.entities.mapobjects.FairyCage;
import com.bj.pigport.entities.mapobjects.FairyDust;
import com.bj.pigport.entities.mapobjects.FireArrow;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.mapobjects.Destructables;
import com.bj.pigport.entities.mapobjects.MovingPlatform;
import com.bj.pigport.entities.mapobjects.Trap;
import com.bj.pigport.entities.player.abilities.Ball;
import com.bj.pigport.entities.player.abilities.BallEffect;
import com.bj.pigport.entities.player.abilities.TimeStop;
import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.entities.hud.HUD;
import com.bj.pigport.entities.hud.Inventory;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.enemy.EnemyCreator;
import com.bj.pigport.entities.enemy.EnemyMissile;
import com.bj.pigport.entities.player.PlayerController;
import com.bj.pigport.entities.player.PlayerCreator;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;

public class Town extends GameState{

	private boolean debug = false;
	public static boolean debugLocationFinder = false;
	
	private static World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dCam;
	
	private MyContactListener cl;
	
	private static TextureRegion[] letters;
	
	public static TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	public static boolean timeStop = false;
	
	public static boolean drawBall = false;
	public static boolean isPlayerTeleporting = false;
	public static boolean tpNow = false;

	
	
	
	public static float playerTeleportX;
	public static float playerTeleportY;
	
	private HUD hud;
	private boolean inventoryActive = false;
	
	
	public static float [] platformDirectionX = new float[10];
	public static float [] platformDirectionY = new float[10];
	
	public static final int miniMapZoom = 100;
	public static int difficulty = 1;
	
	public static boolean slowCamera = false;
	public static int slowCameraNumber = 0;
	public static float lerp = 0.05f;	
	
	public static MapLayer ml = null;
	public static float objectSpawnX = 0;
	public static float objectSpawnY = 0;
		
	public boolean playerJumpStarted = false;
	public static float playerJumpVelocityX;
	public boolean midAirDirectionChanged = false;
	
	public static boolean playerAttackingMelee = false;
	public static int playerAttackingEnemyTimer = 0;
	private int [] mapSize = new int [2];
	
	private TextureRegion dot;
	//private static boolean timeStopFogBoolean = false;
	
	
	public Town(GameStateManager gsm) {
		super(gsm);
		
		// setting up box2d stuff
		setWorld(new World(new Vector2(0, -9.81f), true));
		cl = new MyContactListener();
		getWorld().setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		
		MyContactListener.numFootContactsBall = 0;
		
		Loot.loot = new Loot[20];
		Loot.lootNumber = 0;
		
		PlayerCreator.createPlayer(getWorld());
		
		tileMap = MapCreator.createPlayerSpawn(tileMap, "town");
		MapCreator.createPlayerEnd(tileMap, world);
		tmr = MapCreator.createTiles(tileMap, tmr, tileSize, world);
		
		
		Ball.createBall(getWorld());
		BallEffect.createBallEffect(getWorld());
		
		Play.player.data.numFairyDust = 99;
		
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, 
				(Game.V_WIDTH * 7) / PPM, 
				(Game.V_HEIGHT * 7) / PPM);
		
		// set up HUD
		hud = new HUD(Play.player);
		Play.player.data.inventory = new Inventory(Play.player);
		
		// setup our mini map camera
	    miniMapCam = new OrthographicCamera(
	    		Game.V_WIDTH/3,
	    		Game.V_HEIGHT/3);
	    miniMapCam.zoom = miniMapZoom;
	    //sbMiniMap = new SpriteBatch();
	    
	    playerAttackingMelee = false; 
		playerAttackingEnemyTimer = 0;
		
		Texture texd = Game.res.getTexture("dot");
	    dot = new TextureRegion(texd, 0, 0, 6, 6);
	}
	
	public void handleInput() {
		
		PlayerController.PlayerControls();
		if(inventoryActive != true)Play.player.data.ball.BallControls();
		Play.player.data.ballEffect.ballEffectController();
		TimeStop.TimeStopControls();
		
		if(MyContactListener.isPlayerAtEnd())
		{
			MyContactListener.playerAtEnd = false;
			Play.level = 1;
			gsm.setState(GameStateManager.PLAY);
		}
		
		if(MyInput.isPressed(MyInput.BUTTON2) && playerAttackingMelee != true && playerAttackingEnemyTimer == 0)
		{
			playerAttackingMelee = true; 
			playerAttackingEnemyTimer = 100;
		}
		if(playerAttackingEnemyTimer > 0) playerAttackingEnemyTimer --;
		if(playerAttackingEnemyTimer == 80) playerAttackingMelee = false;
		
		if(MyInput.isPressed(MyInput.BUTTON_INVENTORY) && inventoryActive != true)
		{
			Play.player.data.inventory = new Inventory(Play.player);
			inventoryActive = true;
		}
		else if(MyInput.isPressed(MyInput.BUTTON_INVENTORY) && inventoryActive)
		{
			Play.player.data.inventory = new Inventory(Play.player);
			inventoryActive = false;
		}
		
		if(inventoryActive)Play.player.data.inventory.InputUpdater(gsm);
	}
	public void update(float dt) {
		
		handleInput();
		
		if(inventoryActive)Play.player.data.inventory.Updater(dt);
		
		// update box2d
		//world.step(Game.STEP, 1, 1);
		
		Play.player.update(dt);
		
		Play.player.data.ball.update(dt);	
		Play.player.data.ballEffect.update(dt);

		for(int i = 0; i < Loot.lootNumber; i++)Loot.loot[i].update(dt);
		
		for(int i = 0; i < Destructables.number; i ++) Destructables.destructables[i].update(dt);
		
		getWorld().step(Game.STEP, 1, 1);
	}
	
	public static final int MARKER_SIZE = 20;
	public static final int MINIMAP_LEFT = 0;
	public static final int MINIMAP_RIGHT = 200;
	public static final int MINIMAP_TOP = 480;
	public static final int MINIMAP_BOTTOM = 280;
	
	public void render() {
		//clear screen
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//modifier = 6.05f;
		getCam().zoom = 1;
		
		getCam().position.set((int) (Play.player.getPosition().x * PPM + Game.V_WIDTH / 15) ,(int) (Play.player.getPosition().y * PPM + Game.V_HEIGHT / 15f), 0);
		
		getCam().update();
		getCam().apply(Gdx.gl10);
		
		//draw tile map
		tmr.setView(getCam());
		tmr.render();
		
		// draw player
		sb.setProjectionMatrix(getCam().combined);
		
		for(int i = 0; i < Destructables.number; i ++) Destructables.destructables[i].render(sb);
		
		Play.player.render(sb);
		
		sb.begin();
		sb.draw(Play.player.data.HealthTex, (Play.player.getPosition().x * 100) - 50, (Play.player.getPosition().y * 100) + (Play.player.data.HealthTex.getHeight()/2), ((float) (((100) / (float) (Play.player.data.healthMax)) * (Play.player.data.health))) , 8);
		sb.draw(Play.player.data.HealthLeftTex, (Play.player.getPosition().x * 100) + 50, (Play.player.getPosition().y * 100) + (Play.player.data.HealthLeftTex.getHeight()/2),	 -((float) (100) - (((100) / (float) (Play.player.data.healthMax)) * (Play.player.data.health))) , 8);
		sb.end();
		
		
		
		Play.player.data.ball.render(sb);
		Play.player.data.ballEffect.render(sb);
		
		for(int i = 0; i < Loot.lootNumber; i++)Loot.loot[i].render(sb);
		
		// draw HUD
		sb.setProjectionMatrix(getHudCam().combined);
		hud.render(sb);
		if(inventoryActive)Play.player.data.inventory.render(sb);
		
		if(debug){
			b2dr.render(getWorld(), b2dCam.combined);
						
		}
	}
	
	public void dispose() {}
	
	public void startTheMap()
	{
		Play.player.data.health = Play.player.data.healthMax;
		Play.player.data.numFairyCage = 0;
		Play.player.data.numFairyDust = 0;
		MyContactListener.playerAtEnd = false;
		Play.isPlayerTeleporting = false;
		getCam().zoom = 1;	
		playerAttackingEnemyTimer = 1000;
		playerAttackingMelee = false;
		gsm.setState(GameStateManager.PLAY);
	}
	
	public static void resetSlowCam()
	{
		slowCamera = true;
		slowCameraNumber = 0;
		lerp = 0.05f;
	}

	public static World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}