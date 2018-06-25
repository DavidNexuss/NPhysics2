package com.nsoft.nphysics;

import java.util.ArrayList;

public class Polygon {

	private ArrayList<Point> points;
	
	public Polygon() {
		
	}
	
	public Polygon addPoint(Point p){
		
		points.add(p);
		return this;
	}
	
	public void end() {
		
		
	}
}
