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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
import com.bj.pigport.entities.player.abilities.Weapon;
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
import com.bj.pigport.handlers.GameButtonStandard;
import com.bj.pigport.handlers.GameStateManager;
import com.bj.pigport.handlers.LootButton;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.handlers.MyInput;
import com.bj.pigport.main.Game;

public class Play extends GameState{

	private boolean debug = false;
	public static boolean debugLocationFinder = false;
	
	private static World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dCam;
	
	private MyContactListener cl;
	
	private static TextureRegion[] letters;
	
	public static TiledMap tileMap;
	private float tileSize;
	public OrthogonalTiledMapRenderer tmr;
		
	public static boolean timeStop = false;
	
	public static boolean drawBall = false;
	public static boolean isPlayerTeleporting = false;
	public static boolean tpNow = false;

	public static Player player;
	
	
	public static float playerTeleportX;
	public static float playerTeleportY;
	
	private Array<Trap> trap;
	
	
	private HUD hud;
	private boolean inventoryActive = false;
	
	
	public static int level;
	int countCages = 0;
	
	//Enemy
	public static Enemy[] enemy;
	public static int enemyNumber;
		
	public static boolean enemyDirectionChanger = false;
	
	public static int platformNr;
	public static int [] platformMovingRightLeft = new int[10];
	public static MovingPlatform[] movingPlatform;
	public static float [] platformSpawnX = new float[10];
	public static float [] platformSpawnY = new float[10];
	private boolean platformDirectionChanger = true;
	
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
	
	public boolean TPAnimationReady = false;
	
	public boolean playerJumpStarted = false;
	public static float playerJumpVelocityX;
	public boolean midAirDirectionChanged = false;
	
	public static Array<Body> enemiesThatWillGetKilled;
	public static int playerAttackCDTimer = 0;
	
	public static int enemyNumberHitFirst = 100;
	public static int enemyNumberHitSecond = 100;
	public static boolean wasAnEnemyDeleted = false;
	
	public float [] fireArrowSpawnX = new float [100];
	public float [] fireArrowSpawnY = new float [100];
	public static int fireArrowSpawnNumber = 0;
	public static float [] fireArrowSpawnDirection = new float [100];
	
	public static int fireArrowSpawnLocationNumber = 1;
	public static int fireArrowTimer = 100;
	
	public static FireArrow [] fireArrow;
	public static float [] fireArrowDirection;
	public static int fireArrowNumber;
	public static int fireArrowNumberInitialised;
	public static boolean [] fireArrowInitialised;
	
	public static float [] FASpawnSlotX;
	public static float [] FASpawnSlotY;
	
	public static int [] mapSize = new int [2];
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		// setting up box2d stuff
		setWorld(new World(new Vector2(0, -9.81f), true));
		cl = new MyContactListener();
		getWorld().setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		
		MyContactListener.numFootContactsBall = 0;
		
		Loot.loot = new Loot[200];
		Loot.lootButton = new LootButton[200];
		Loot.lootNumber = 0;
		
		PlayerCreator.createPlayer(getWorld());
		
		tileMap = MapCreator.createPlayerSpawn(tileMap, "level" + level);
		MapCreator.createPlayerEnd(tileMap, world);
		tmr = MapCreator.createTiles(tileMap, tmr, tileSize, world);
		
		//createFairyDust();
		//createFairyCage();
		createTrap();
		EnemyCreator.createEnemy();
		createMovingPlatform();
		
		Destructables.getDestructablesSpawn();
		Destructables.createDestructables();
		
		Ball.createBall(getWorld());
		BallEffect.createBallEffect(getWorld());
		Weapon.createWeapon(getWorld(), "axe1h");
		
		createFireArrowSpawn();
		fireArrow = new FireArrow[50];
		fireArrowDirection = new float[50];
		
		FASpawnSlotX = new float [50];
		FASpawnSlotY = new float [50];
		
