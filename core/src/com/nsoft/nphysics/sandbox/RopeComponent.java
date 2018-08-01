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

public class RopeComponent extends Actor implements RawJoint, Parent<Point>{

	private PolygonActor A,B;
	private Point anchorA,anchorB;
	private ArrayList<Point> anchors = new ArrayList<>();
	
	private boolean complete;
	
	public static RopeComponent temp;
	
	public RopeComponent() {
		
		A = (PolygonActor) Sandbox.mainSelect.getSelecteds().get(0);
		B = (PolygonActor) Sandbox.mainSelect.getSelecteds().get(1);
		
		setColor(Color.BLACK);
	}
	
	public boolean addAnchor(Point p) {
		
		if(anchorA == null) setAnchorAPoint(p); else
		if(anchorB == null) setAnchorBPoint(p); else return false;
		
		return true;
	}
	public void setAnchorAPoint(Point A) {
		if(anchorA != null) anchorA.setObjectParent(null);
		anchorA = A;
		anchorA.setObjectParent(this);
		end();
	}
	
	public void setAnchorBPoint(Point B) {
		if(anchorB != null) anchorB.setObjectParent(null);
		anchorB = B;
		anchorB.setObjectParent(this);
		end();
	}

	public Point getAnchorA() {
		
		return anchorA;
	}
	
	public Point getAnchorB() {
		
		return anchorB;
	}
	
	public PolygonActor getPolygonA() {
		
		return A;
	}
	
	public PolygonActor getPolygonB() {
		
		return B;
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
