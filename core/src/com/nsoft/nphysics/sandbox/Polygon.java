package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Parent;

import earcut4j.Earcut;

public class Polygon extends Actor implements Parent<Point>{

	private ArrayList<Point> points = new ArrayList<>();
	private Point initial;
	private List<Integer> indexes = new ArrayList<Integer>();
	private double[] buffer;
	private boolean end = false;
	
	public static Polygon temp;
	public Polygon() {
		
		setDebug(true);
	}
	
	@Override
	protected void drawDebugBounds(ShapeRenderer shapes) {}
	
	final Color shape = new Color(0.2f, 0.8f, 0.2f, 0.6f);
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(!isEnded()) return;
		
		Sandbox.shapefill.setColor(shape);
		for (int i = 0; i < indexes.size(); i+=3) {
			
			Sandbox.shapefill.triangle(points.get(indexes.get(i)).getX(), 
									   points.get(indexes.get(i)).getY(), 
									   points.get(indexes.get(i + 1)).getX(), 
									   points.get(indexes.get(i + 1)).getY(), 
									   points.get(indexes.get(i + 2)).getX(), 
									   points.get(indexes.get(i + 2)).getY());
		}
		
	}
	public Polygon addPoint(Point p){
		
		if(isEnded()) return this;
		
		if(points.size() == 0) initial = p;
		
		for (Point pa : points) {
			
			if(pa == p && p != initial)return this;
		}
		
		if(p == initial && points.size() > 1) {
			
			NPhysics.sandbox.addActor(new Segment(points.get(points.size() - 1), p));
			end(); 
			return this;
		}
		
		if(points.size() != 0) {
			
			NPhysics.sandbox.addActor(new Segment(points.get(points.size() - 1), p));
		}
		points.add(p);
		return this;
	}
	
	public boolean isEnded() {return end;}
	
	private void createBuffer() {
		
		buffer = new double[points.size()*2];
		
		for (int i = 0; i < points.size()*2; i+=2) {
				
			buffer[i] = points.get(i/2).getX();
			buffer[i + 1] = points.get(i/2).getY();
		}
	}
	private void triangulate() {
		
		createBuffer();
		
		indexes = Earcut.earcut(buffer);
	}
	public void end() {
		
		for (Point point : points) {
			
			point.setPolygonParent(this);
		}
		
		triangulate();
		end = true;
		
		if(temp == this) {
			
			NPhysics.sandbox.addActor(this);
			temp = null;
		}
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
		if(!isEnded())return;
		triangulate();
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return isEnded() ? points : null;
	}
}
