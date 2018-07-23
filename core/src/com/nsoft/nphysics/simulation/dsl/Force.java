package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;

import static com.nsoft.nphysics.simulation.dsl.Core.*;
public class Force implements Dev{

	public static final float NULL = Float.MAX_VALUE;
	public static float G = 9.81f;
	
	private Vector2 position = new Vector2(Vector2.Zero);
	private Vector2 strenght;

	private float angle = NULL;
	private float mod = NULL;
	
	private float[] temporalStrenght = new float[2];
	
	{
		temporalStrenght[0] = NULL;
		temporalStrenght[1] = NULL;
		
	}
	
	public boolean hasX() { return isDone() || temporalStrenght[0] != NULL;}
	public boolean hasY() { return isDone() || temporalStrenght[1] != NULL;}
	
	public Force(float mass,Vector2 pos) {
		
		strenght = new Vector2(0, mass*-G);
		position = pos;
	}
	
	public Force(Vector2 pos,Vector2 str) {
		
		position = pos;
		strenght = str;
	}
	
	public Force(float[] pos,float[] str) {
		
		Vector2 position = new Vector2(pos[0], pos[1]);
		this.position = position;
		
		if(str[0] == NULL || str[1] == NULL) {
			
			if(str[0] != NULL) {
				
				temporalStrenght[0] = str[0];
			}
			if(str[1] != NULL) {
				
				temporalStrenght[1] = str[1];
			}
			
			if(DEBUG) {say("Incomplete force: X: " + (str[0] == NULL ? "NULL" : str[0]) + 
											" Y: " + (str[1] == NULL ? "NULL" : str[1]) +
											" Angle: " + (getAngleDegrees() == NULL ? "NULL" : getAngleDegrees()));}
		}else {
			
			Vector2 strenght = new Vector2(str[0], str[1]);
			if(DEBUG) {say("Complete force: X:" + strenght.x + " Y: " + strenght.y);}
			this.strenght = strenght;
		}
		
	}
	
	public Force(float[] pos) {
		
		this(pos,new float[] {NULL,NULL});
	}
	public Force(float[] pos,float mod,float angle) {
		
		Vector2 position = new Vector2(pos[0], pos[1]);
		this.position = position;
		
		if(mod == 0 ) throw new IllegalStateException("A force may not have a null mod value");
		
		this.mod = mod;
		this.angle = (float) Math.toRadians(angle);
		
		if(!completeUsingTrigonometric()) throw new IllegalStateException("Error in parsing polar expression to cartesian");
		else if(DEBUG) say("Force succesfully casted from polar to cartesian. X: " + strenght.x + " Y: " + strenght.y);
	}
	
	public Force setAngleDegrees(float angle) {
		
		if(this.angle == NULL) this.angle = (float) Math.toRadians(angle);
		say("Angle changed: " + angle);
		return this;
	}

	public Force setAngleRadians(float angle) {
		
		if(this.angle == NULL) this.angle = angle;
		say("Angle changed: " + angle);
		return this;
	}
	
	public float getAngleRadians() {
		
		if(angle == Float.MAX_VALUE) {
			
			if(isComplete()) {

				return angle = strenght.angleRad();
			}
		}return angle;
	}

	public float getAngleDegrees() {
		
		if(angle == Float.MAX_VALUE) {
			
			if(isComplete()) {

				return angle = strenght.angle();
			}
		}return (float) Math.toDegrees(angle);
	}
	public Force setMod(float mod) {
		
		if(this.mod == NULL) this.mod = mod;
		return this;
	}
	public void end() {
		
		if(isComplete() && !isDone()) {
			
			strenght = new Vector2(temporalStrenght[0], temporalStrenght[1]);
			
			if(DEBUG) {
				
				say("Force solved: X: " + strenght.x + " Y: " + strenght.y);
			}
		} 
		
		
	}
	
	public void setTempX(float x) {
		
		temporalStrenght[0] = x;
	}
	
	public void setTempY(float y) {
		
		temporalStrenght[1] = y;
	}
	public void setForce(Vector2 newStrenght) {
		
		if(!isDone()) throw new IllegalStateException("Cant use that before solving the force!");
		say("New force set: X: " + newStrenght.x + " Y: " + newStrenght.y);
		strenght.set(newStrenght);
	}
	public boolean completeUsingTrigonometric() {
		
		if(isDone()) return true;
		
		Vector2 test = getTemporalVector();
		if(test != null) {
			
			if(strenght == null) { 
				
				strenght = new Vector2();
			}
			setForce(test);
			return true;
		}else {
			
			return false;
		}
	}
	public boolean isUnkown() {
		
		return angle == NULL && !isDone() && temporalStrenght[0] == NULL && temporalStrenght[1] == NULL;
	}
	public boolean isComplete() {
		
		return !(temporalStrenght[0] == NULL || temporalStrenght[1] == NULL);
	}
	
	private Vector2 tempStrenght;
	
	public float[] getTemporalFloat() {
		
		if(isDone()) {
			
			return temporalStrenght = new float[] {strenght.x,strenght.y};
		}
		return temporalStrenght;
		
	}
	public Vector2 getTemporalVector() {
		
		if(isDone()) return strenght;
		else if(tempStrenght == null){
			
			tempStrenght = Vector2.Zero;
			if(temporalStrenght[0] != NULL && temporalStrenght[1] != NULL) {
				
				tempStrenght.set(temporalStrenght[0], temporalStrenght[1]);
			}
			else if(angle != NULL && mod != NULL && temporalStrenght[0] != NULL) {
				
				tempStrenght.set(temporalStrenght[0], (float) (mod*Math.sin(angle)));
			}
			else if(angle != NULL && mod != NULL && temporalStrenght[1] != NULL) {
				
				tempStrenght.set((float) (Math.cos(angle)*mod), temporalStrenght[1]);
			}
			else if(angle != NULL && mod != NULL) {
				
				tempStrenght.set((float)Math.cos(angle)*mod, (float)Math.sin(angle)*mod);
			}else {
				
				return null;
			}
		}
		
		return tempStrenght;
	}
	/**
	 * If the Force Vector is incomplete return false, otherwise true.
	 * @return
	 */
	public boolean isDone() {
		
		return strenght != null;
	}
	public float mod() {
		
		if(mod == Float.MAX_VALUE) {
			
			if(isDone()) {
				
				return mod = strenght.len();
			}
		}return mod;
	}
	
	
	public float getDist(Force b) {
		b.position = b.position;
		position = position;
		return position.dst(b.position);
		}
	public Vector2 getVectorDist(Force b) { return new Vector2(b.position).sub(position);}
	
	public Vector2 getPosition() {return position;}
	
	public float getAngle(Vector2 ref) {
		
		return strenght.angleRad(ref);
	}
	
	public static Force force(float posx, float posy, float strx, float stry) {
		
		return new Force(arr(posx, posy),arr(strx, stry));
	}

	public static Force force(float pos[], float strx, float stry) {
		
		return force(pos[0],pos[1],strx,stry);
	}

	public static Force force(float[] pos,float[] force) {
		
		return new Force(pos, force);
	}
	
	public static float[] arr(float x,float y) {
		
		return new float[] {x,y};
	}
	
	public static float[] pol(float mod,float angle) {
		
		
		return arr((float)Math.cos(Math.toRadians(angle))*mod, (float)Math.sin(Math.toRadians(angle))*mod);
	}
	
}
