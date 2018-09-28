package com.nsoft.nphysics.sandbox;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
	
	public static void createCircle(Vector2 center,float radius) {
		
		create();
		
		Point start = Point.getPoint(center.x + radius, center.y);
		temp.addPoint(start);
		
		for (int i = 1; i < 360; i++) {
			
			Vector2 proj = new Vector2(center.x + radius,center.y);
			proj = Util.rotPivot(center, proj, i * MathUtils.degRad);
			Point p = new Point(proj.x, proj.y, false);
			NPhysics.currentStage.addActor(p);
			p.setVisible(false);
			temp.addPoint(p);
		}
		
		start.setVisible(false);
		temp.addPoint(start);
		
		NPhysics.currentStage.addActor(temp);
	}
	
	
}
