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
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;

public class Point extends Actor implements ClickIn, Position,Removeable{


	static final Color point = new Color(0.2f, 0.4f, 0.2f, 1f);
	static final Color pointselected = new Color(0.9f, 0.8f, 0.2f, 1f);
	static final Color tempColor = new Color(0.2f, 0.8f, 0.2f, 0.2f);
	
	public static int RADIUS = 5;
	
	public static int INPUT_RADIUS = RADIUS*3;
	
	static int RADIUS2 = RADIUS*RADIUS;
	
	boolean isTemp;
	
	private Parent segmentParent;
	private Parent polygonParent;
	
	static int pointCounter = 0;
	
	static Point lastPoint = new Point(Float.NaN, Float.NaN, true);
	
	public Point(float x,float y,boolean isTemp) {
		
		setX(x);
		setY(y);
		setSize(INPUT_RADIUS, INPUT_RADIUS);
		this.isTemp = isTemp;
		addInput();
		addDragListener();
		pointCounter ++;
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

	public Parent getPolygonParent() {return polygonParent;}
	public boolean hasPolygonParent() { return polygonParent != null;}
	public void setPolygonParent(Parent newParent) {polygonParent = newParent;}
	
	public Parent getSegmentParent() {return segmentParent;}
	public boolean hasSegmentParent() { return segmentParent != null;}
	public void setSegmentParent(Parent newParent) {segmentParent = newParent;}
	
	public boolean isTemp() {return isTemp;}
	
	@Override public void setX(float x) { super.setX(x - INPUT_RADIUS/2f); updatePosition();}
	@Override public void setY(float y) { super.setY(y - INPUT_RADIUS/2f); updatePosition();}
	@Override public float getX() {return super.getX() + INPUT_RADIUS/2f;}
	@Override public float getY() {return super.getY() + INPUT_RADIUS/2f;}
	@Override public void setPosition(float x, float y) { super.setPosition(x- INPUT_RADIUS/2f, y - INPUT_RADIUS/2f); updatePosition(); }
	
	//----------------------INPUT----------------------
	
	boolean hit;
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		
		hit =  x >= -INPUT_RADIUS/2f && x < getWidth() + INPUT_RADIUS/2f && y >= -INPUT_RADIUS/2f && y < getHeight() + INPUT_RADIUS/2f;
		return hit ? this : null;
	}
	
	public void updatePosition() {
		
		if(hasPolygonParent())
			polygonParent.updatePosition(getX(), getY(), this);
		if(hasSegmentParent())
			segmentParent.updatePosition(getX(), getY(), this);
	}

	@Override
	public boolean isInside(float x, float y) {
		
		float len2 = new Vector2(x - getX(), y - getY()).len2();
		return len2 < INPUT_RADIUS*INPUT_RADIUS;
	}

	private void addDragListener() {
		
		Point dis = this;
		addListener(new DragListener() {
		    public void drag(InputEvent event, float x, float y, int pointer) {
		    	
		    	if(!getHandler().isSelected(dis)) getHandler().setSelected(dis);
		    	if (NPhysics.currentStage.isSnapping()) {
		    		
		    		setPosition(Sandbox.snapGrid(getX()), Sandbox.snapGrid(getY()));
		    		moveBy(Sandbox.snapGrid(x - getWidth() / 2),Sandbox.snapGrid( y - getHeight() / 2));
				}else {
					moveBy(x - getWidth() / 2, y - getHeight() / 2);
				}
		    	
		        updatePosition();
		    }
		});
	}
	//------------SEGMENT-CREATION--------------------------
	
	static Point A,B;
	
	private void createSegment(Point dis,boolean cont) {
		
		if(A == null) 
			
			A = this;
		else { 
			
			B = this;
			if(hasSegmentParent() && getSegmentParent().getChildList().contains(B) && getSegmentParent().getChildList().contains(A)) return;
			
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
		
		if(hasPolygonParent()) return false;
		if(hasSegmentParent()) return false;
		return super.remove();
	}
}
