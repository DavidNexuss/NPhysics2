package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class Point extends Actor implements ClickIn<Point>, Position{

	public static int RADIUS = 8;
	
	public static int INPUT_RADIUS = RADIUS*3;
	
	static int RADIUS2 = RADIUS*RADIUS;
	
	boolean isTemp;
	private Parent parent;
	
	static Point lastPoint = new Point(Float.NaN, Float.NaN, true);
	static Point selected;
	static int pointCounter = 0;
	
	public Point(float x,float y,boolean isTemp) {
		
		setX(x);
		setY(y);
		setSize(INPUT_RADIUS, INPUT_RADIUS);
		this.isTemp = isTemp;
		addInput();
		addDragListener();
		pointCounter ++;
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(isTemp) 
			Sandbox.shapefill.setColor(0.2f, 0.8f, 0.2f, 0.2f);
		else {
			
			if(selected == this)Sandbox.shapefill.setColor(0.9f, 0.8f, 0.2f, 1f);
			else Sandbox.shapefill.setColor(0.2f, 0.4f, 0.2f, 1f);
		} 
		
		if(A == this)Sandbox.shapefill.circle(getX(), getY(), 5*1.2f);
		else Sandbox.shapefill.circle(getX(), getY(), 5);
	}
	
	public Parent getObjectParent() {return parent;}
	public boolean hasObjectParent() { return parent != null;}
	public void setObjectParent(Parent newParent) {parent = newParent;}
	
	public boolean isTemp() {return isTemp;}
	
	@Override public void setX(float x) { super.setX(x - INPUT_RADIUS/2f); updatePosition();}
	@Override public void setY(float y) { super.setY(y - INPUT_RADIUS/2f); updatePosition();}
	@Override public float getX() {return super.getX() + INPUT_RADIUS/2f;}
	@Override public float getY() {return super.getY() + INPUT_RADIUS/2f;}
	@Override public void setPosition(float x, float y) { super.setPosition(x, y); updatePosition(); }
	
	//----------------------INPUT----------------------
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		return x >= -INPUT_RADIUS/2f && x < getWidth() + INPUT_RADIUS/2f && y >= -INPUT_RADIUS/2f && y < getHeight() + INPUT_RADIUS/2f ? this : null;
	}
	
	public void updatePosition() {
		
		if(hasObjectParent())
			parent.updatePosition(getX(), getY(), this);
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
		    	selected = dis;
		        moveBy(x - getWidth() / 2, y - getHeight() / 2);
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
			if(hasObjectParent() && getObjectParent().getChildList().contains(B) && getObjectParent().getChildList().contains(A)) return;
			
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
	public void select() {
		
		selected = this;
		switch (GameState.current) {
		case CREATE_SEGMENT:
			
			createSegment(this, false);
			break;
		case CREATE_SEGMENTS:
			
			createSegment(this, true);
			break;

		default:
			
			selected = this;
			break;
		}
	}
}
