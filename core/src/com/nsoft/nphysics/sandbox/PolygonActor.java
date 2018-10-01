package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.DiscontLine;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.FontManager;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;

import earcut4j.Earcut;

public class PolygonActor extends PhysicalActor<PolygonDefinition>{

	private Point initial;
	private ArrayList<Segment> segments = new ArrayList<>();
	private ArrayList<Integer> indexes = new ArrayList<>();
	private double[] buffer;
	private float X,Y,width,height; //BOUNDS
	
	private Polygon hitboxPolygon; 
	
	private static float axisMargin = 20;
	
	private int forceVariableCount;
	
	private float physMass;
	
	public static PolygonActor temp;

	public PolygonActor() {
		
		
	}

	public void initDefinition() {
		
		definition = new PolygonDefinition();
	}
	
	public PolygonActor createCopy(Vector2 offset) {
		
		PolygonActor newpolygon = new PolygonActor();
		Vector2 center = definition.getCenter(false);
		
		Point first = null;
		for (Point p : points) {
			
			Point pi = new Point(p.getX() - center.x + offset.x, p.getY() - center.y + offset.y, false);
			if(first == null) {
				
				first = pi;
			}
			newpolygon.addPoint(pi);
			NPhysics.currentStage.addActor(pi);
		}
		
		newpolygon.addPoint(first);
		
		DynamicWindow.dumpConfiguration(this, newpolygon);
		
		return newpolygon;
	}

	
	public void updateForceVariableCount() {
		forceVariableCount = 0;
		for (ObjectChildren f : getComponents()) {
			
			if(f instanceof ForceComponent) {
				
				forceVariableCount += ((ForceComponent) f).isVariable() ? 1 : 0;
			}
		}
	}
	@Override
	public boolean isInside(float x, float y) {
		
		if (x < X || x > width || y < Y|| y > height) return false;
		if(hitboxPolygon.contains(x, y)) return true;
		return false;
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		if(!isEnded()) return;
		
		Util.renderPolygon(NPhysics.currentStage.shapefill, points, indexes);
		
		
		super.draw(batch, parentAlpha);
		
	}
	public PolygonActor addPoint(Point p){
		
		if(isEnded()) return this;
		
		if(points.size() == 0) initial = p;
		
		for (Point pa : points) {
			
			if(pa == p && p != initial)return this;
		}
		
		if(p == initial && points.size() > 1) {
			
			Segment c = new Segment(points.get(points.size() - 1),p);
			segments.add(c);
			NPhysics.sandbox.addActor(c);
			
			end(); 
			return this;
		}
		
		if(points.size() != 0) {
			
			Segment c = new Segment(points.get(points.size() - 1), p);
			segments.add(c);
			NPhysics.sandbox.addActor(c);
		}
		
		points.add(p);
		
		return this;
	}
	
	public ArrayList<Point> getPointList() {return points;}
	
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
		
		calculateMass();
		
	}
	
	public float getArea(boolean m2) {
		
		if(m2) return hitboxPolygon.area() / (30*30);
		else return hitboxPolygon.area();
	}
	public float calculateMass() {
		
		physMass = definition.density * getArea(true);
		setValue("polygon_phys_mass", physMass);
		return physMass;
	}
	public float calculateDensity() {
		
		definition.density = physMass / getArea(true);
		setValue("polygon_phys_density", definition.density);
		return definition.density;
	}
	private void createDefinition() {
		
		definition.vertices.clear();
		definition.indexes.clear();
		
		definition.indexes = indexes;
		for (Point p : points) {
			
			definition.vertices.add(new PositionVector(p.getX(), p.getY()));
		}
		
		definition.init();
		definition.childrens = getComponents();
		
	}
	
	@Override
	float getArea() {
		return hitboxPolygon.area() / (30*30);
	}
	@Override
	public void end() {
		

		for (Point point : points) {
			
			point.setObjectParent(this);
		}
		
		triangulate();
		
		if(temp == this) {
			
			NPhysics.sandbox.addActor(this);
			temp = null;
		}

		createDefinition();
		createHitBox();

		calculateMass();
		
		super.end();
	}

	
	
	@Override
	public void act(float delta) {
		
		
		super.act(delta);
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		super.updatePosition(x, y, p);
		if(!isEnded())return;
		triangulate();

		createDefinition();
		calculateHitBox();
	}
	
	@Override
	public boolean remove() {
		
		for (Segment s : segments) {
			s.remove();
		}
		return super.remove();
	}

	@Override
	public void updateValuesFromForm() {
		
		super.updateValuesFromForm();
		float newDensity = getValue("polygon_phys_density");
		
		float newMass = getValue("polygon_phys_mass");
		
		if( definition.density != newDensity) {
			
			definition.density = newDensity;
			calculateMass();
		
		}else if(physMass != newMass) {
			
			physMass = newMass;
			calculateDensity();
		}
		
		switch ((int)getValue("polygon_phys_state")) {
		case 2:
			definition.type = BodyType.StaticBody;
			break;
		case 1:
			definition.type = BodyType.KinematicBody;
			break;
		case 0:
			definition.type = BodyType.DynamicBody;
			break;
		default:
			throw new IllegalStateException();
		}
		
		definition.linearVelocity.set(getValue("polygon_lvel_x"), 
									  getValue("polygon_lvel_y"));
		
	}

	@Override
	public void updateValuesToForm() {
		
		
	}
}
