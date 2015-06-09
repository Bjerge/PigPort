package com.bj.pigport.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.bj.pigport.entities.mapobjects.Destructables;
import com.bj.pigport.entities.mapobjects.Loot;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.entities.player.abilities.Ball;
import com.bj.pigport.states.Play;

public class MyContactListener implements ContactListener {
	private static int numFootContacts;
	public boolean footContactsPLF2 = false;
	public static int numFootContactsBall;
	
	private Array<Body> fairyDustToRemove;
	private Array<Body> fairyCageToRemove;
	private Array<Body> enemyToRemove;
	private Array<Body> fireArrowToRemove1;
	private Array<Body> fireArrowToRemove2;
	private Array<Body> fireArrowToRemove3;
	
	
	
	public static boolean playerAtEnd = false;
	private static int playerTouchingWallRight;
	private static int playerTouchingWallLeft;
	private int playerTouchingPLFRight;
	private int playerTouchingPLFLeft;
	//public int enemyNumberHit = 0;
	//public int enemyNumberAttacked = 100;
	public int playerOnPlatformNr;
	
	public int fireArrowNumberToDelete1 = 1;
	public int fireArrowNumberToDelete2 = 1;
	public int fireArrowNumberToDelete3 = 1;
	
	//public static boolean enemyBeingHitByPlayerAttack = false;
	//public static boolean[] enemyBeingHitByPlayerAttackBoolean = new boolean[200];
	//public static boolean[] enemyBeenHitByPlayerAttackAlready = new boolean[200];
	
	//public static Ball[] ball = new Ball[300];
	
	
	public MyContactListener() {
		super();
		numFootContacts = 0;
		playerTouchingWallRight = 0;
		playerTouchingWallLeft = 0;
		
		
		
		fairyDustToRemove = new Array<Body>();
		fairyCageToRemove = new Array<Body>();
		enemyToRemove = new Array<Body>();
		fireArrowToRemove1 = new Array<Body>();
		fireArrowToRemove2 = new Array<Body>();
		fireArrowToRemove3 = new Array<Body>();
		
	}
	
	//called when two fixtures start to collide
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		if(fa == null || fb == null) return;
		
		
		if(fa.getUserData() != null && fa.getUserData().equals("rightSensor")){
			playerTouchingWallRight++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("rightSensor")){
			playerTouchingWallRight++;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("leftSensor")){
			playerTouchingWallLeft++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("leftSensor")){
			playerTouchingWallLeft++;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("rightSensorPLF")){
			playerTouchingPLFRight++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("rightSensorPLF")){
			playerTouchingPLFRight++;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("leftSensorPLF")){
			playerTouchingPLFLeft++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("leftSensorPLF")){
			playerTouchingPLFLeft++;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("collThinDisabler")){
			Play.player.data.collThinDisableTimer = 1;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("collThinDisabler")){
			Play.player.data.collThinDisableTimer = 1;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("ballCollThinDisabler")){
			Play.player.data.ball.collThinDisableTimer = 1;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ballCollThinDisabler")){
			Play.player.data.ball.collThinDisableTimer = 1;
		}
		
		
		if(Play.platformNr > 0)
		{
			for(int i = 0; i < Play.platformNr; i++)
			{	
				if(fa.getUserData() != null && fa.getUserData().equals("movingPlatformTopSensor"+i)){
					footContactsPLF2 = true;
					playerOnPlatformNr = i;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("movingPlatformTopSensor"+i)){
					footContactsPLF2 = true;
					playerOnPlatformNr = i;
				}
			}
		}
		
		if(Destructables.number > 0)
		{
			for(int i = 0; i < Destructables.number; i++)
			{	
				if(fa.getUserData() != null && fa.getUserData().equals("destructables"+i)){
					Destructables.destructables[i].playerMeleeContact = true;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("destructables"+i)){
					Destructables.destructables[i].playerMeleeContact = true;
				}
			}
		}
	
		
		if(fa.getUserData() != null && fa.getUserData().equals("footBall")){
			Play.tpNow = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("footBall")){
			Play.tpNow = true;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("dust")){
			fairyDustToRemove.add(fa.getBody());
		}
		if(fb.getUserData() != null && fb.getUserData().equals("dust")){
			fairyDustToRemove.add(fb.getBody());
		}
		if(fa.getUserData() != null && fa.getUserData().equals("cage")){
			fairyCageToRemove.add(fa.getBody());
		}
		if(fb.getUserData() != null && fb.getUserData().equals("cage")){
			fairyCageToRemove.add(fb.getBody());
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("playerHitEnemy")){
			Play.player.data.damageContact = true;
			Play.player.data.knockBackContact = true;
			Play.player.data.knockBackSource = (fb.getBody().getPosition());		
		}
		if(fb.getUserData() != null && fb.getUserData().equals("playerHitEnemy")){
			Play.player.data.damageContact = true;
			Play.player.data.knockBackContact = true;
			Play.player.data.knockBackSource = (fa.getBody().getPosition());
		}
		if(fa.getUserData() != null && fa.getUserData().equals("playerHitMissile")){
			Play.player.data.damageContact = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("playerHitMissile")){
			Play.player.data.damageContact = true;
		}
		
