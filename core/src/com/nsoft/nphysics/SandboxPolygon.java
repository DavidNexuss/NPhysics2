package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class SandboxPolygon extends Actor implements Parent<Segment>{

	ArrayList<Point> points;
	private boolean end;
	public SandboxPolygon() {
		
	}
	
	public void addPoint(Point p) {
		
		points.add(p);
	}
	
	public void end(){
		
		end = true;
	}
	
	@Override
	public void updatePosition(float x, float y, Segment p) {
		
		
	}

	@Override
	public ArrayList<Segment> getChildList() {
		
		return null;
	}

}
