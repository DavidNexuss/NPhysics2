package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Point extends Actor{

	boolean isTemp;
	private Parent parent;
	
	static Point lastPoint = new Point(Float.NaN, Float.NaN, true);
	static Point selected;
	static int pointCounter = 0;
	
	public Point(float x,float y,boolean isTemp) {
		
		setX(x);
		setY(y);
		setSize(5, 5);
		this.isTemp = isTemp;
		pointCounter ++;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(isTemp) 
			Sandbox.shapefill.setColor(0.2f, 0.8f, 0.2f, 0.2f);
		else Sandbox.shapefill.setColor(0.2f, 0.8f, 0.2f, 1f);
		
		Sandbox.shapefill.circle(getX(), getY(), 5);
	}
	
	public Parent getObjectParent() {return parent;}
	public boolean hasObjectParent() { return parent != null;}
	public void setObjectParent(Parent newParent) {parent = newParent;}
	
	@Override public void setX(float x) { super.setX(x); updatePosition();}
	@Override public void setY(float y) { super.setY(y); updatePosition();}
	@Override public void setPosition(float x, float y) { super.setPosition(x, y); updatePosition(); }
	
	public void updatePosition() {
		
		if(hasObjectParent())
			parent.updatePosition(getX(), getY(), this);
	}
}
