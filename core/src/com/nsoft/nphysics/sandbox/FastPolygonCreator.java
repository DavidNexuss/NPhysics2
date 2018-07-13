package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.NPhysics;

public class FastPolygonCreator {

	
	static PolygonActor temp;
	
	public static void handleClick(float x,float y) {
		
		if(temp == null || temp.isEnded()) create();
		
		Point p = new Point(x, y, false);
		for (Point a : temp.getPointList()) {
			
			if(Math.abs(a.getX() - x) < Point.INPUT_RADIUS && Math.abs(a.getY() - y) < Point.INPUT_RADIUS) {
				
				p = a;
				break;
			}
		}
		
		NPhysics.currentStage.addActor(p);
		
		temp.addPoint(p);
		if(temp.isEnded()) NPhysics.currentStage.addActor(temp);
	}
	private static void create() {
		
		temp = new PolygonActor();
	}
	
	
}
