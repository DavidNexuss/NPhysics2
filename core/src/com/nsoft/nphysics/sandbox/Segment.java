package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class Segment extends Group implements Parent<Point>,ClickIn,Showable{

	public static final float SHOW_DELAY = 1f;
	public static final float INPUT_EPSILON = 20f;
	public static final float DETECT_EPSILON = 0.1f;
	private static ArrayList<Segment> segments = new ArrayList<Segment>();
	
	private Point A;
	private Point B;
	private SimpleAxis Axis;
	private AngleArcActor Arc;
	
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
		
		//setDebug(true);
		lst.set(0, A);
		lst.set(1, B);
		
		A.setSegmentParent(this);
		B.setSegmentParent(this);
		
		Arc = new AngleArcActor(A,this);
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
	
	final static Color line = new Color(0.2f, 0.2f, 0.2f, 1);
	final static Color Cselected = new Color(0.8f, 0.2f, 0.2f, 1);
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if(isReady()) {
			
			if(!hit)Sandbox.shapeline.setColor(line);
			else Sandbox.shapeline.setColor(Cselected);
			Sandbox.shapeline.begin(ShapeType.Line);
			Gdx.gl.glLineWidth(3 + 3*getAlpha());
			Sandbox.shapeline.line(A.getX(), A.getY(), B.getX(), B.getY());
			Sandbox.shapeline.end();
			
			Sandbox.shapefill.setColor(0.8f, 0.2f, 0.2f, 0.3f);
			
			//RENDER HITBOX TRIANGLES
			/*
			Sandbox.shapefill.triangle(trianglesBuffer[0][0], trianglesBuffer[0][1], 
									   trianglesBuffer[0][2], trianglesBuffer[0][3], 
									   trianglesBuffer[0][4], trianglesBuffer[0][5]);
			
			Sandbox.shapefill.triangle(trianglesBuffer[1][0], trianglesBuffer[1][1], 
					   				   trianglesBuffer[1][2], trianglesBuffer[1][3], 
					   				   trianglesBuffer[1][4], trianglesBuffer[1][5]);
			
			Sandbox.shapefill.triangle(trianglesBuffer[2][0], trianglesBuffer[2][1], 
					   				   trianglesBuffer[2][2], trianglesBuffer[2][3], 
					   				   trianglesBuffer[2][4], trianglesBuffer[2][5]);
			
			Sandbox.shapefill.triangle(trianglesBuffer[3][0], trianglesBuffer[3][1], 
	   				   				   trianglesBuffer[3][2], trianglesBuffer[3][3], 
	   				   				   trianglesBuffer[3][4], trianglesBuffer[3][5]);*/
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
	boolean hit;
	
	private final float[][] trianglesBuffer = new float[4][6];
	
	public Actor hitChildren(float x,float y,boolean touchable) {
		
		Actor[] childrenArray = getChildren().items;
		Vector2 point = temp;
		for (int i = getChildren().size - 1; i >= 0; i--) {
			Actor child = childrenArray[i];
			if (!child.isVisible()) continue;
			child.parentToLocalCoordinates(point.set(x, y));
			Actor hit = child.hit(point.x, point.y, touchable);
			if (hit != null) return hit;
		}
		
		return null;
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {

		if (touchable && getTouchable() == Touchable.disabled) return null;
		
		Actor p = hitChildren(x,y,touchable);
		if(p != null) return p;
		
		
		float PX = Gdx.input.getX();
		float PY = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		float AX = A.getX();
		float AY = A.getY();
		
		boolean insideBox = false;
		
		if(getWidth() != 0) {
			
			trianglesBuffer[0][0] = PX;
			trianglesBuffer[0][1] = PY;
			trianglesBuffer[1][0] = PX;
			trianglesBuffer[1][1] = PY;
			trianglesBuffer[2][0] = PX;
			trianglesBuffer[2][1] = PY;
			trianglesBuffer[3][0] = PX;
			trianglesBuffer[3][1] = PY;
			
			trianglesBuffer[0][2] = AX;
			trianglesBuffer[0][3] = AY;
			trianglesBuffer[0][4] = AX + getWidth();
			trianglesBuffer[0][5] = AY;
			
			trianglesBuffer[1][2] = AX;
			trianglesBuffer[1][3] = AY;
			trianglesBuffer[1][4] = AX;
			trianglesBuffer[1][5] = AY + getHeight();
			
			trianglesBuffer[2][2] = AX;
			trianglesBuffer[2][3] = AY + getHeight();
			trianglesBuffer[2][4] = AX + getWidth();
			trianglesBuffer[2][5] = AY + getHeight();
			
			trianglesBuffer[3][2] = AX + getWidth();
			trianglesBuffer[3][3] = AY + getHeight();
			trianglesBuffer[3][4] = AX + getWidth();
			trianglesBuffer[3][5] = AY;
			
			float T1 = Util.triangleArea(trianglesBuffer[0]);
			float T2 = Util.triangleArea(trianglesBuffer[1]);
			float T3 = Util.triangleArea(trianglesBuffer[2]);
			float T4 = Util.triangleArea(trianglesBuffer[3]);
			
			float A = Math.abs(getWidth() * getHeight()) - T1 - T2 - T3 - T4;
			
			insideBox = A < DETECT_EPSILON && A > -DETECT_EPSILON;
		}else {
			
			insideBox = x > INPUT_EPSILON && x < -INPUT_EPSILON && y > A.getY() && y < getHeight();
		}
		
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
		
		Actor hit = hitChildren(x - A.getX(), y - A.getY(), true);
		if (hit != null) {
			return false;
		}
		float slope = getSlope();
		float n = getN();
		
		float val = epsilon*10;
		if(getWidth() == 0) {
			
			val = A.getX() - x;
		}
		else val = (float) (Math.abs(slope*x - y + n)/Math.sqrt(Math.pow(slope, 2) + 1));
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
