package com.bj.pigport.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.bj.pigport.handlers.Animation;
import com.bj.pigport.handlers.B2DVars;

public class B2DSprite {

	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	protected boolean stretced = false;
	protected float stretchX;
	protected float stretchY;
	protected boolean rotated = false;
	protected float rotation = 0;
	
	public B2DSprite(Body body){
		this.body = body;
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void setAnimationRotated(TextureRegion[] reg, float delay, float rot){
		rotated = true;
		rotation = rot;
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void setAnimationStreched(TextureRegion[] reg, float delay, float widthStretched, float heightStretched){
		stretced = true;
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
		stretchX = widthStretched;
		stretchY = heightStretched;
	}
	
	public void update(float dt){
		animation.update(dt);
	}
	
	
	public void render(SpriteBatch sb){
		sb.begin();
		
		if(stretced)
		{			
			sb.draw(animation.getFrame(),
					body.getPosition().x * B2DVars.PPM - (stretchX / 2),
					body.getPosition().y * B2DVars.PPM - (stretchY / 2),
					stretchX,
					stretchY);
		}
		else if(rotated)
		{
			sb.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - height / 2, body.getPosition().y * B2DVars.PPM - width / 2,
					height/2, width/2, height, width, 1, 1, rotation + 90, true);
		}
		else
		{
			sb.draw(animation.getFrame(),
					body.getPosition().x * B2DVars.PPM - width / 2,
					body.getPosition().y * B2DVars.PPM - height / 2);
		}
		
		
		sb.end();
	}
	
	public Body getBody(){
		return body;
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHieght(){
		return height;
	}
}
