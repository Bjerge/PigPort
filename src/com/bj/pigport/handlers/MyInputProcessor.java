package com.bj.pigport.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter{
	
	public boolean mouseMoved(int x, int y) {
		MyInput.x = x;
		MyInput.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		MyInput.x = x;
		MyInput.y = y;
		MyInput.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		MyInput.x = x;
		MyInput.y = y;
		MyInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		MyInput.x = x;
		MyInput.y = y;
		MyInput.down = false;
		return true;
	}
	
	public boolean keyDown(int k){
		if(k == Keys.SPACE){
			MyInput.setKey(MyInput.BUTTON1, true);
		}
		if(k == Keys.W) {
			MyInput.setKey(MyInput.BUTTON2, true);
		}
		if(k == Keys.T){
			MyInput.setKey(MyInput.BUTTON3, true);
		}
		if(k == Keys.A){
			MyInput.setKey(MyInput.BUTTON4, true);
		}
		if(k == Keys.D){
			MyInput.setKey(MyInput.BUTTON5, true);
		}
		if(k == Keys.S){
			MyInput.setKey(MyInput.BUTTON6, true);
		}
		if(k == Keys.Q){
			MyInput.setKey(MyInput.BUTTON7, true);
		}
		if(k == Keys.I){
			MyInput.setKey(MyInput.BUTTON_INVENTORY, true);
		}

		return true;
	}
	
	public boolean keyUp(int k){
		if(k == Keys.SPACE){
			MyInput.setKey(MyInput.BUTTON1, false);
		}
		if(k == Keys.W) {
			MyInput.setKey(MyInput.BUTTON2, false);
		}
		if(k == Keys.T){
			MyInput.setKey(MyInput.BUTTON3, false);
		}
		if(k == Keys.A){
			MyInput.setKey(MyInput.BUTTON4, false);
		}
		if(k == Keys.D){
			MyInput.setKey(MyInput.BUTTON5, false);
		}
		if(k == Keys.S){
			MyInput.setKey(MyInput.BUTTON6, false);
		}
		if(k == Keys.Q){
			MyInput.setKey(MyInput.BUTTON7, false);
		}
		if(k == Keys.I){
			MyInput.setKey(MyInput.BUTTON_INVENTORY, false);
		}
		return true;
	}
}
