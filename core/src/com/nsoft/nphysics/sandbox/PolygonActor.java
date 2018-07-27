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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.simulation.dynamic.PolygonDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;

import earcut4j.Earcut;

public class PolygonActor extends Group implements Parent<Point>,ClickIn,Handler,Removeable,Draggable{

	public static ArrayList<PolygonActor> polygonlist = new ArrayList<>();
	private Point initial;
	private ArrayList<Point> points = new ArrayList<>();
	private ArrayList<Segment> segments = new ArrayList<>();
	private ArrayList<ObjectChildren> components = new ArrayList<>();
	private ArrayList<Integer> indexes = new ArrayList<>();
	private double[] buffer;
	private boolean end = false;
	private float X,Y,width,height; //BOUNDS
	
	private PolygonDefinition definition;
	private Polygon hitboxPolygon;
	
	public SelectHandle handler = new SelectHandle();  
	
	private Vector2 polygonMassCenter = new Vector2();
	private SimpleArrow gravityArrow;
	private DoubleArrow xaxis;
	private DoubleArrow yaxis;
	private static float axisMargin = 20;
	
	@Override public SelectHandle getSelectHandleInstance() { return handler; }
	
	public static PolygonActor temp;

	public PolygonActor() {
		
		setDebug(true, true);
		addInput();
		addDragListener();
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
	//	xaxis.hide();
	//	yaxis.hide();
		
	}
	@Override
	public void select(int pointer) {
		
		current = mightSelected;
		UIStage.contextMenu.show();
	//	xaxis.show();
	//	yaxis.show();
	}
	
	@Override
	public SelectHandle getHandler() {
		
		return Sandbox.mainSelect;
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
	}
	
	final Vector2 origin = new Vector2(0, 0);
	final Vector2 start = new Vector2(0, 0);
	@Override
	public void addDragListener() {


		ClickIn Pointer = this;
		DragListener d = new DragListener() {
		    public void drag(InputEvent event, float x, float y, int pointer) {
		    	
		    	doDrag(true,x,y,event);
		    }
		    
		    @Override
		    public void dragStart(InputEvent event, float x, float y, int pointer) {
		    	
		    	start.set(NPhysics.currentStage.getUnproject(false));
		    	origin.set(NPhysics.currentStage.getUnproject(false));
		    	if(!isSelected()) getHandler().setSelected(Pointer);
				
		    	sumx = 0;
		    	sumy = 0;
		    	
		    	for (Point p : points) {
					
		    		p.originx = p.getX();
		    		p.originy = p.getY();
				}
		    	
		    }
		    
		    @Override
		    public void dragStop(InputEvent event, float x, float y, int pointer) {
		    	
		    	updatePosition(0, 0, null);
		    }
		};
		
		d.setTapSquareSize(1);
		addListener(d);
	}
	
	float sumx = 0;
	float sumy = 0;
	@Override
	public void doDrag(boolean pool, float x, float y,InputEvent event) {
		
		Vector2 v2 = new Vector2(NPhysics.currentStage.getUnproject(false)).sub(start);
		for (Point p : points) {

			if (NPhysics.currentStage.isSnapping()) {
				
				p.setPosition(DragStage.snapGrid(p.originx + sumx),DragStage.snapGrid(p.originy + sumy));
				
			}else {
				p.moveBy(v2.x, v2.y);
				updatePosition(0, 0, null);
			}
			
		}
		
		start.set(NPhysics.currentStage.getUnproject(false));
		

		sumx = start.x - origin.x;
		sumy = start.y - origin.y;

	}
	final static Color shape = 		   new Color(0.2f, 0.8f, 0.2f, 0.6f);
	final static Color shapeSelected = new Color(0.8f, 0.2f, 0.2f, 0.6f);
	final static Color mightSelected = new Color(0.8f,0.5f,0.2f,0.6f);
	Color current = shape;
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(!isEnded()) return;
		
		NPhysics.currentStage.shapefill.setColor(isLastSelected() ? shapeSelected : current);
		
		Util.renderPolygon(NPhysics.currentStage.shapefill, points, indexes);
		
		NPhysics.currentStage.shapefill.setColor(Color.GRAY);
		NPhysics.currentStage.shapefill.circle(polygonMassCenter.x, polygonMassCenter.y, 5);
		
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
		xaxis.setPosition(new PositionVector(X,Y - axisMargin), new PositionVector(width,Y - axisMargin));
		yaxis.setPosition(new PositionVector(X - axisMargin,Y), new PositionVector(X - axisMargin,height));
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
		
		polygonMassCenter.set(definition.getCenter(false));
		
		
	}
	
	private void createArrow() {
		
		if(gravityArrow == null) {
			
			Vector2 start = new Vector2(polygonMassCenter).sub(getPosition());
			gravityArrow = new SimpleArrow(start, new Vector2(start).add(0,hitboxPolygon.area() * Util.UNIT * -9.8f / SimulationStage.ForceMultiplier / (30*30)));
			addActor(gravityArrow);
			gravityArrow.setColor(Color.BLUE);
		}else {
			
			Vector2 start = new Vector2(polygonMassCenter).sub(getPosition());
			gravityArrow.setStart(start);
			gravityArrow.setEnd(new Vector2(start).add(0,hitboxPolygon.area() * Util.UNIT * -9.8f / SimulationStage.ForceMultiplier / (30*30)));
			gravityArrow.updateVertexArray();
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
		createHitBox();
		createArrow();
		xaxis = new DoubleArrow(new PositionVector(X,Y - axisMargin), new PositionVector(width,Y - axisMargin));
		yaxis = new DoubleArrow(new PositionVector(X - axisMargin,Y), new PositionVector(X - axisMargin,height));
		
		addActor(xaxis);
		addActor(yaxis);
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
		if(!isEnded())return;
		triangulate();

		createDefinition();
		calculateHitBox();
		createArrow();
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return isEnded() ? points : null;
	}
	
	
	public void addComponent(ObjectChildren child) {
		
		components.add(child);
	}
	
	public void removeComponent(ObjectChildren child) {
		
		components.remove(child);
	}
	
	@Override
	public boolean remove() {
		
		polygonlist.remove(this);
		for (Point p : points) {
			p.remove();
		}
		
		for (Segment s : segments) {
			s.remove();
		}
		return super.remove();
	}
	
	public Vector2 getPosition() {return new Vector2(getX(), getY());}
}
