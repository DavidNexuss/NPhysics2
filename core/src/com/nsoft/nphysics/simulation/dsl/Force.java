package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class Force implements Say{
	
	public static enum Variable{
		
		NONE,X,Y,MOD,ANGLE;
	}
	public static final float NULL = Float.MAX_VALUE;
	
	public static final Vector2 NULL_V = new Vector2(NULL, NULL);
	public static final Vector2 NULL_VX = new Vector2(NULL,0);
	public static final Vector2 NULL_VY = new Vector2(0,NULL);
	
	private Vector2 force;
	private Vector2 position;
	
	private boolean knowX,knowY;
	
	public Variable var = Variable.NONE;
	
	public ObjectChildren parent;
	
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

	public Force setParent(ObjectChildren p) {parent = p; return this;}
	public void setKnowX(boolean newKnowX) {knowX = newKnowX;}
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
	
	public void setVariable(Variable nvar) {var = nvar;}
	public Variable getVariableType() {return var;}
	
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
