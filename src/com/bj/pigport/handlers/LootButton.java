package com.bj.pigport.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class LootButton {
	
	private float x;
	private float y;
	private float width;
	private float height;
	private float letterScale;
	
	Vector3 vec;
	private OrthographicCamera cam;
	
	private TextureRegion reg;
	
	private String buttonText;
	
	private boolean clicked;
	private boolean hovered;
	private boolean released;
	
	//public GameButton(TextureRegion regOut, float x, float y, OrthographicCamera cam){
	public LootButton(String string, float x, float y, OrthographicCamera cam, float Ls){	
		
		//String string = "Button";
		
		Texture PlainButtonTex = Game.res.getTexture("lootBackground");
		
		this.reg = new TextureRegion(PlainButtonTex, 0, 0, PlainButtonTex.getWidth(), PlainButtonTex.getHeight());
		
		this.buttonText = string;
		//this.reg = reg;
		this.x = x;
		this.y = y;
		this.cam = cam;
		
		this.letterScale = Ls;
		
		this.width = 120;
		this.height = 14;
		this.vec = new Vector3();
		
	}
	
	public boolean isClicked() { return clicked; }
	public boolean isHovered() { return hovered; }
	public boolean isReleased() { return released; }
	
	public void update(float dt) {
		
		vec.set(((MyInput.x + cam.position.x - (Game.V_WIDTH * Game.SCALE / 2))), 
				(((Game.V_HEIGHT * Game.SCALE / 2) - MyInput.y + cam.position.y)), 0);
		
		if(MyInput.isPressed() &&
			vec.x > x - width / 2 && vec.x < x + (width / 2) &&
			vec.y > y - (height / 2) && vec.y < y + height / 2) {
			clicked = true;
		}
		else {
			clicked = false;
		}
		
		if(vec.x > x - width / 2 && vec.x < x + (width / 2) &&
			vec.y > y - (height / 2) && vec.y < y + height / 2) {
			hovered = true;
		}
		else {
			hovered = false;
		}
		
		if(MyInput.isReleased() &&
				vec.x > x - width / 2 && vec.x < x + (width / 2) &&
				vec.y > y - (height / 2) && vec.y < y + height / 2) {
				released = true;
			}
		else released = false;
		
	}
	
	public void render(SpriteBatch sb) {
		
		sb.begin();
		
		
		sb.draw(reg, x - (width / 2), y - (height / 2), width, height);
		
		Play.drawLetter(sb, buttonText,x - (width / 2) + 10, y - 5, letterScale);		
		
		sb.end();
		
	}
}
