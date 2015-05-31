package com.bj.pigport.entities.player.abilities;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;
import com.bj.pigport.entities.B2DSprite;
import com.bj.pigport.entities.player.Player;
import com.bj.pigport.entities.player.PlayerData;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.handlers.MyContactListener;
import com.bj.pigport.handlers.MyInput;

public class BallEffect extends B2DSprite{
	
	public static int BallEffectTimer = 0;
	
	public BallEffect(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("ball");
		TextureRegion[] sprites = TextureRegion.split(tex, 25, 25)[0];
		
		setAnimation(sprites, 1 / 12f);
	}
	
	public static void ballEffectController()
	{
		if(BallEffectTimer == 10) PlayerData.ballEffect.getBody().setTransform(PlayerData.player.getBody().getPosition(), 0);
		
		if(BallEffectTimer == 1) PlayerData.ballEffect.getBody().setTransform(1000, 1000, 0);
		
		
		if(BallEffectTimer > 0) BallEffectTimer--;
	}
	
	public static void createBallEffect(World world){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		//create ballEffect
		bdef.position.set(1000, 1000);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(200 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALLEFFECT; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_ENEMY;
		body.createFixture(fdef).setUserData("ballEffect");					
		
		PlayerData.ballEffect = new BallEffect(body);
		body.setUserData(PlayerData.ballEffect);
		
		body.setGravityScale(0);		
		shape.dispose();
	}	
}