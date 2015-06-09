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
	
	public int BallEffectTimer = 0;
	
	public float x = 0;
	public float y = 0;
	
	public BallEffect(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("fireBallEffect");
		TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth(), tex.getHeight())[0];
		
		setAnimation(sprites, 1 / 12f);
	}
	
	public void ballEffectController()
	{
		if(BallEffectTimer == 20) Play.player.data.ballEffect.getBody().setTransform(x,y, 0);
		if(BallEffectTimer == 1 && Play.player.data.ball.shootNoTp != true)
		{
			Play.player.getBody().setLinearVelocity(0, 0);
			Play.player.getBody().setTransform(Play.playerTeleportX, Play.playerTeleportY, 0); 
		}
		this.getBody().setLinearVelocity(0.001f, 0);
		
		if(BallEffectTimer == 1) Play.player.data.ballEffect.getBody().setTransform(1000, 1000, 0);
		
		
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
		
		shape.setRadius(150 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_BALL; //| B2DVars.BIT_BALL;
		fdef.filter.maskBits = B2DVars.BIT_ENEMY;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ballEffect");	
		
		Play.player.data.ballEffect = new BallEffect(body);
		body.setUserData(Play.player.data.ballEffect);
		
		body.setGravityScale(0);		
		shape.dispose();
	}	
}