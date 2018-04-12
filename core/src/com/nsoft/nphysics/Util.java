package com.nsoft.nphysics;

import com.badlogic.gdx.math.MathUtils;

public class Util {

	public static float rotx(float x,float y,float rad) {
		
		return x*MathUtils.cos(rad) - y*MathUtils.sin(rad);
	}
	
	public static float roty(float x,float y,float rad){
		
		return y*MathUtils.cos(rad) + x*MathUtils.sin(rad);
	}
	
	public static void rot(float[] xy,float rad) {
		
		
	}
}