		if(Play.enemyNumber > 0)
		{
			for(int i = 0; i < Play.enemyNumber; i++)
			{				
				if(fa.getUserData() != null && fa.getUserData().equals("enemyHitByPlayerMelee"+i)){
					Play.enemy[i].data.hitByMelee = true;
					//System.out.println(Play.enemy[i].data.name + " number " + i + " was hit by player melee");
				}
				if(fb.getUserData() != null && fb.getUserData().equals("enemyHitByPlayerMelee"+i)){
					Play.enemy[i].data.hitByMelee = true;
					//System.out.println(Play.enemy[i].data.name + " number " + i + " was hit by player melee");
				}
				if(fa.getUserData() != null && fa.getUserData().equals("enemyHitByBallEffect"+i)){
					Play.enemy[i].data.hitByBallEffect = true;
					//System.out.println(Play.enemy[i].data.name + " was hit by player ball");
				}
				if(fb.getUserData() != null && fb.getUserData().equals("enemyHitByBallEffect"+i)){
					Play.enemy[i].data.hitByBallEffect = true;
					//System.out.println(Play.enemy[i].data.name + " was hit by player ball");
				}
				
				if(fa.getUserData() != null && fa.getUserData().equals("enemyRightHand"+i)){
					Play.enemy[i].data.enemyWalkRightLeft = -1;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("enemyRightHand"+i)){
					Play.enemy[i].data.enemyWalkRightLeft = -1;
				}
				if(fa.getUserData() != null && fa.getUserData().equals("enemyLeftHand"+i)){
					Play.enemy[i].data.enemyWalkRightLeft = 1;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("enemyLeftHand"+i)){
					Play.enemy[i].data.enemyWalkRightLeft = 1;
				}
				
				for(int ii = 0; ii < Play.enemy[i].data.missilesNumber; ii++)
				{
					if(fa.getUserData() != null && fa.getUserData().equals("enemyMissileColission"+i+"."+ii)){
						Play.enemy[i].data.enemyMissileReset[ii] = true;
					}
					if(fb.getUserData() != null && fb.getUserData().equals("enemyMissileColission"+i+"."+ii)){
						Play.enemy[i].data.enemyMissileReset[ii] = true;
					}
				}
			}
		}
		
		if(Play.fireArrowNumber > 1)
		{
			for(int i = 0; i < Play.fireArrowNumber; i++)
			{	
				
				if(fa.getUserData() != null && fa.getUserData().equals("fireArrowHitWall"+i)){
					fireArrowNumberToDelete1 = i;
					fireArrowToRemove1.add(fa.getBody());
					Play.fireArrowInitialised[i] = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("fireArrowHitWall"+i)){
					fireArrowNumberToDelete1 = i;
					fireArrowToRemove1.add(fb.getBody());
					Play.fireArrowInitialised[i] = false;
				}
				
				if(fa.getUserData() != null && fa.getUserData().equals("fireArrowHitPlayer"+i)){
					fireArrowNumberToDelete1 = i;
					fireArrowToRemove1.add(fa.getBody());
					System.out.println("FA 1: " + i);
					Play.fireArrowInitialised[i] = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("fireArrowHitPlayer"+i)){
					fireArrowNumberToDelete1 = i;
					fireArrowToRemove1.add(fb.getBody());
					System.out.println("FB 1: " + i);
					Play.fireArrowInitialised[i] = false;
				}
			}
		}
		
