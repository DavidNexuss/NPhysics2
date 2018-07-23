package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.MathUtils;

public class Util {

	
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
}
