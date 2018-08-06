package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;

public class Force implements Say{
	
	public static final float NULL = Float.MAX_VALUE;
	private Vector2 force;
	private Vector2 position;
	
	private boolean knowX,knowY;
	
	public Force() {
		
		knowX = true;
		knowY = true;
		position = new Vector2();
		force = new Vector2();
	}
	public Force(float positionx,float positiony,float forcex,float forcey) {
		
		this(new Vector2(positionx, positiony), new Vector2(forcex,forcey));
	}
	public Force(Vector2 position,Vector2 force) {
		
		this(position,force, force.x != NULL, force.y != NULL);
	}
	public Force(Vector2 position,Vector2 force,boolean knowX,boolean knowY) {
		
		this.force = force;
		this.position = position;
		this.knowX = knowX;
		this.knowY = knowY;
		
		if(MainTest.DEBUG) {
			
			say("New force at: " + position.toString());
			say("Force: " + getForceVector().toString());
			say("X: " + knowX);
			say("Y: " + knowY);
		}
			
	}

	public void setKnowX(boolean newKnowX) {knowY = newKnowX;}
	public void setKnowY(boolean newKnowY) {knowY = newKnowY;}
	
	public boolean isXKnown() {return knowX;}
	public boolean isYKnown() {return knowY;}
	
	public boolean isKnown(boolean any) {return any ? knowY || knowX : knowX && knowY;}
	
	public void setPosition(float x,float y) {
		
		position.set(x, y);
	}
	
	public void setPolarPosition(float mod,float degrees) {
		
		position.set(1, 0);
		position.setAngle(degrees);
		position.setLength(mod);
		
		say(position);
	}
	public void setForce(float x,float y) {
		
		force.set(x, y);
		setKnowX(x != NULL);
		setKnowY(y != NULL);
	}
	
	public void setPolarForce(float mod,float degrees) {
		
		if(mod == 0) {
			
			setKnowX(false);
			setKnowY(false);
			force.set(Vector2.Zero);
			return ;
		}
		force.set(1, 0);
		force.setLength(mod);
		force.setAngle(degrees);
	}
	public Vector2 getForceVector() {
		
		return new Vector2(knowX ? force.x : Float.NaN,knowY ? force.y : Float.NaN);
	}
	
	public Vector2 getPositionVector() {
		
		return new Vector2(position);
	}
	
	@Override
	public String toString() {
		
		return "Position: " + getPositionVector().toString() + "|| Force: " + getForceVector().toString();
	}
}
