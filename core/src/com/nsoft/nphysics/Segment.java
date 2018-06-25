package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Segment extends AlphaActor implements Parent<Point>,ClickIn<Segment>{

	public static final float SHOW_DELAY = 1f;
	public static final float INPUT_EPSILON = 20f;
	
	private static ArrayList<Segment> segments = new ArrayList<Segment>();
	
	private Point A,B;
	private SimpleAxis Axis;
	private AngleArc Arc;
	
	public static Segment selected;
	
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
		
		A.setObjectParent(this);
		B.setObjectParent(this);
		
		Arc = new AngleArc(A);
		addActor(Arc);
	
		Axis = new SimpleAxis(A);
		addActor(Axis);
		
		setAlpha(0);
		updateAll();
		addInput();
		
		segments.add(this);
	}	

	
	//------------------GEOMETRY--------------------------

	private float modulus;
	
	public boolean isReady() {
		
		return (A!= null && !A.isTemp()) && (B != null && !B.isTemp());
	}
	
	private void notReady() { throw new IllegalStateException("The segment isn't ready.");}
	
	public float getModulus() {return modulus;}
	public void updateModulus() {modulus = getLenght();}
	
	public Vector2 getVector() {
		
		if(!isReady()) return null;
		else {
			
			return new Vector2(B.getX() - A.getX(), B.getY() - A.getY());
		}
	}
	
	public float getLenght() { 
		
		if(!isReady()) notReady();
		return getVector().len(); 
		
	}
	public float getSlope() {
		
		if(!isReady()) notReady();
		Vector2 v = getVector();
		return v.y/v.x;
	}
	
	public float getN() {
		
		if(!isReady()) notReady();
		return A.getY() - getSlope()*A.getX();
	}
	public void setPointA(Point p) {A = p; lst.set(0, A);}
	public void setPointB(Point p) {B = p; lst.set(1, B);}
	
	//--------------------------END-GEOMETRY-------------------------
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(isReady()) {
			
			if(!hit)Sandbox.shapeline.setColor(0.2f, 0.2f, 0.2f, 1);
			else Sandbox.shapeline.setColor(0.8f, 0.2f, 0.2f, 1);
			Sandbox.shapeline.begin(ShapeType.Line);
			Gdx.gl.glLineWidth(3 + 3*getAlpha());
			Sandbox.shapeline.line(A.getX(), A.getY(), B.getX(), B.getY());
			Sandbox.shapeline.end();
		}
		
		super.draw(batch, parentAlpha);
	}
	
	//------------------------FADE---------------------------
	
	
	public void show() {
		
		addAction(Actions.fadeIn(1f, Interpolation.exp5));
	}
	
	public void hide() {
		
		addAction(Actions.fadeOut(1f, Interpolation.exp5));
	}
	//----------------------------INPUT----------------------
	
	private static Vector2 temp = new Vector2();
	private boolean hit;
	@Override
	public Actor hit(float x, float y, boolean touchable) {

		if (touchable && getTouchable() == Touchable.disabled) return null;
		Actor[] childrenArray = getChildren().items;
		Vector2 point = temp;
		for (int i = getChildren().size - 1; i >= 0; i--) {
			Actor child = childrenArray[i];
			if (!child.isVisible()) continue;
			child.parentToLocalCoordinates(point.set(x, y));
			Actor hit = child.hit(point.x, point.y, touchable);
			if (hit != null) return hit;
		}
		
		boolean insideBox =  x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
		
		if(isInside(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(),INPUT_EPSILON*1.2f) && insideBox) {
			hit = true;
			return this;
		}else {
			hit = false;
			return null;
		}

	}
	@Override
	public boolean isInside(float x, float y) {
		
		if(isInside(x, y, INPUT_EPSILON)){
			
			return true;
		}else return false;
	}
	
	public boolean isInside(float x, float y,float epsilon) {
		
		
		float val = y - getSlope()*x - getN();
		return val < epsilon && val > -epsilon;
	}
	
	public boolean isSelected() {return this == selected;}
	public void unselect() {
		
		if(isSelected()) {
			
			Axis.hide();
			Arc.hide();
			hide();
			selected = null;
		}
	}
	@Override
	public void select() {
		
		if(isSelected()) {
			
			unselect();
			return;
		}
		if(selected != null) {
			

			selected.unselect();
		}
		

		selected = this;
		Axis.show();
		Arc.show();
		show();
		
		
	}
	
	
	//---------------------------END-INPUT-------------------
	
	//--------------UPDATE--------------------
	
	private void updateHitBox() {
		
		setX(A.getX());
		setY(A.getY());
		setSize(B.getX() - A.getX(), B.getY() - A.getY());
	}
	

	private void updateArc(){
		
		Arc.setAngle(getVector().angle());
		Arc.setRadius(modulus);
		Arc.update();
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		updateAll();
	}

	private void updateAll() {
		
		updateModulus();
		updateArc();
		updateHitBox();
	}
	@Override
	public ArrayList<Point> getChildList() {
		
		return lst;
	}

}
