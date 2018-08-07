package com.nsoft.nphysics.sandbox;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Draggable;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;

public class Point extends Actor implements ClickIn, Position,Removeable, Draggable{


	public interface ParentCall{
		
		public void run(Parent<Point> p);
	}
	static final Color point = new Color(0.2f, 0.4f, 0.2f, 1f);
	static final Color pointselected = new Color(0.9f, 0.8f, 0.2f, 1f);
	static final Color tempColor = new Color(0.2f, 0.8f, 0.2f, 0.2f);

	public static ArrayList<Point> allpoints = new ArrayList<>();
	
	public static int RADIUS = 5;
	
	public static int INPUT_RADIUS = RADIUS*3;
	
	static int RADIUS2 = RADIUS*RADIUS;
	
	boolean isTemp;
	
	private ArrayList<Parent<Point>> objectsParent = new ArrayList<>();
	
	static int pointCounter = 0;
	
	static Point lastPoint = new Point(Float.NaN, Float.NaN, true);
	
	float originx,originy; //USED IN POLYGONACTOR DRAG ONLY
	
	public Vector2 initial;
	
	
	public Point(float x,float y,boolean isTemp) {
		
		setX(x);
		setY(y);
		setSize(INPUT_RADIUS, INPUT_RADIUS);
		this.isTemp = isTemp;
		addInput();
		addDragListener();
		pointCounter ++;
		allpoints.add(this);
	}

	public static Point getPoint(float x,float y) {
		
		Point p = isThereAPoint(x, y);
		if(p == null) {
			
			p = new Point(x, y, false);
			NPhysics.currentStage.addActor(p);
		}
		
		return p;
	}
	public static Point isThereAPoint(float x,float y) {
		
		for (Point a : allpoints) {
			
			if(Math.abs(a.getX() - x) < Point.INPUT_RADIUS && Math.abs(a.getY() - y) < Point.INPUT_RADIUS) {
				
				return a;
			}
		}
		
		return null;
		
	}
	private Color current = point;
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		NPhysics.currentStage.shapefill.setColor(isTemp ? tempColor: current);
		NPhysics.currentStage.shapefill.setColor(current);
		
		if(getHandler().isSelected(this))NPhysics.currentStage.shapefill.circle(getX(), getY(), RADIUS*1.4f);
		else {
			
			if(!hit)NPhysics.currentStage.shapefill.circle(getX(), getY(), RADIUS);
			else NPhysics.currentStage.shapefill.circle(getX(), getY(), RADIUS*1.3f);
		}
	}

	public Parent getObjectParent() {return getObjectParent(0);}
	public Parent getObjectParent(int index) {return objectsParent.get(index);}
	
	public ArrayList<Parent<Point>> getObjectParentList(Class<?> clas){
		
		ArrayList<Parent<Point>> parent = new ArrayList<>();
		
		for (Parent<Point> p : objectsParent) {
			
			if(clas.isInstance(p)) parent.add(p);
		}
		return parent;
	}
	public boolean hasObjectParent() {return hasObjectParent(Parent.class);}
	public boolean hasObjectParent(Class<?> clas) { 
		
		if(objectsParent.isEmpty()) return false;
		
		for (Parent p : objectsParent) {
			
			if(Util.isInstance(clas, p)) return true;
		}
		
		return false;
	}
	
	
	public void removeObjectParent(Parent oldParent) {
		
		int v = -1;
		for (int i = 0; i < objectsParent.size(); i++) {
			if(oldParent == objectsParent.get(i)) {
				
				if(v != -1) throw new IllegalStateException();
				v = i;
				break;
			}
		}
		
		if(v != -1) objectsParent.remove(v);
	}
	
	public void setObjectParent(Parent newParent) {addObjectParent(newParent);}
	public void addObjectParent(Parent newParent) {

		if(!objectsParent.contains(newParent)) objectsParent.add(newParent);
	}

	public void forEveryParent(ParentCall call) {
		
		for (Parent<Point> p : objectsParent) {
			
			call.run(p);
		}
	}
	
	public boolean isTemp() {return isTemp;}
	
	@Override public void setX(float x) { super.setX(x - INPUT_RADIUS/2f); updatePosition();}
	@Override public void setY(float y) { super.setY(y - INPUT_RADIUS/2f); updatePosition();}
	@Override public float getX() {return super.getX() + INPUT_RADIUS/2f;}
	@Override public float getY() {return super.getY() + INPUT_RADIUS/2f;}
	@Override public void setPosition(float x,float y) { setPosition(x, y, true);}
	public void setPosition(float x, float y,boolean update) { super.setPosition(x- INPUT_RADIUS/2f, y - INPUT_RADIUS/2f); if(update)updatePosition(); }
	
	//----------------------INPUT----------------------
	
	boolean hit;
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		
		hit =  x >= -INPUT_RADIUS/2f && x < getWidth() + INPUT_RADIUS/2f && y >= -INPUT_RADIUS/2f && y < getHeight() + INPUT_RADIUS/2f;
		return hit ? this : null;
	}
	
	public void updatePosition() {
		
		if(hasObjectParent())forEveryParent((p)->{p.updatePosition(getX(), getY(), this);});
	}

	@Override
	public boolean isInside(float x, float y) {
		
		float len2 = new Vector2(x - getX(), y - getY()).len2();
		return len2 < INPUT_RADIUS*INPUT_RADIUS;
	}
	
	@Override
	public void doDrag(boolean pool,float x,float y,InputEvent event) {
		
		if(!getHandler().isSelected(this)) getHandler().setSelected(this);
    	
    	if (NPhysics.currentStage.isSnapping()) {
    		
    		setPosition(Sandbox.snapGrid(getX()), Sandbox.snapGrid(getY()));
    		moveBy(Sandbox.snapGrid(x - getWidth() / 2),Sandbox.snapGrid( y - getHeight() / 2));
		}else {
			moveBy(x - getWidth() / 2, y - getHeight() / 2);
		}
    	
        updatePosition();
        Draggable.super.doDrag(pool, x, y,event);
		
	}
	//------------SEGMENT-CREATION--------------------------
	
	static Point A,B;
	
	private void createSegment(Point dis,boolean cont) {
		
		if(A == null) 
			
			A = this;
		else { 
			
			B = this;
			if(hasObjectParent(Segment.class) && getObjectParent().getChildList().contains(B) && getObjectParent().getChildList().contains(A)) return;
			
			Segment seg = new Segment(A, B);
			getStage().addActor(seg);

			if(cont) {
				
				A = B;
				B = null;
			}else {
				
				A = null;
				B = null;
			}
		}
	}
	
	@Override
	public void unselect() {
		
		current = point;
	}
	@Override
	public void select(int pointer) {

		current = pointselected;
		switch (GameState.current) {
		case CREATE_SEGMENT:
			
			createSegment(this, false);
			break;
		case CREATE_SEGMENTS:
			
			createSegment(this, true);
			break;
		case CREATE_POLYGON:
			
			if(PolygonActor.temp == null) PolygonActor.temp = new PolygonActor();
			
			PolygonActor.temp.addPoint(this);
			break;
		default:
			
			break;
		}
	}
	
	@Override
	public SelectHandle getHandler() {
		
		return Sandbox.mainSelect;
	}
	
	@Override
	public boolean remove() {
		
		if(hasObjectParent()) return false;
		return super.remove();
	}
	
	@Override
	public String toString() {

		return "Point: " + getVector().toString();
	}
}
