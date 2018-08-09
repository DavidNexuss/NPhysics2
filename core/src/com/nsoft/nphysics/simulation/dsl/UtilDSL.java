package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class UtilDSL {

	
	public static int UNIT = 10;
	
	public static float rotx(float x,float y,float rad) {
		
		return x*MathUtils.cos(rad) - y*MathUtils.sin(rad);
	}
	
	public static float roty(float x,float y,float rad){
		
		return y*MathUtils.cos(rad) + x*MathUtils.sin(rad);
	}
	
	public static void rot(float[] xy,float rad) {
		
	}
	
	public static void proj(float[][] vertices,float[][] buffer,float xoffset,float yoffset,float anglerad) {
		
		for (int i = 0; i < buffer.length; i++) {
			
			buffer[i][0] = rotx(vertices[i][0], vertices[i][1], anglerad) + xoffset;
			buffer[i][1] = roty(vertices[i][0], vertices[i][1], anglerad) + yoffset;
		}
	}
	
	public static Force getProjectedForce(Force a,float degrees) {
		
		Vector2 position = a.getPositionVector().rotate(degrees);
		Vector2 force = a.getForceVector().rotate(degrees);
		
		return new Force(position, force);
	}
}