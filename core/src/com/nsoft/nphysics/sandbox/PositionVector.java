package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.sandbox.interfaces.Position;

public class PositionVector implements Position{

	float x,y;
	public PositionVector(float x,float y) {
		
		this.x = x;
		this.y = y;
	}
	
	@Override
	public float getX() { return x; }
	
	@Override
	public float getY() { return y; }
}
