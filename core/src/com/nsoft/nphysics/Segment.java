package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Segment extends Group implements Parent<Point>{

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
		
		updateAll();
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
	
	public float getLenght() {
		
		return getVector().len();
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
		
		super.draw(batch, parentAlpha);
	}
	
	//----------------------------INPUT----------------------
	
	float[] vertexArray = new float[8];
	private float modulus;
	static float LW = 5;  //Width of the line hitbox
	
	public void updateVertexArray() {
		
		vertexArray[0] = 0;
		vertexArray[1] = LW;
		
		vertexArray[2] = 0;
		vertexArray[3] = -LW;
		
		vertexArray[4] = modulus;
		vertexArray[5] = LW;
		
		vertexArray[6] = modulus;
		vertexArray[7] = -LW;
		
		Util.rot(vertexArray, getVector().angleRad());
		
		setX(A.getX());
		setY(A.getY());
		setSize(B.getX() - A.getX(), B.getY() - A.getY());
	}
	
	//---------------------------END-INPUT-------------------
	public void select() {
		
		if(selected != null) {
			

			selected.Axis.hide();
			selected.Arc.hide();
		}
		
		selected = this;
		Axis.show();
		Arc.show();
		
		
	}
	
	public float getModulus() {return modulus;}
	public void updateModulus() {modulus = getLenght();}
	
	private void updateArc(){
		
		Arc.setAngle(getVector().angle());
		Arc.setRadius(modulus);
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		updateAll();
	}

	private void updateAll() {
		
		updateModulus();
		updateArc();
		updateVertexArray();
	}
	@Override
	public ArrayList<Point> getChildList() {
		
		return lst;
	}
}