		fireArrowNumber = 1;
		fireArrowNumberInitialised = 1;
		fireArrowInitialised = new boolean [50];
		if(fireArrowSpawnNumber > 0)
		{
			for(int i = 1; i < 50; i++)
			{
				createFireArrow();
			}
		}
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, 
				(Game.V_WIDTH * 7) / PPM, 
				(Game.V_HEIGHT * 7) / PPM);
		
		
		// set up HUD
		hud = new HUD(player);
		player.data.inventory = new Inventory(player);
		
		// setup our mini map camera
	    miniMapCam = new OrthographicCamera(
	    		Game.V_WIDTH/3,
	    		Game.V_HEIGHT/3);
	    miniMapCam.zoom = miniMapZoom;
	    //sbMiniMap = new SpriteBatch();
	    
	    
	    
	    enemiesThatWillGetKilled = new Array<Body>();
	    
	    
	    player.data.playerAttackingMelee = false; 
	    player.data.playerAttackingEnemyTimer = 0;
		startTheMap();
	    
	}
	
	public void handleInput() {
		
		
		if(MyInput.isPressed(MyInput.BUTTON_INVENTORY) && inventoryActive != true)
		{
			player.data.inventory = new Inventory(player);
			inventoryActive = true;
		}
		else if(MyInput.isPressed(MyInput.BUTTON_INVENTORY) && inventoryActive)
		{
			player.data.inventory = new Inventory(player);
			inventoryActive = false;
		}
		
		if(inventoryActive)player.data.inventory.InputUpdater(gsm);
		
		if(HUD.returnButton.isClicked()) {
			getCam().zoom = 1;
			gsm.setState(GameStateManager.TOWN);
		}
		
		if(HUD.restartButton.isClicked())
		{
			startTheMap();
			gsm.setState(GameStateManager.PLAY);
		}
		
		//if(MyContactListener.isPlayerAtEnd() && player.data.numFairyCage == player.data.totalFairyCage)
		if(MyContactListener.isPlayerAtEnd())
		{
			player.data.fairyDustCollectedInFinishedMap = player.data.numFairyDust;
			player.data.fairyDustCollectedInAllMaps += player.data.fairyDustCollectedInFinishedMap;
			player.data.gainedXP = player.data.gainedXP + 10 + player.data.fairyDustCollectedInFinishedMap;
			player.playerExperienceCalculator();
			getCam().zoom = 1;
			level++;
			gsm.setState(GameStateManager.PLAY);
		}
		
		if(player.data.health == 0)
		{
			startTheMap();
			player.data.gainedXP = 0;	
			gsm.setState(GameStateManager.TOWN); 
		}
		
		
		if(player.data.damageContact && player.data.damageCD == 0)
		{
			player.data.damageCD = 100;
			player.data.health -= 5;	
			
			if(player.data.knockBackContact)
			{
				player.data.knockBackCD = 30;
				Vector2 vecPlayer = new Vector2(player.getBody().getPosition().x, player.getBody().getPosition().y);
				int angleDamage = (int) (Math.toDegrees(Math.atan2(player.data.knockBackSource.y - vecPlayer.y , player.data.knockBackSource.x - vecPlayer.x)));
				if(angleDamage < 0) angleDamage = (360 + angleDamage);	
				float forceX = (((float) (Math.cos(Math.toRadians(angleDamage)))) * 200);
				float forceY = (((float) (Math.sin(Math.toRadians(angleDamage)))) * 200);
				
				player.getBody().setLinearVelocity(0 , 0);
				player.getBody().applyForceToCenter(-forceX , -forceY, true);
			}
		}
		if(player.data.damageCD > 0) player.data.damageCD --;
		if(player.data.knockBackCD > 0) player.data.knockBackCD --;
		if(player.data.knockBackCD == 0) PlayerController.PlayerControls();
		if(inventoryActive != true)player.data.ball.BallControls();
		
		Play.player.data.ballEffect.ballEffectController();
		Play.player.data.weapon.WeaponController();
		
		TimeStop.TimeStopControls();
		
		for(int i = 0; i < Destructables.number; i ++) Destructables.destructables[i].DestructablesController();
		for(int i = 0; i < enemyNumber; i++)
		{
			//System.out.println(i + " " + enemyNumber + " " + enemy[i].data.warlock);
			enemy[i].enemyControllerV2();
		}

		for(int i = Loot.lootNumber-1; i > -1; i--) 
		{
			Loot.loot[i].LootButtonController(getCam());
		}
	}
	public void update(float dt) {
		HUD.returnButton.update(dt);
		HUD.restartButton.update(dt);
		if(inventoryActive)player.data.inventory.Updater(dt);
		
		handleInput();
		
		if(cl.footContactsPLF2){
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x+movingPlatform[cl.playerOnPlatformNr].getBody().getLinearVelocity().x,
					player.getBody().getLinearVelocity().y);
		}
		
		player.update(dt);
		player.data.ball.update(dt);	
		player.data.ballEffect.update(dt);
		player.data.weapon.update(dt);
		for(int i = 0; i < Loot.lootNumber; i++)Loot.loot[i].update(dt);
		for(int i = 0; i < Loot.lootNumber; i++)Loot.lootButton[i].update(dt);
		for(int i = 0; i < trap.size; i++)trap.get(i).update(dt);	
		for(int i = 0; i < enemyNumber; i++){
			if(!(enemy[i] == null))
			{
				enemy[i].update(dt);
				for(int ii = 0; ii < enemy[i].data.missilesNumber; ii++)enemy[i].data.enemyMissiles[ii].update(dt);
			}
		}	
		
		for(int i = 0; i < platformNr; i++)if(!(movingPlatform[i] == null))movingPlatform[i].update(dt);	
		for(int i = 1; i < fireArrowNumber; i++) if(!(fireArrow[i] == null))fireArrow[i].update(dt);	
		for(int i = 0; i < Destructables.number; i ++) Destructables.destructables[i].update(dt);
		
		if(cl.isPlayerTouchingPLFRight())player.getBody().setTransform(player.getBody().getPosition().x - 5/ PPM, player.getBody().getPosition().y, 0);
		if(cl.isPlayerTouchingPLFLeft())player.getBody().setTransform(player.getBody().getPosition().x + 5/ PPM, player.getBody().getPosition().y, 0);
		MovingPlatform.PlatformMove();
		
		if(fireArrowSpawnNumber > 0 && timeStop != true)
		{
			fireArrowTimer++;
			if(fireArrowTimer >= 150)
			{
				for(int i = 1; i < (fireArrowSpawnNumber + 1); i++)
				{
					fireArrowSpawnLocationNumber = i;
					fireArrowDirection[fireArrowNumberInitialised] = fireArrowSpawnDirection[i];
					InitialiseFireArrow();						
				}
				fireArrowTimer = 0;
			}
		}
		FireArrow.FireArrowMove();
		
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
		if(getCam().zoom > 1) getCam().zoom -=0.005;
		
		/*
		if(isPlayerTeleporting && PlayerData.ball != null)
		{
			cam.position.set(
			(int) ((((PlayerData.ball.getPosition().x - player.getPosition().x) / 3) + player.getPosition().x) * PPM + Game.V_WIDTH / 15),
			(int) ((((PlayerData.ball.getPosition().y - player.getPosition().y) / 3) + player.getPosition().y) * PPM + Game.V_HEIGHT / 15), 0);
			
			float cameraLengthX = PlayerData.ball.getPosition().x - player.getPosition().x;
			float cameraLengthY = PlayerData.ball.getPosition().y - player.getPosition().y;
			float cameraLength = (float) (Math.sqrt(Math.pow(cameraLengthX, 2.0)+ Math.pow(cameraLengthY, 2.0)));
		
			if(cameraLengthY > 4 || cameraLengthY < -1 )
			{
				cam.zoom += (cameraLength/500);
			}
		}
		
		if(slowCamera)
		{
			Vector3 position = cam.position;
			position.x += (int) ((player.getPosition().x * PPM + Game.V_WIDTH / 15 - position.x) * lerp);
			position.y += (int) ((player.getPosition().y * PPM + Game.V_HEIGHT / 15 - position.y) * lerp);
			cam.position.set(position);
			slowCameraNumber++;
			lerp += 0.005;
			
			if(slowCameraNumber == 200)
			{
				slowCamera = false;
				slowCameraNumber = 0;
				lerp = 0.05f;
			}
		}

		else 
		*/
		getCam().position.set((int) (player.getPosition().x * PPM + Game.V_WIDTH / 15) ,(int) (player.getPosition().y * PPM + Game.V_HEIGHT / 15f), 0);
		
		getCam().update();
		getCam().apply(Gdx.gl10);
		
		//draw tile map
		tmr.setView(getCam());
		tmr.render();
		
		// draw player
		sb.setProjectionMatrix(getCam().combined);
		
		for(int i = 0; i < Destructables.number; i ++) Destructables.destructables[i].render(sb);
		
		sb.begin();
		boolean direction = true;
		if(player.data.direction == -1)direction = false;
		
		sb.draw(player.data.weapon.weaponTex, (player.getPosition().x * 100) - (player.data.weapon.weaponTex.getWidth()/2) - (40 * Play.player.data.direction), 
				(player.getPosition().y * 100) - (player.data.weapon.weaponTex.getHeight()/2),
				player.data.weapon.weaponTex.getWidth() / 2, 
				player.data.weapon.weaponTex.getHeight() / 2, 
				player.data.weapon.weaponTex.getWidth(), 
				player.data.weapon.weaponTex.getHeight(),  
				1, 1, player.data.weaponRotation * player.data.direction, 0, 0, 
				player.data.weapon.weaponTex.getWidth(), 
				player.data.weapon.weaponTex.getHeight(), direction, false);
		sb.end();
		
		player.render(sb);
		
		sb.begin();
		sb.draw(player.data.HealthTex, (player.getPosition().x * 100) - 50, 
				(player.getPosition().y * 100) + (player.data.HealthTex.getHeight()/2), 
				((float) (((100) / (float) (player.data.healthMax)) * (player.data.health))) , 8);
		sb.draw(player.data.HealthLeftTex, (player.getPosition().x * 100) + 50, 
				(player.getPosition().y * 100) + (player.data.HealthLeftTex.getHeight()/2),	 
				-((float) (100) - (((100) / (float) (player.data.healthMax)) * (player.data.health))) , 8);
		sb.end();
		
		player.data.ball.render(sb);
		player.data.ballEffect.render(sb);
		player.data.weapon.render(sb);
		for(int i = 0; i < Loot.lootNumber; i++)Loot.loot[i].render(sb);
		for(int i = 0; i < Loot.lootNumber; i++)Loot.lootButton[i].render(sb);
		for(int i = 0; i < enemyNumber; i++)enemy[i].enemyRender(sb);
		for(int i = 0; i < trap.size; i++)trap.get(i).render(sb);	
		for(int i = 0; i < platformNr; i++) if(!(movingPlatform[i] == null)) movingPlatform[i].render(sb);
		for(int i = 1; i < fireArrowNumber; i++) if(!(fireArrow[i] == null)) fireArrow[i].render(sb);
		
		// draw HUD
		sb.setProjectionMatrix(getHudCam().combined);
		hud.render(sb);
		if(inventoryActive)player.data.inventory.render(sb);
		
		/**
		 * SLET IKKKE! :(
		 */
		
		/*
		// setup our mini map camera
	    miniMapCam = new OrthographicCamera(Game.V_WIDTH/Game.CAMZOOM,Game.V_HEIGHT/Game.CAMZOOM);
	    miniMapCam.zoom = miniMapZoom;
	    //sbMiniMap = new SpriteBatch();
	     */
				
		
		// update our camera
	   // miniMapCam.update();
	    //miniMapCam.apply(Gdx.gl10);
	    
	   // tmr.setView(miniMapCam);
	//	tmr.render();

	    // set the projection matrix for our batch so that it draws
	    // with the zoomed out perspective of the minimap camera
	 //   sbMiniMap.setProjectionMatrix(miniMapCam.combined);
	    
		//player.render(sbMiniMap);
	
	    
	    /*
	     * 
	     * (heart, 315, 680);
	    // draw the player
	    sbMiniMap.begin();
	    //sbMiniMap.draw(player.tex, player.getPosition().x - (Game.V_WIDTH/3)*miniMapZoom + ((MINIMAP_RIGHT-MINIMAP_LEFT)/3)*miniMapZoom, 
	    	//	player.getPosition().y + (Game.V_HEIGHT/3)*miniMapZoom - ((MINIMAP_TOP-MINIMAP_BOTTOM)/3)*miniMapZoom, MARKER_SIZE, MARKER_SIZE);
	    sbMiniMap.draw(player.getTexture(), player.getPosition().x/3,player.getPosition().y/3);
	    sbMiniMap.end();
	    
	    miniMapCam.position.set(
				player.getPosition().x * 10 + Game.V_WIDTH / 25,
				player.getPosition().y * 10 + Game.V_HEIGHT / 25, 0);
				*/
	    
	  //- (WIDTH/2)*SCALE + ((MINIMAP_RIGHT-MINIMAP_LEFT)/2)*SCALE, player.getY() 
	    //+ (HEIGHT/2)*SCALE - ((MINIMAP_TOP-MINIMAP_BOTTOM)/2)*SCALE, MARKER_SIZE, MARKER_SIZE);
	   
		//draw box2d world
		
	
		if(debug){
			b2dr.render(getWorld(), b2dCam.combined);
						
		}
	}
	
	public void dispose() {}
	
	private void createFairyDust(){
		//fairyDust = new Array<FairyDust>();
		MapLayer layer = null;
		if(difficulty == 1) 
		{
			layer = tileMap.getLayers().get("fairyDustEasy");
		}
		if(layer == null) return;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for(MapObject mo : layer.getObjects()){
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
			cshape.setRadius(15 / PPM);
			fdef.shape = cshape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_LOOT;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			Body body = getWorld().createBody(bdef);
			body.createFixture(fdef).setUserData("dust");
			FairyDust f = new FairyDust(body);
			//fairyDust.add(f);
			body.setUserData(f);
			cshape.dispose();
		}
	}
	
	private void createFairyCage(){
		//fairyCage = new Array<FairyCage>();
		MapLayer ml = tileMap.getLayers().get("fairyCage");
		
		countCages = 0;
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.StaticBody;
			if(mo instanceof RectangleMapObject){
				float x =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				float y =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				bdef.position.set(x, y);
				countCages++;
			}
			else if(mo instanceof EllipseMapObject){
				float x =  ((EllipseMapObject) mo).getEllipse().x / PPM;
				float y =  ((EllipseMapObject) mo).getEllipse().y / PPM;
				bdef.position.set(x, y);
				countCages++;
			}
						
			player.data.totalFairyCage = countCages;
			CircleShape cshape = new CircleShape();
			cshape.setRadius(32 / PPM);
			FixtureDef fdef = new FixtureDef();
			fdef.shape = cshape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_LOOT;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			Body body = getWorld().createBody(bdef);
			body.createFixture(fdef).setUserData("cage");
			FairyCage c = new FairyCage(body);
			//fairyCage.add(c);
			body.setUserData(c);
			cshape.dispose();
			
		}	
	}	
	
	private void createTrap(){
		trap = new Array<Trap>();
		MapLayer ml = null;
		if(difficulty == 1) 
		{
			ml = tileMap.getLayers().get("trapEasy");
		}	
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
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
			PolygonShape shapeT = new PolygonShape();
			shapeT.setAsBox(20 / PPM, 8 / PPM);
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shapeT;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			Body body = getWorld().createBody(bdef);
			body.createFixture(fdef).setUserData("trap");
			Trap t = new Trap(body);
			trap.add(t);
			body.setUserData(t);
			shapeT.dispose();
		}
	}	
	
	private void createMovingPlatform()
	{
		movingPlatform = new MovingPlatform[10];
		platformNr = 0;
		MapLayer ml = tileMap.getLayers().get("movingPlatform");
		if(ml == null) return;
		for(MapObject mo : ml.getObjects()){
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.KinematicBody;

			if(mo instanceof RectangleMapObject){
				float x =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				float y =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				bdef.position.set(x, y);
				platformSpawnX[platformNr] = x;
				platformSpawnY[platformNr] = y;			
				platformDirectionX[platformNr] = 1;
				platformDirectionY[platformNr] = 0;
			}
			else if(mo instanceof EllipseMapObject){
				float x =  ((EllipseMapObject) mo).getEllipse().x / PPM;
				float y =  ((EllipseMapObject) mo).getEllipse().y / PPM;
				bdef.position.set(x, y);
				platformSpawnX[platformNr] = x;
				platformSpawnY[platformNr] = y;	
				platformDirectionX[platformNr] = 0;
				platformDirectionY[platformNr] = 1;
			}
			Body body = getWorld().createBody(bdef);
			
			PolygonShape shapeT = new PolygonShape();
			FixtureDef fdef = new FixtureDef();
			//fdef.density = 100000000;
			//fdef.friction = 1000;
			
			// paltform fixture
			shapeT.setAsBox(96 / PPM, 32 / PPM);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_MOVINGPLATFORM;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BALL | B2DVars.BIT_ENEMY | B2DVars.BIT_COLL;
			body.createFixture(fdef).setUserData("MovingPlatform");
			
			//BodyDef bdef2 = new BodyDef();
			//bdef.type = BodyType.DynamicBody;
			//Body body2 = world.createBody(bdef2);
			
			//paltform right hand sensor
			shapeT.setAsBox(5 / PPM, 5 / PPM, new Vector2(96 / PPM, 0 / PPM), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_MOVINGPLATFORM;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("movingPlatformRightHand"+platformNr);
			
			//platform Left hand sensor
			shapeT.setAsBox(5 / PPM, 5 / PPM, new Vector2(-96 / PPM, 0 / PPM), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_MOVINGPLATFORM;
			fdef.filter.maskBits = B2DVars.BIT_COLL;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("movingPlatformLeftHand"+platformNr);
			
			//platform top sensor
			shapeT.setAsBox(85 / PPM, 4 / PPM, new Vector2(0 / PPM, 32 / PPM), 0);
			fdef.shape = shapeT;
			fdef.filter.categoryBits = B2DVars.BIT_MOVINGPLATFORM;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			fdef.isSensor = true;
			body.createFixture(fdef).setUserData("movingPlatformTopSensor"+platformNr);
			
			
			
			movingPlatform[platformNr] = new MovingPlatform(body);
			body.setUserData(movingPlatform[platformNr]);
			shapeT.dispose();
			
			if(platformDirectionChanger)
			{
			platformMovingRightLeft[platformNr] = 1;
			platformDirectionChanger = false;
			}
			else
			{
				platformMovingRightLeft[platformNr] = 2;
				platformDirectionChanger = true;
			}
			
			platformNr++;
		}	
	}	
	
	public void startTheMap()
	{
		//PlayerData.health = PlayerData.healthMax;
		player.data.numFairyCage = 0;
		player.data.numFairyDust = 0;
		MyContactListener.playerAtEnd = false;
		Play.isPlayerTeleporting = false;
		getCam().zoom = 1;	
	}
	
	public static void resetSlowCam()
	{
		slowCamera = true;
		slowCameraNumber = 0;
		lerp = 0.05f;
	}
	
	
	
	
	private void createFireArrowSpawn()
	{
		
		MapLayer ml = tileMap.getLayers().get("fireArrowSpawn");
		if(ml == null) return;
		fireArrowSpawnNumber = 0;
		for(MapObject mo : ml.getObjects())
		{
			fireArrowSpawnNumber++;
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			if(mo instanceof RectangleMapObject){
				fireArrowSpawnX[fireArrowSpawnNumber] =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				fireArrowSpawnY[fireArrowSpawnNumber] =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				fireArrowSpawnDirection[fireArrowSpawnNumber] = 1;
			}
			else if(mo instanceof EllipseMapObject){
				fireArrowSpawnX[fireArrowSpawnNumber] = ((EllipseMapObject) mo).getEllipse().x / PPM;
				fireArrowSpawnY[fireArrowSpawnNumber] = ((EllipseMapObject) mo).getEllipse().y / PPM;
				fireArrowSpawnDirection[fireArrowSpawnNumber] = -1;
			}
		}			
	}
	
	
	private void createFireArrow(){
		
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		//create player
		FASpawnSlotX[fireArrowNumber] = 3 + (fireArrowNumber * 0.1f);
		FASpawnSlotY[fireArrowNumber] = -100;
				
		bdef.position.set(FASpawnSlotX[fireArrowNumber] , FASpawnSlotY[fireArrowNumber]);
		
		bdef.type = BodyType.DynamicBody;
		Body body = getWorld().createBody(bdef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(10 / PPM);
				
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_MISSILE; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("fireArrow");
				
		//create collision sensor
		shape.setRadius(20 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_MISSILE;
		fdef.filter.maskBits = B2DVars.BIT_COLL;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("fireArrowHitWall"+fireArrowNumber);
				
		//create player sensor
		shape.setRadius(12 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_MISSILE;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("fireArrowHitPlayer"+fireArrowNumber);
				
		fireArrow[fireArrowNumber] = new FireArrow(body);
		body.setUserData(fireArrow[fireArrowNumber]);
				
		fireArrow[fireArrowNumber].getBody().setGravityScale(0f);

		shape.dispose();
		
		fireArrowInitialised[fireArrowNumber] = false;
		
		fireArrowNumber ++;
	}	

	
	public void InitialiseFireArrow()
	{
		fireArrow[fireArrowNumberInitialised].getBody().setTransform(fireArrowSpawnX[fireArrowSpawnLocationNumber], fireArrowSpawnY[fireArrowSpawnLocationNumber], 0);
		fireArrowInitialised[fireArrowNumberInitialised] = true;
		System.out.println(fireArrowNumberInitialised + " " + fireArrowInitialised[fireArrowNumberInitialised]);
		fireArrowNumberInitialised++;
		if(fireArrowNumberInitialised == fireArrowNumber) fireArrowNumberInitialised = 1;
	}

	public static World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public static void drawLetter(SpriteBatch sb, String s, float x, float y, float ls) 
	{
		if( s == null) return;
		Texture texLetters = Game.res.getTexture("Letters");
		letters = new TextureRegion[100];
		for(int i = 0; i < 29; i ++)
		{
			letters[i] = new TextureRegion(texLetters, i * 18, 0, 18, 24);
		}
		for(int i = 0; i < 29; i ++)
		{
			letters[i+29] = new TextureRegion(texLetters, i * 18, 24, 18, 24);
		}
		for(int i = 0; i < 29; i ++)
		{
			letters[i+58] = new TextureRegion(texLetters, i * 18, 48, 18, 24);
		}
		
		float yC = y;
		float xC = x;
		float letterScale = ls;
		int ii = 0;
		
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i); 
			if(c == 'A') c = 0;
			else if(c == 'B') c = 1;
			else if(c == 'C') c = 2;
			else if(c == 'D') c = 3;
			else if(c == 'E') c = 4;
			else if(c == 'F') c = 5;
			else if(c == 'G') c = 6;
			else if(c == 'H') c = 7;
			else if(c == 'I') c = 8;
			else if(c == 'J') c = 9;
			else if(c == 'K') c = 10;
			else if(c == 'L') c = 11;
			else if(c == 'M') c = 12;
			else if(c == 'N') c = 13;
			else if(c == 'O') c = 14;
			else if(c == 'P') c = 15;
			else if(c == 'Q') c = 16;
			else if(c == 'R') c = 17;
			else if(c == 'S') c = 18;
			else if(c == 'T') c = 19;
			else if(c == 'U') c = 20;
			else if(c == 'V') c = 21;
			else if(c == 'W') c = 22;
			else if(c == 'X') c = 23;
			else if(c == 'Y') c = 24;
			else if(c == 'Z') c = 25;
			else if(c == '.') c = 26;
			else if(c == ',') c = 27;
			else if(c == ' ') c = 28;
			else if(c == 'a') c = 29;
			else if(c == 'b') c = 30;
			else if(c == 'c') c = 31;
			else if(c == 'd') c = 32;
			else if(c == 'e') c = 33;
			else if(c == 'f') c = 34;
			else if(c == 'g') c = 35;
			else if(c == 'h') c = 36;
			else if(c == 'i') c = 37;
			else if(c == 'j') c = 38;
			else if(c == 'k') c = 39;
			else if(c == 'l') c = 40;
			else if(c == 'm') c = 41;
			else if(c == 'n') c = 42;
			else if(c == 'o') c = 43;
			else if(c == 'p') c = 44;
			else if(c == 'q') c = 45;
			else if(c == 'r') c = 46;
			else if(c == 's') c = 47;
			else if(c == 't') c = 48;
			else if(c == 'u') c = 49;
			else if(c == 'v') c = 50;
			else if(c == 'w') c = 51;
			else if(c == 'x') c = 52;
			else if(c == 'y') c = 53;
			else if(c == 'z') c = 54;
			else if(c == '?') c = 55;
			else if(c == '!') c = 56;
			else if(c == '0') c = 58;
			else if(c == '1') c = 59;
			else if(c == '2') c = 60;
			else if(c == '3') c = 61;
			else if(c == '4') c = 62;
			else if(c == '5') c = 63;
			else if(c == '6') c = 64;
			else if(c == '7') c = 65;
			else if(c == '8') c = 66;
			else if(c == '9') c = 67;
			else if(c == ':') c = 68;
			else if(c == ';') c = 69;
			else if(c == '/') c = 70;
			else if(c == '(') c = 71;
			else if(c == ')') c = 72;
			else if(c == '%') c = 73;
			else if(c == '+') c = 74;
			else if(c == '-') c = 75;
			else if(c == '~') 
			{
				yC -= 24;
				ii = 0;
				continue;
			}
			else continue;
			sb.draw(letters[c], xC + (ii * 18 * letterScale), yC, 18 * letterScale, 24 * letterScale);
			++ii;
		}
	}
	
	public static void timeMadeLookNice(SpriteBatch sb, int seconds, int x, int y, float ls)
	{
		int secTime = 0;
		int minTime = 0;
		int hourTime = 0;
		
		secTime = seconds;
		while(secTime > 59)
		{
			minTime ++;
			secTime -= 60;
		}
		while(minTime > 59)
		{
			hourTime ++;
			minTime -= 60;
		}
		
		if(minTime == 0 && hourTime == 0) 
		{
			if(secTime < 10)drawLetter(sb, "00:00:0" + secTime, x, y, ls);
			else drawLetter(sb, "00:00:" + secTime, x, y, ls);
		}
		else if(hourTime == 0)
		{
			if(minTime < 10)
			{
				if(secTime < 10)drawLetter(sb, "00:0" + minTime + ":0" + secTime, x, y, ls);
				else drawLetter(sb, "00:0" + minTime + ":" + secTime, x, y, ls);
			}
			else 
			{
				if(secTime < 10)drawLetter(sb, "00:" + minTime + ":0" + secTime, x, y, ls);
				else drawLetter(sb, "00:" + minTime + ":" + secTime, x, y, ls);
			}
		}
		else drawLetter(sb, hourTime + ":" + minTime + ":" + secTime, x, y, ls);
	}
}