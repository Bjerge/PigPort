package com.bj.pigport.entities.player.abilities;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.handlers.MyInput;

public class Ball extends B2DSprite{

	public boolean shootNoTp = false;
	
	public int sensingBallRightLeft;
	public int sensingBallTopDown;
	public float ballCoordsX;
	public float ballCoordsY;
	
	public int collThinDisableTimer = 0;
	
	private double PC1;
	private double PC2;
	private double vectorMyAss;
	private float forceX;
	private float forceY;
	private float PCLength;
	public float startVelocityX;
	public float startVelocityY;
	private float gravity = (float) -9.81;
	public float modifier = (float) 6.05;
	public float PCLengthAim;
	private float startVelocityModifier;
	public float xBallCoordinate;
	public float yBallCoordinate;
	
	public Ball(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("ball");
		TextureRegion[] sprites = TextureRegion.split(tex, 25, 25)[0];
		
		setAnimation(sprites, 1 / 12f);
	}
	
	
	public void BallControls()
	{
		if(collThinDisableTimer == 1)
		{
			collThinManager(0);
			collThinDisableTimer = 2;
		}
		if(collThinDisableTimer > 0)
		{
			collThinDisableTimer++;
			if(collThinDisableTimer > 15)
			{
				collThinManager(1);
				collThinDisableTimer = 0;
			}
		}
		
		
		
		if(Play.isPlayerTeleporting && Play.tpNow ||
				Play.isPlayerTeleporting && this.getBody().getLinearVelocity().y == 0.0f)
		{
			Play.playerTeleportX = this.getPosition().x;
			Play.playerTeleportY = this.getPosition().y;	
			Play.player.data.ballEffect.x = this.getPosition().x;
			Play.player.data.ballEffect.y = this.getPosition().y;
			
			
			Play.tpNow = false;
			this.getBody().setGravityScale(0);
			this.getBody().setLinearVelocity(0, 0);
			this.getBody().setTransform(1000, 1000, 0);
			
			Play.resetSlowCam();
			
			//Play.playerTeleportX = ballCoordsX;
			//Play.playerTeleportY = ballCoordsY;
			
			//Check if there is collision on right (1) or left (2)
			if(sensingBallRightLeft == 1){
				Play.playerTeleportX = Play.playerTeleportX - (30 / PPM);
			}
			else if(sensingBallRightLeft == 2){
				Play.playerTeleportX = Play.playerTeleportX + (30 / PPM);
			}
			//Check if there is collision on top (1) or down (2)
			if(sensingBallTopDown == 1){
				Play.playerTeleportY = Play.playerTeleportY - (50 / PPM);
			}
			else if(sensingBallTopDown == 2){
				Play.playerTeleportY = Play.playerTeleportY + (50 / PPM);
			}
			
			Play.isPlayerTeleporting = false;
			Play.timeStop = false;
			Play.playerJumpVelocityX = 0;
			if(this.shootNoTp)
			{
				
			}
			else
			{
				//Play.player.getBody().setLinearVelocity(0, 0);
				//Play.player.getBody().setTransform(Play.playerTeleportX, Play.playerTeleportY, 0); 
			}
			Play.player.data.ballEffect.BallEffectTimer = 20;
		}
		
		if(MyInput.isReleased() && Play.isPlayerTeleporting) this.shootNoTp = true;
		
		if(MyInput.isReleased() && Play.isPlayerTeleporting != true && PCLengthAim > 80){
			getDrawBall(0);
			Play.player.data.numFairyDust--;
			Play.drawBall = true;
			Play.isPlayerTeleporting = true;
			Play.tpNow = false;
			this.shootNoTp = false;
			Play.resetSlowCam();
			this.getBody().setGravityScale(1);
			this.getBody().setTransform(Play.player.getPosition(), 0);
			this.getBody().applyForceToCenter(startVelocityX * modifier, startVelocityY * modifier, true);
		}
		
	}
	
	public void collThinManager(int i)
	{
		Filter filter = this.getBody().getFixtureList().first().getFilterData();
		short bits = filter.maskBits;
		
		if(i == 0)bits = B2DVars.BIT_COLL;
		if(i == 1)bits = B2DVars.BIT_COLLTHIN;
		
		filter.maskBits = bits;
		this.getBody().getFixtureList().get(0).setFilterData(filter);
	}
	
