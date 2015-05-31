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
import com.bj.pigport.entities.mapobjects.FairyCage;
import com.bj.pigport.entities.mapobjects.FairyDust;
import com.bj.pigport.entities.mapobjects.FireArrow;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.mapobjects.Destructables;
import com.bj.pigport.entities.mapobjects.MovingPlatform;
import com.bj.pigport.entities.mapobjects.Trap;
import com.bj.pigport.entities.player.abilities.AttackAnimation;
import com.bj.pigport.entities.player.abilities.AttackAnimationCD;
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
	public static AttackAnimation attackAnimation;
	public static AttackAnimationCD attackAnimationCD;
	
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
		
		createPlayerSpawn();
		createPlayerEnd();
		PlayerCreator.createPlayer(getWorld());
		createTiles();
		Ball.createBall(getWorld());
		BallEffect.createBallEffect(getWorld());
		createAttackAnimationStar();
		createAttackAnimationStarCD();
		
		Player.numFairyDust = 99;
		
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, 
				(Game.V_WIDTH * 7) / PPM, 
				(Game.V_HEIGHT * 7) / PPM);
		
		// set up HUD
		hud = new HUD(PlayerData.player);
		PlayerData.inventory = new Inventory(PlayerData.player);
		
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
		if(inventoryActive != true)Ball.BallControls();
		BallEffect.ballEffectController();
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
			PlayerData.inventory = new Inventory(PlayerData.player);
			inventoryActive = true;
		}
		else if(MyInput.isPressed(MyInput.BUTTON_INVENTORY) && inventoryActive)
		{
			PlayerData.inventory = new Inventory(PlayerData.player);
			inventoryActive = false;
		}
		
		if(inventoryActive)PlayerData.inventory.InputUpdater(gsm);
	}
	public void update(float dt) {
		
		handleInput();
		
		if(inventoryActive)PlayerData.inventory.Updater(dt);
		
		// update box2d
		//world.step(Game.STEP, 1, 1);
		
		PlayerData.player.update(dt);
		
		if(playerAttackingEnemyTimer > 90)
		{
			attackAnimation.getBody().setTransform(PlayerData.player.getPosition().x, PlayerData.player.getPosition().y, 0);
			attackAnimation.update(dt);
		}
		else if(playerAttackingEnemyTimer > 0)
		{
			attackAnimationCD.getBody().setTransform(PlayerData.player.getPosition().x, PlayerData.player.getPosition().y, 0);
			attackAnimationCD.update(dt);
		}
		
		PlayerData.ball.update(dt);	
		PlayerData.ballEffect.update(dt);

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
		
		/*
		getCam().position.set((Game.V_WIDTH * Game.SCALE)/2, (Game.V_HEIGHT * Game.SCALE / 2), 0);
		
		if(PlayerData.player.getPosition().x * PPM > (Game.V_WIDTH * Game.SCALE /2 ))
		{
			getCam().position.set((PlayerData.player.getPosition().x * PPM), getCam().position.y, 0);
		}
		if(PlayerData.player.getPosition().y * PPM > (Game.V_HEIGHT * Game.SCALE / 2) + 32)
		{
			getCam().position.set(getCam().position.x, (PlayerData.player.getPosition().y * PPM), 0);
		}
		if(PlayerData.player.getPosition().x * PPM > mapSize[0] - (Game.V_WIDTH * Game.SCALE / 2))
		{
			getCam().position.set(mapSize[0] - (Game.V_WIDTH * Game.SCALE / 2), getCam().position.y, 0);
		}	
		if(PlayerData.player.getPosition().y * PPM > mapSize[1] - (Game.V_HEIGHT * Game.SCALE / 2) - 32)
		{
			getCam().position.set(getCam().position.x , mapSize[1] - (Game.V_HEIGHT * Game.SCALE / 2) - 32, 0);
		}
		*/
		
		getCam().position.set((int) (PlayerData.player.getPosition().x * PPM + Game.V_WIDTH / 15) ,(int) (PlayerData.player.getPosition().y * PPM + Game.V_HEIGHT / 15f), 0);
		
		getCam().update();
		getCam().apply(Gdx.gl10);
		
		//draw tile map
		tmr.setView(getCam());
		tmr.render();
		
		// draw player
		sb.setProjectionMatrix(getCam().combined);
		
		for(int i = 0; i < Destructables.number; i ++) Destructables.destructables[i].render(sb);
		
		PlayerData.player.render(sb);
		
		sb.begin();
		sb.draw(Player.HealthTex, (PlayerData.player.getPosition().x * 100) - 50, (PlayerData.player.getPosition().y * 100) + (PlayerData.player.getTexture().getHeight()/2), ((float) (((100) / (float) (Player.playerStartHealth)) * (PlayerData.player.health))) , 8);
		sb.draw(Player.HealthLeftTex, (PlayerData.player.getPosition().x * 100) + 50, (PlayerData.player.getPosition().y * 100) + (PlayerData.player.getTexture().getHeight()/2),	 -((float) (100) - (((100) / (float) (Player.playerStartHealth)) * (PlayerData.player.health))) , 8);
		sb.end();
		
		
		
		PlayerData.ball.render(sb);
		PlayerData.ballEffect.render(sb);
		
		for(int i = 0; i < Loot.lootNumber; i++)Loot.loot[i].render(sb);
		
		if(playerAttackingEnemyTimer > 90) attackAnimation.render(sb);
		else if(playerAttackingEnemyTimer > 0) attackAnimationCD.render(sb);

		sb.begin();
		/*
		if (MyInput.isDown())
		{
			for(float i = 1; i < (6.5f + (0.5 * Player.ballDotLevel)) ;)
			{
				Ball.getDrawBall(i, getCam().position.x, getCam().position.y);
				sb.draw(dot,Ball.xBallCoordinate, Ball.yBallCoordinate);
				i += 0.5f;
				
				//if(i == 2) System.out.println(Ball.xBallCoordinate + " " + Ball.yBallCoordinate + " " + PlayerData.player.getPosition().x * 100 + " " + PlayerData.player.getPosition().y * 100);
			}
		}
		*/
		sb.end();
		
		// draw HUD
		sb.setProjectionMatrix(getHudCam().combined);
		hud.render(sb);
		if(inventoryActive)PlayerData.inventory.render(sb);
		
		if(debug){
			b2dr.render(getWorld(), b2dCam.combined);
						
		}
	}
	
	public void dispose() {}
	
	
	private void createPlayerSpawn(){
		
		try {
			tileMap = new TmxMapLoader().load("res/maps/town.tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: res/maps/town.tmx");
			Gdx.app.exit();
		}
		MapLayer ml = tileMap.getLayers().get("playerSpawn");
		for(MapObject mo : ml.getObjects()){
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			if(mo instanceof RectangleMapObject){
				PlayerData.playerSpawnX =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				PlayerData.playerSpawnY =  ((RectangleMapObject) mo).getRectangle().y / PPM;

			}
			else if(mo instanceof EllipseMapObject){
				PlayerData.playerSpawnX = ((EllipseMapObject) mo).getEllipse().x / PPM;
				PlayerData.playerSpawnY = ((EllipseMapObject) mo).getEllipse().y / PPM;
			}
		}		
	}
	
	private void createPlayerEnd()
	{
		MapLayer ml = tileMap.getLayers().get("playerEnd");
		if(ml == null) return;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for(MapObject mo : ml.getObjects()){
			bdef.type = BodyType.StaticBody;
			if(mo instanceof RectangleMapObject){
				float x =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				float y =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				bdef.position.set(x, y);
			}
			else if(mo instanceof EllipseMapObject){
				float x =  ((EllipseMapObject) mo).getEllipse().x / PPM;
				float y =  ((EllipseMapObject) mo).getEllipse().y / PPM;
				bdef.position.set(x, y);
			}
			CircleShape cshape = new CircleShape();
			cshape.setRadius(32 / PPM);
			fdef.shape = cshape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_END;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			Body body = getWorld().createBody(bdef);
			body.createFixture(fdef).setUserData("end");
			cshape.dispose();
		}	
	}
	
	private void createTiles(){
		// load tile map
		try {
			tileMap = new TmxMapLoader().load("res/maps/town.tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: res/maps/town.tmx");
			Gdx.app.exit();
		}
		
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tileSize = (int) tileMap.getProperties().get("tilewidth");
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("coll");
		createLayer (layer, B2DVars.BIT_COLL);
	}
	
	private void createLayer(TiledMapTileLayer layer, short bits){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		// go through all the cells in the layer
		//y-axis direction
		mapSize[0] = (int) (layer.getWidth() * tileSize);
		mapSize[1] = (int) (layer.getHeight() * tileSize);
		
		for(int row = 0; row < layer.getHeight(); row++){
			//x-axis direction
			for(int col = 0; col < layer.getWidth(); col++){
						
				// get cell
				Cell cell = layer.getCell(col, row);
						
				// check if cells exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
						
				// create a body + fixture from cell;
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / PPM, //width
					(row + 0.5f) * tileSize / PPM //height
				);	
						
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[3] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[4] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				cs.createChain(v);
				fdef.friction = 2;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BALL | B2DVars.BIT_ENEMY | B2DVars.BIT_MOVINGPLATFORM | B2DVars.BIT_FIREARROW | B2DVars.BIT_OBJECTS | B2DVars.BIT_LOOT; 
				fdef.isSensor = false;
				getWorld().createBody(bdef).createFixture(fdef);
				cs.dispose();
			}
		}
	}
	
	public void startTheMap()
	{
		PlayerData.player.health = PlayerData.player.healthMax;
		Player.numFairyCage = 0;
		Player.numFairyDust = 0;
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
	
	private void createAttackAnimationStar(){
		
		BodyDef bdef = new BodyDef();
		//create ball
		bdef.position.set(PlayerData.player.getPosition().x, PlayerData.player.getPosition().y);
		bdef.type = BodyType.DynamicBody;
		Body body = getWorld().createBody(bdef);
		CircleShape shape = new CircleShape();
		shape.setRadius(12 / PPM);	
		attackAnimation = new AttackAnimation(body);
		body.setUserData(attackAnimation);
		attackAnimation.getBody().setGravityScale(0f);
		shape.dispose();
	}	
	
	private void createAttackAnimationStarCD(){
		
		BodyDef bdef = new BodyDef();
		//create ball
		bdef.position.set(PlayerData.player.getPosition().x, PlayerData.player.getPosition().y);
		bdef.type = BodyType.DynamicBody;
		Body body = getWorld().createBody(bdef);
		CircleShape shape = new CircleShape();
		shape.setRadius(12 / PPM);	
		attackAnimationCD = new AttackAnimationCD(body);
		body.setUserData(attackAnimationCD);
		attackAnimationCD.getBody().setGravityScale(0f);
		shape.dispose();
	}

	public static World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}