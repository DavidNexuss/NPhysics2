package com.nsoft.nphysics;

import java.util.ArrayList;

public interface Parent<T> {

	public void updatePosition(float x,float y,T p);
	
	public ArrayList<T> getChildList();
}
