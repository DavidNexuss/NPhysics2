package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class RopeComponent extends RawJoint implements Parent<Point>{

	private Point anchorA,anchorB;
	private ArrayList<Point> anchors = new ArrayList<>();
	
	private boolean complete;
	
	public static RopeComponent temp;
	
	public RopeComponent() {
		
		defaultInit();
		setColor(Color.BLACK);
	}
	
	public boolean addAnchor(Point p) {
		
		if(anchorA == null) setAnchorAPoint(p); else
		if(anchorB == null) setAnchorBPoint(p); else return false;
		
		return true;
	}
	public void setAnchorAPoint(Point A) {
		if(anchorA != null) anchorA.removeObjectParent(this);
		anchorA = A;
		anchorA.setObjectParent(this);
		System.out.println("Anchor A: " + A);
		end();
	}
	
	public void setAnchorBPoint(Point B) {
		if(anchorB != null) anchorB.removeObjectParent(this);
		anchorB = B;
		anchorB.setObjectParent(this);
		System.out.println("Anchor B: " + B);
		end();
	}

	public Point getAnchorA() {
		
		return anchorA;
	}
	
	public Point getAnchorB() {
		
		return anchorB;
	}
	
	private void end() {
		
		anchors.clear();
		anchors.add(anchorA);
		anchors.add(anchorB);
		complete = !(anchorA == null || anchorB == null);
	}
	
	public Vector2 getRopeVector() {
		
		if(!complete) return null;
		return new Vector2(anchorB.getVector()).sub(anchorA.getVector());
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(!complete) return;
		
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		NPhysics.currentStage.shapeline.setColor(getColor());
		NPhysics.currentStage.shapeline.line(anchorA.getX(), anchorA.getY(), anchorB.getX(), anchorB.getY());
		NPhysics.currentStage.shapeline.end();
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
	}

	@Override
	public ArrayList<Point> getChildList() {
		return anchors;
	}
	
}
