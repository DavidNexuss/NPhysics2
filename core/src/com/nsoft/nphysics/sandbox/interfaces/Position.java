package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public interface Position {

	public float getX();
	public float getY();
	
	public default float getLen2() {
		
		return getX()*getX() + getY()*getY();
	}
	public default float getLen() {
		
		return (float) Math.sqrt(getLen2());
	}
	public default float getAngleRad() {
		
		return MathUtils.atan2(getY(), getX());
	}
	public default float getAngle() {
		
		return getAngleRad()*MathUtils.radDeg;
	}
}

