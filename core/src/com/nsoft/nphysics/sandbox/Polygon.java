package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;

import earcut4j.Earcut;

public class Polygon extends Actor implements Parent<Point>{

	public static ArrayList<Polygon> polygonlist = new ArrayList<>();
	
	private ArrayList<Point> points = new ArrayList<>();
	private Point initial;
	private ArrayList<Integer> indexes = new ArrayList<Integer>();
	private double[] buffer;
	private boolean end = false;
	
	private PolygonDefinition definition;
	
	public static Polygon temp;
	
	public Polygon() {
		
		setDebug(true);
		definition = new PolygonDefinition();
	}
	
	public PolygonDefinition getDefinition() {return definition;}
	@Override
	protected void drawDebugBounds(ShapeRenderer shapes) {}
	
	final static Color shape = new Color(0.2f, 0.8f, 0.2f, 0.6f);
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(!isEnded()) return;
		
		Sandbox.shapefill.setColor(shape);
		
		Util.renderPolygon(Sandbox.shapefill, points, indexes);
		
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
		
		indexes = (ArrayList<Integer>) Earcut.earcut(buffer);
	}
	
	private void createDefinition() {
		
		definition.vertices.clear();
		definition.indexes.clear();
		
		definition.indexes = indexes;
		for (Point p : points) {
			
			definition.vertices.add(new PositionVector(p.getX(), p.getY()));
		}
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
		
		polygonlist.add(this);
		createDefinition();
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
		if(!isEnded())return;
		triangulate();
		createDefinition();
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return isEnded() ? points : null;
	}
	
}
