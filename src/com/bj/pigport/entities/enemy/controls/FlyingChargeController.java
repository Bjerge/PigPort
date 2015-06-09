package com.bj.pigport.entities.enemy.controls;

import com.bj.pigport.entities.enemy.Enemy;
import com.bj.pigport.states.Play;

public class FlyingChargeController {
	public FlyingChargeController()
	{
		
	}
	
	public static void FlyingChargeControls(Enemy enemy)
	{
		if(enemy.data.getFlyingState() == 0)
		{
			enemy.data.setFlyPosX(enemy.getPosition().x);
			if(enemy.getPosition().y < Play.player.getPosition().y) {enemy.data.setFlyPosY(enemy.getPosition().y + 2); enemy.data.setFlyRotation(270);}
			else 											  {enemy.data.setFlyPosY(enemy.getPosition().y - 2); enemy.data.setFlyRotation(90);}
			
			if(enemy.getPosition().y < Play.player.getPosition().y && enemy.getPosition().x > Play.player.getPosition().x)enemy.data.setFlyingState(1);
			if(enemy.getPosition().y > Play.player.getPosition().y && enemy.getPosition().x > Play.player.getPosition().x)enemy.data.setFlyingState(2);
			if(enemy.getPosition().y < Play.player.getPosition().y && enemy.getPosition().x < Play.player.getPosition().x)enemy.data.setFlyingState(3);
			if(enemy.getPosition().y > Play.player.getPosition().y && enemy.getPosition().x < Play.player.getPosition().x)enemy.data.setFlyingState(4);
		}
		if(enemy.data.getFlyingState() == 1 | enemy.data.getFlyingState() == 2 | enemy.data.getFlyingState() == 3 | enemy.data.getFlyingState() == 4)
		{
			enemy.getBody().setTransform( (float) ((enemy.data.getFlyPosX()) + (2f * Math.cos((enemy.data.getFlyRotation())/58f))) , (float) ((enemy.data.getFlyPosY()) + (2f * Math.sin((enemy.data.getFlyRotation())/58f))) , 0);
			if(enemy.data.getFlyingState() == 1 | enemy.data.getFlyingState() == 4) enemy.data.setFlyRotation(enemy.data
					.getFlyRotation() + 1);
			if(enemy.data.getFlyingState() == 2 | enemy.data.getFlyingState() == 3) enemy.data.setFlyRotation(enemy.data
					.getFlyRotation() - 1);
			if(enemy.data.getFlyingState() == 1 | enemy.data.getFlyingState() == 4) if(enemy.data.getFlyRotation() == 360) enemy.data.setFlyRotation(0);
			if(enemy.data.getFlyingState() == 2 | enemy.data.getFlyingState() == 3) if(enemy.data.getFlyRotation() == 0) enemy.data.setFlyRotation(360);
			if(enemy.data.getFlyingState() == 1 | enemy.data.getFlyingState() == 3) if(enemy.data.getFlyRotation() == 90) enemy.data.setFlyingState(5);
			if(enemy.data.getFlyingState() == 2 | enemy.data.getFlyingState() == 4) if(enemy.data.getFlyRotation() == 270) enemy.data.setFlyingState(5);
		}
		else if(enemy.data.getFlyingState() == 5)
		{
			float x = (Play.player.getPosition().x - enemy.getPosition().x) * 0.8f;
			float y = (Play.player.getPosition().y - enemy.getPosition().y) * 0.8f;
			
			enemy.getBody().setLinearVelocity(x, y);
			
			if( (Math.sqrt (Math.pow(x,2) + Math.pow(y,2)))  < 3) enemy.data.setFlyingState(6);
			
			if(enemy.data.getFlyingState() == 6)
			{
				enemy.getBody().setLinearVelocity(x, y);
				enemy.data.setFlyingState(7);
			}
		}
		
		if(enemy.data.getFlyingState() == 7)
		{
			enemy.data.setFlyChargeTimer(enemy.data
					.getFlyChargeTimer() + 1);
			if(enemy.data.getFlyChargeTimer() > 120)
			{
				enemy.data.setFlyingState(0);
				enemy.data.setFlyChargeTimer(0);
			}
		}
	}
}
