package com.bj.pigport.entities.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.bj.pigport.main.Game;
import com.bj.pigport.entities.enemy.EnemyMissile;

public class EnemyData {
	protected boolean active = true;
	protected int health;
	protected int maxHealth;
	protected int damage;
	protected int type;
	protected String name;
	protected int level;	
	protected int playerXP;
	protected Texture texEnemy;
	protected float texStretch = 1;
	
	protected Texture texWeapon;
	protected float speed;
	protected int enemyNumber;
	protected float renderPosition;
	
	protected boolean ground = false;
	protected boolean ranged = false;
	protected boolean giant = false;
	public boolean missile = false;
	
	
	public EnemyMissile enemyMissiles [];
	public int missilesNumber = 0;
	public int missilesNextToFire = 0;
	
	protected int missileCDTimer = 0;
	protected int missileShootTimer = 0;
	public boolean enemyMissileReset = false;
	
	protected boolean flying = false;
	private int flyingState = 0;
	private float flyPosX = 0;
	private float flyPosY = 0;
	private int flyRotation = 0;
	private int flyChargeTimer = 0;
	
	protected boolean [] boss = {false, false, false, false, false, false, false, false, false, false};
		
	protected float SpawnX = 0;
	protected float SpawnY = 0;
	public int enemyWalkRightLeft = 0;
	
	protected int enemyType;
	
	protected float playerInRangeRadiusModifier = 0; 
	
	public Texture enemyHealth = Game.res.getTexture("PlayerHealth");;
	public Texture enemyHealthLeft = Game.res.getTexture("PlayerHealthLeft");
	
	public boolean hitByBallEffect = false;
	public int hitByBallEffectTimer = 0;
	
	public boolean hitByMelee = false;
	public int hitByMeleeTimer = 0;
	
	protected boolean frozen = false;
	protected int frozenTimer = 0;
	
	protected boolean fire = false;
	protected int fireTimer = 0;
	
	protected boolean wind = false;
	protected int windTimer = 0;
	
	
	//Movement
	protected int lockDirection = 0;
	protected int lockTimer = 0;
	
	
	
	public EnemyData() {
		
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPlayerXP() {
		return playerXP;
	}

	public void setPlayerXP(int playerXP) {
		this.playerXP = playerXP;
	}

	public Texture getTexEnemy() {
		return texEnemy;
	}

	public void setTexEnemy(Texture texEnemy) {
		this.texEnemy = texEnemy;
	}

	public Texture getTexWeapon() {
		return texWeapon;
	}

	public void setTexWeapon(Texture texWeapon) {
		this.texWeapon = texWeapon;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getEnemyNumber() {
		return enemyNumber;
	}

	public void setEnemyNumber(int enemyNumber) {
		this.enemyNumber = enemyNumber;
	}

	public float getRenderPosition() {
		return renderPosition;
	}

	public void setRenderPosition(float renderPosition) {
		this.renderPosition = renderPosition;
	}
	
	public boolean isRanged() {
		return ranged;
	}

	public void setRanged(boolean ranged) {
		this.ranged = ranged;
	}

	public boolean isGiant() {
		return giant;
	}

	public void setGiant(boolean giant) {
		this.giant = giant;
	}
	
	public boolean isMissile() {
		return missile;
	}

	public void setMissile(boolean missile) {
		this.missile = missile;
	}
	
	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public int getEnemyType() {
		return enemyType;
	}

	public void setEnemyType(int enemyType) {
		this.enemyType = enemyType;
	}

	public float getPlayerInRangeRadiusModifier() {
		return playerInRangeRadiusModifier;
	}

	public void setPlayerInRangeRadiusModifier(float playerInRangeRadiusModifier) {
		this.playerInRangeRadiusModifier = playerInRangeRadiusModifier;
	}

	public int getFlyingState() {
		return flyingState;
	}

	public void setFlyingState(int flyingState) {
		this.flyingState = flyingState;
	}

	public float getFlyPosX() {
		return flyPosX;
	}

	public void setFlyPosX(float flyPosX) {
		this.flyPosX = flyPosX;
	}

	public float getFlyPosY() {
		return flyPosY;
	}

	public void setFlyPosY(float flyPosY) {
		this.flyPosY = flyPosY;
	}

	public int getFlyRotation() {
		return flyRotation;
	}

	public void setFlyRotation(int flyRotation) {
		this.flyRotation = flyRotation;
	}

	public int getFlyChargeTimer() {
		return flyChargeTimer;
	}

	public void setFlyChargeTimer(int flyChargeTimer) {
		this.flyChargeTimer = flyChargeTimer;
	}
}

