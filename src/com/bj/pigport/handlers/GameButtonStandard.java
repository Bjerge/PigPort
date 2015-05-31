package com.bj.pigport.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.bj.pigport.main.Game;
import com.bj.pigport.states.Play;

public class GameButtonStandard {
	
	private float x;
	private float y;
	private float width;
	private float height;
	private float letterScale;
	
	Vector3 vec;
	private OrthographicCamera cam;
	
	private TextureRegion reg;
	
	private TextureRegion LTcorner;
	private TextureRegion RTcorner;
	private TextureRegion LBcorner;
	private TextureRegion RBcorner;
	private TextureRegion Tline;
	private TextureRegion Bline;
	private TextureRegion Lline;
	private TextureRegion Rline;
	private TextureRegion Center;
	
	private Texture picture;
	private boolean drawPicture = false;
	
	private float sizeModifierX;
	private float sizeModifierY;
	private float Xsize;
	private float Ysize;
	
	private String buttonText;
	
	private boolean clicked;
	private boolean hovered;
	private boolean released;
	
	//public GameButton(TextureRegion regOut, float x, float y, OrthographicCamera cam){
	public GameButtonStandard(String string, float x, float y, OrthographicCamera cam, float sX, float sY,  float Ls, Texture tex){	
		
		//String string = "Button";
		
		Texture PlainButtonTex = Game.res.getTexture("PlainButton1");
		if(tex != null) 
		{
			this.picture = tex;
			drawPicture = true;
		}
		
		this.LTcorner = new TextureRegion(PlainButtonTex, 0, 0, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		this.RTcorner = new TextureRegion(PlainButtonTex, PlainButtonTex.getWidth()/2, 0, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		this.LBcorner = new TextureRegion(PlainButtonTex, 0, PlainButtonTex.getHeight()/2, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		this.RBcorner = new TextureRegion(PlainButtonTex, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		
		this.Tline = new TextureRegion(PlainButtonTex, PlainButtonTex.getWidth()/4, 0, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		this.Bline = new TextureRegion(PlainButtonTex, PlainButtonTex.getWidth()/4, PlainButtonTex.getHeight() - PlainButtonTex.getHeight()/2, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		
		this.Lline = new TextureRegion(PlainButtonTex, 0, PlainButtonTex.getHeight()/4, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		this.Rline = new TextureRegion(PlainButtonTex, PlainButtonTex.getWidth() - PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/4, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		
		this.Center = new TextureRegion(PlainButtonTex, PlainButtonTex.getWidth()/4, PlainButtonTex.getHeight()/4, PlainButtonTex.getWidth()/2, PlainButtonTex.getHeight()/2);
		
		
		this.reg = new TextureRegion(PlainButtonTex, 0, 0, PlainButtonTex.getWidth(), PlainButtonTex.getHeight());
		
		this.buttonText = string;
		//this.reg = reg;
		this.x = x;
		this.y = y;
		this.cam = cam;
		
		this.letterScale = Ls;
		
		this.sizeModifierX = sX;
		this.sizeModifierY = sY;
		
		this.width = reg.getRegionWidth();
		this.height = reg.getRegionHeight();
		this.vec = new Vector3();
		
		this.Xsize = width * (sizeModifierX-1);
		this.Ysize = height * (sizeModifierY-1);
		
	}
	
	public boolean isClicked() { return clicked; }
	public boolean isHovered() { return hovered; }
	public boolean isReleased() { return released; }
	
	public void update(float dt) {
		
		
		vec.set(MyInput.x, MyInput.y, 0);
		if(cam != null){
		cam.unproject(vec);
		}
		
		if(MyInput.isPressed() &&
			vec.x > x - width / 2 && vec.x < x + (Xsize) + (width / 2) &&
			vec.y > y - (height / 2) - Ysize && vec.y < y + height / 2) {
			clicked = true;
		}
		else {
			clicked = false;
		}
		
		if(vec.x > x - width / 2 && vec.x < x + (Xsize) + (width / 2) &&
			vec.y > y - (height / 2) - Ysize && vec.y < y + height / 2) {
			hovered = true;
		}
		else {
			hovered = false;
		}
		
		if(MyInput.isReleased() &&
				vec.x > x - width / 2 && vec.x < x + (Xsize) + (width / 2) &&
				vec.y > y - (height / 2) - Ysize && vec.y < y + height / 2) {
				released = true;
			}
		else released = false;
		
	}
	
	public void render(SpriteBatch sb) {
		
		sb.begin();
		
		sb.draw(LBcorner, x - (width / 2), y - (height / 2) - Ysize);
		sb.draw(LTcorner, x - (width / 2), (y));
		sb.draw(RBcorner, x + (Xsize), y - (height / 2) - Ysize);
		sb.draw(RTcorner, x + (Xsize), (y));
		
		sb.draw(Tline, x - width/4, y, 									(width) * (sizeModifierX) - width/2, Tline.getRegionHeight());
		sb.draw(Bline, x - width/4, y - (height / 2) - Ysize, 			(width) * (sizeModifierX) - width/2, Tline.getRegionHeight());
		sb.draw(Lline, x - width / 2, (y - height / 4) - Ysize,			Lline.getRegionWidth(),(height) * (sizeModifierY) - height/2);
		sb.draw(Rline, x + Xsize, (y - height / 4) - Ysize,				Rline.getRegionWidth(),(height) * (sizeModifierY) - height/2);
		
		sb.draw(Center, x - width/4, (y - height / 4) - Ysize,			(width) * (sizeModifierX) - width/2, (height) * (sizeModifierY) - height/2); 
		
		if(drawPicture)sb.draw(this.picture, x - width/4, (y - height / 4) - Ysize);
		
		if(sizeModifierY < 1)Play.drawLetter(sb, buttonText,x - 10, y, letterScale);
		else Play.drawLetter(sb, buttonText,x - 10, y - (height/4.5f), letterScale);
		
		
		sb.end();
		
	}
}
