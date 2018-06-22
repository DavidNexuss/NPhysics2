package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Segment extends Actor implements Parent<Point>{

	private Point A,B;
	private ArrayList<Point> lst = new ArrayList<>();
	{
		lst.add(null);
		lst.add(null);
	}
	public Segment() {}
	
	public Segment(Point A,Point B) {
		
		this.A = A;
		this.B = B;
		
		lst.set(0, A);
		lst.set(1, B);
	}	

	public boolean isReady() {
		
		return (A!= null && !A.isTemp()) && (B != null && !B.isTemp());
	}
	
	public Vector2 getVector() {
		
		if(!isReady()) return null;
		else {
			
			return new Vector2(B.getX() - A.getX(), B.getY() - A.getY());
		}
	}
	public void setPointA(Point p) {A = p; lst.set(0, A);}
	public void setPointB(Point p) {B = p; lst.set(1, B);}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(isReady()) {
			
			Sandbox.shapeline.begin(ShapeType.Line);
			Gdx.gl.glLineWidth(3);
			Sandbox.shapeline.line(A.getX(), A.getY(), B.getX(), B.getY());
			Sandbox.shapeline.end();
		}
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		
	}

	@Override
	public ArrayList<Point> getChildList() {
		
		return lst;
	}
}
