package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Util {

	
	public static int UNIT = 30;
	
	/*
	 * p[0] =  A.x
	 * p[1] =  A.y
	 * p[2] =  B.x
	 * p[3] =  B.y
	 * p[4] =  C.x
	 * p[5] =  C.y
	 */
	public static float triangleArea(float ... p) {
		
		return Math.abs((p[0]*(p[3] - p[5]) + p[2]*(p[5] - p[1]) + p[4]*(p[1] - p[3]))/2);
	}
	public static float rotx(float x,float y,float rad) {
		
		return x*MathUtils.cos(rad) - y*MathUtils.sin(rad);
	}
	
	public static float roty(float x,float y,float rad){
		
		return y*MathUtils.cos(rad) + x*MathUtils.sin(rad);
	}
	
	public static float rotVectorX(Vector2 point,float anglerad) {
		
		return rotx(point.x, point.y, anglerad);
	}
	
	public static float rotVectorY(Vector2 point,float anglerad) {
		
		return roty(point.x, point.y, anglerad);
	}
	public static Vector2 rotPivot(Vector2 pivot,Vector2 point,float anglerad) {
		
		Vector2 dif = new Vector2(point).sub(pivot);
		
		float x;
		float y;
		
		x = rotVectorX(dif, anglerad);
		y = rotVectorY(dif, anglerad);
		
		return new Vector2(x, y).add(pivot);
	}
	
	public static Vector2 rot(Vector2 point,float anglerad) {
		
		return rotPivot(new Vector2(), point, anglerad);
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
