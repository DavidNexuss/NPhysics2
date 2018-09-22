package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.NPhysics;

public class FastPolygonCreator {

	
	public static PolygonActor temp;
	
	public static void handleClick(float x,float y) {
		
		if(temp == null || temp.isEnded()) create();
		
		Point p = Point.getPoint(x, y);

		temp.addPoint(p);
		if(temp.isEnded()) NPhysics.currentStage.addActor(temp);
	}
	private static void create() {
		
		temp = new PolygonActor();
	}
	
	
}
