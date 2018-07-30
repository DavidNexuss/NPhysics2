package com.nsoft.nphysics.sandbox.interfaces;

import java.util.ArrayList;

public interface Parent<T> {

	public void updatePosition(float x,float y,T p);
	public default void updatePosition() {
		updatePosition(0, 0, null);
	}
	public ArrayList<T> getChildList();
}
