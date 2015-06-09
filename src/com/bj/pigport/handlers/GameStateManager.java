package com.bj.pigport.handlers;

import java.util.Stack;

import com.bj.pigport.main.Game;
import com.bj.pigport.states.GameState;
import com.bj.pigport.states.MainMenu;
import com.bj.pigport.states.Play;
import com.bj.pigport.states.Town;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 7335461;
	public static final int MAINMENU = 325435;
	public static final int LEVELSELECT = 214324;
	public static final int BETWEENLEVEL = 583059;
	public static final int TOWN = 508958;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MAINMENU);
		//pushState(PLAY);
		//pushState(LEVELSELECT);
		
	}
	
	public Game game(){
		return game;
	}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	private GameState getState(int state){
		if(state == MAINMENU)
			return new MainMenu(this);
		if(state == PLAY)
			return new Play(this);
		if(state == TOWN)
			return new Town(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState(){
		GameState g = gameStates.pop();
		g.dispose();
	}
}
