package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.sandbox.interfaces.Position;

public class PositionVector extends Vector2 implements Position{

	
	public PositionVector(float x,float y) {
		super(x, y);
	}
	
	public PositionVector(Vector2 v) {
		super(v);
	}
	@Override
	public float getX() { return super.x; }

	@Override
	public float getY() { return super.y; }
}