	public static void createBall(World world){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		//create ball
		bdef.position.set(1000, 1000);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		CircleShape shape = new CircleShape();
		PolygonShape shapeB = new PolygonShape();
		
		shapeB.setAsBox(12 / PPM, 2 / PPM, new Vector2(0, -12 / PPM), 0);
		fdef.shape = shapeB;
		fdef.filter.categoryBits = B2DVars.BIT_BALL; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLLTHIN;
		fdef.isSensor = false;
		body.createFixture(fdef).setUserData("collThin");
		
		shapeB.setAsBox(12 / PPM, 2 / PPM, new Vector2(0, 12 / PPM), 0);
		fdef.shape = shapeB;
		fdef.filter.categoryBits = B2DVars.BIT_BALL; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLLTHIN;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ballCollThinDisabler");
		
		shape.setRadius(12 / PPM);
		fdef.shape = shape;
		//fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = false;
		body.createFixture(fdef).setUserData("ball");			
		//create foot sensor
		//cshape.setRadius(8 / PPM);
		shape.setRadius(13 / PPM);
		fdef.shape = shape;
		//fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("footBall");
		//create right sensor
		shape.setRadius(8 / PPM);
		shape.setPosition(new Vector2(32 / PPM, 0));
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ballSensorRight");
		//create left sensor
		shape.setRadius(8 / PPM);
		shape.setPosition(new Vector2(-32 / PPM, 0));
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ballSensorLeft");
		//create right sensor
		shape.setRadius(8 / PPM);
		shape.setPosition(new Vector2(0, 32 / PPM));
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ballSensorTop");
		//create left sensor
		shape.setRadius(8 / PPM);
		shape.setPosition(new Vector2(0, -32 / PPM));
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_COLL | B2DVars.BIT_MOVINGPLATFORM;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ballSensorDown");
		Play.player.data.ball = new Ball(body);
		body.setUserData(Play.player.data.ball);
		
		body.setGravityScale(0);		
		shape.dispose();
		shapeB.dispose();
	}
	
	//public static void getDrawBall(float t, float camPosX, float camPosY)
	public void getDrawBall(float t)
	{
		
		PC1 = Gdx.input.getX()-((Game.V_WIDTH * Game.SCALE)/2);

		if(PC1 > 0)PC2 = (Gdx.input.getY() * (-1))+((Game.V_HEIGHT * Game.SCALE)/2) * (1+(PC1/1000));
		if(PC1 < 0)PC2 = (Gdx.input.getY() * (-1))+((Game.V_HEIGHT * Game.SCALE)/2) * (1+((PC1*-1)/1000));
		
		/*
		PC1 = (Gdx.input.getX()+(camPosX -(Game.V_WIDTH * Game.SCALE)/2)) - (PlayerData.player.getPosition().x*100);
		if(PC1 > 0)PC2 = ((((Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY()) + (camPosY - (Game.V_HEIGHT * Game.SCALE / 2))))-(PlayerData.player.getPosition().y*100) * (1+(PC1/1000));
		if(PC1 < 0)PC2 = ((((Game.V_HEIGHT * Game.SCALE) - Gdx.input.getY()) + (camPosY - (Game.V_HEIGHT * Game.SCALE / 2))))-(PlayerData.player.getPosition().y*100) * (1+((PC1*-1)/1000));
		*/
		
		vectorMyAss = Math.acos((((PC1*1)+(PC2*0))/(Math.sqrt(Math.pow(PC1, 2.0)+ Math.pow(PC2, 2.0)))));
		
		forceX = (float) Math.cos(vectorMyAss);
		forceY = (float) Math.sin(vectorMyAss);
		if(PC2<0) forceY = forceY*(-1);
		PCLength = (float) (Math.sqrt(Math.pow(PC1, 2.0)+ Math.pow(PC2, 2.0)));
		PCLengthAim = PCLength;
		if(PCLength>200) PCLength = 200;
		else if(PCLength<=40) 
		{
			PCLength = 0;
			t = 0;
		}
		
		startVelocityX = (((forceX*1.5f)*(PCLength)) / modifier);
		if(startVelocityX > 0) startVelocityX += startVelocityModifier;
		if(startVelocityX < 0) startVelocityX -= startVelocityModifier;

		startVelocityY = (((forceY*2.5f)*(PCLength)) / modifier) + startVelocityModifier;
		xBallCoordinate = (startVelocityX * t)  + 628;
		yBallCoordinate = (0.5f * gravity * t * t + startVelocityY * t) + 330;
		//xBallCoordinate = (startVelocityX * t)  + (PlayerData.player.getPosition().x*100);
		//yBallCoordinate = (0.5f * gravity * t * t + startVelocityY * t) + (PlayerData.player.getPosition().y*100);
	}
}