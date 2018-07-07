package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;

import earcut4j.Earcut;

public class PolygonActor extends Actor implements Parent<Point>,ClickIn,Handler{

	public static ArrayList<PolygonActor> polygonlist = new ArrayList<>();
	private Point initial;
	private ArrayList<Point> points = new ArrayList<>();
	private ArrayList<ObjectChildren> components = new ArrayList<>();
	private ArrayList<Integer> indexes = new ArrayList<>();
	private double[] buffer;
	private boolean end = false;
	private float X,Y,width,height; //BOUNDS
	
	private PolygonDefinition definition;
	private Polygon hitboxPolygon;
	
	public SelectHandle handler = new SelectHandle();  
	@Override public SelectHandle getSelectHandleInstance() { return handler; }
	
	public static PolygonActor temp;

	public PolygonActor() {
		
		setDebug(true);
		addInput();
		definition = new PolygonDefinition();
	}
	
	public PolygonDefinition getDefinition() {return definition;}
	
	@Override
	public boolean isInside(float x, float y) {
		
		if (x < X || x > width || y < Y|| y > height) return false;
		
		if(hitboxPolygon.contains(x, y)) return true;
		return false;
	}

	@Override
	public void unselect() {
		
		current = shape;
		UIStage.contextMenu.hide();
		
	}
	@Override
	public void select() {
		
		current = shapeSelected;
		UIStage.contextMenu.show();
	}
	
	@Override
	public SelectHandle getHandler() {
		
		return Sandbox.mainSelect;
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
	}
	final static Color shape = 		   new Color(0.2f, 0.8f, 0.2f, 0.6f);
	final static Color shapeSelected = new Color(0.8f, 0.2f, 0.2f, 0.6f);
	
	Color current = shape;
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(!isEnded()) return;
		
		Sandbox.shapefill.setColor(current);
		
		Util.renderPolygon(Sandbox.shapefill, points, indexes);
		
	}
	public PolygonActor addPoint(Point p){
		
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
	
	private void calculaeBounds() {
		
		X = Float.MAX_VALUE;
		Y = Float.MAX_VALUE;
		height = Float.MIN_VALUE;
		width = Float.MIN_VALUE;
		
		for (Point p : points) {
			
			float px = p.getX();
			float py = p.getY();
			
			if(px < X) X = px;
			if(px > width) width = px;
			if(py < Y) Y = py;
			if(py > height) height = py;
		}
	}

	private void createHitBox() {
		calculaeBounds();
		hitboxPolygon = new Polygon(definition.getRawVertices());
	}
	private void calculateHitBox() {
		
		calculaeBounds();
		hitboxPolygon.setVertices(definition.getRawVertices());
	}
	private void createDefinition() {
		
		definition.vertices.clear();
		definition.indexes.clear();
		
		definition.indexes = indexes;
		for (Point p : points) {
			
			definition.vertices.add(new PositionVector(p.getX(), p.getY()));
		}
		
		definition.init();
		definition.childrens = components;
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
		createHitBox();
		
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
		if(!isEnded())return;
		triangulate();
		createDefinition();
		calculateHitBox();
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return isEnded() ? points : null;
	}
	
	
	public void addComponent(ObjectChildren child) {
		
		components.add(child);
	}
}