		//System.out.println(Play.platformNr + " outside");
		if(Play.platformNr > 0)
		{
			//System.out.println(Play.platformNr + " inside");
			for(int i = 0; i < Play.platformNr; i++)
			{	
				//System.out.println(i);
				
				if(fa.getUserData() != null && fa.getUserData().equals("movingPlatformRightHand"+i))
				{
					Play.platformMovingRightLeft[i] = 2;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("movingPlatformRightHand"+i))
				{
					Play.platformMovingRightLeft[i] = 2;	
				}
				if(fa.getUserData() != null && fa.getUserData().equals("movingPlatformLeftHand"+i))
				{
					Play.platformMovingRightLeft[i] = 1;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("movingPlatformLeftHand"+i))
				{
					Play.platformMovingRightLeft[i] = 1;
				}
			}
		
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("end")){
			playerAtEnd = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("end")){
			playerAtEnd = true;
		}
		
		
		//Check Ball's Right/Left/Top/Down sensors
		if(fa.getUserData() != null && fa.getUserData().equals("ballSensorRight")){
			Play.player.data.ball.sensingBallRightLeft = 1;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ballSensorRight")){
			Play.player.data.ball.sensingBallRightLeft = 1;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("ballSensorLeft")){
			Play.player.data.ball.sensingBallRightLeft = 2;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ballSensorLeft")){
			Play.player.data.ball.sensingBallRightLeft = 2;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("ballSensorTop")){
			Play.player.data.ball.sensingBallTopDown = 1;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ballSensorTop")){
			Play.player.data.ball.sensingBallTopDown = 1;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("ballSensorDown")){
			Play.player.data.ball.sensingBallTopDown = 2;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ballSensorDown")){
			Play.player.data.ball.sensingBallTopDown = 2;
		}

	}
	
	//called when two fixtures no longer collide
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if(fa.getUserData() != null && fa.getUserData().equals("rightSensor")){
			playerTouchingWallRight--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("rightSensor")){
			playerTouchingWallRight--;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("leftSensor")){
			playerTouchingWallLeft--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("leftSensor")){
			playerTouchingWallLeft--;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("rightSensorPLF")){
			playerTouchingPLFRight--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("rightSensorPLF")){
			playerTouchingPLFRight--;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("leftSensorPLF")){
			playerTouchingPLFLeft--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("leftSensorPLF")){
			playerTouchingPLFLeft--;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("playerHitEnemy")){
			Play.player.data.damageContact = false;
			Play.player.data.knockBackContact = false;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("playerHitEnemy")){
			Play.player.data.damageContact = false;
			Play.player.data.knockBackContact = false;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("playerHitMissile")){
			Play.player.data.damageContact = false;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("playerHitMissile")){
			Play.player.data.damageContact = false;
		}
		
		if(Play.platformNr > 0)
		{
			for(int i = 0; i < Play.platformNr; i++)
			{	
				if(fa.getUserData() != null && fa.getUserData().equals("movingPlatformTopSensor"+i)){
					footContactsPLF2 = false;
					playerOnPlatformNr = i;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("movingPlatformTopSensor"+i)){
					footContactsPLF2 = false;
					playerOnPlatformNr = i;
				}
			}
		}
		
		if(Destructables.number > 0)
		{
			for(int i = 0; i < Destructables.number; i++)
			{	
				if(fa.getUserData() != null && fa.getUserData().equals("destructables"+i)){
					Destructables.destructables[i].playerMeleeContact = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("destructables"+i)){
					Destructables.destructables[i].playerMeleeContact = false;
				}
			}
		}
		
		
		if(Play.enemyNumber > 0)
		{
			for(int i = 0; i < Play.enemyNumber; i++)
			{	
				if(fa.getUserData() != null && fa.getUserData().equals("enemyHitByPlayerMelee"+i)){
					Play.enemy[i].data.hitByMelee = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("enemyHitByPlayerMelee"+i)){
					Play.enemy[i].data.hitByMelee = false;
				}
				if(fa.getUserData() != null && fa.getUserData().equals("enemyHitByBallEffect"+i)){
					Play.enemy[i].data.hitByBallEffect = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("enemyHitByBallEffect"+i)){
					Play.enemy[i].data.hitByBallEffect = false;
				}
			}
		}
		
		
		
		if(fa.getUserData() != null && fa.getUserData().equals("footBall")){
			//numFootContactsBall[Play.ballNumber]--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("footBall")){
			//numFootContactsBall[Play.ballNumber]--;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("end")){
			playerAtEnd = false;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("end")){
			playerAtEnd = false;
		}
	}
	
	public static boolean isPlayerOnGround() {
		return numFootContacts > 0; 
	}
	
	public boolean isBallOnGround() {
		return numFootContactsBall > 0;
	}
	public static boolean isPlayerAtEnd() {
			return playerAtEnd;
	}
	public static boolean isPlayerTouchingWallRight() {
		return playerTouchingWallRight > 0;
	}
	public static boolean isPlayerTouchingWallLeft() {
		return playerTouchingWallLeft > 0;
	}
	
	public boolean isPlayerTouchingPLFRight() {
		return playerTouchingPLFRight > 0;
	}
	public boolean isPlayerTouchingPLFLeft() {
		return playerTouchingPLFLeft > 0;
	}
	
	public Array<Body> getFairyDustToRemove() {return fairyDustToRemove; }
	public Array<Body> getFairyCageToRemove() {return fairyCageToRemove; }
	public Array<Body> getEnemyToRemove() {return enemyToRemove; }
	public Array<Body> getFireArrowToRemove1() {return fireArrowToRemove1; }
	public Array<Body> getFireArrowToRemove2() {return fireArrowToRemove2; }
	public Array<Body> getFireArrowToRemove3() {return fireArrowToRemove3; }
	
	public void preSolve(Contact c, Manifold m) {}
	public void postSolve(Contact c, ContactImpulse ci) {}
	
}
