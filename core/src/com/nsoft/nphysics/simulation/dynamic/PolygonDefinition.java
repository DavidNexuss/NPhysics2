package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class PolygonDefinition extends ObjectDefinition{

	public ArrayList<PositionVector> vertices = new ArrayList<>();
	public ArrayList<Integer> indexes = new ArrayList<>();
	
	private float[] rawVertices;
	
	public void init() {
		
		dupeRawVertices();
	}
	
	private void dupeRawVertices() {
		
		rawVertices = new float[vertices.size()*2];
		
		for (int i = 0; i < rawVertices.length; i+=2) {
			
			rawVertices[i] = vertices.get(i/2).x;
			rawVertices[i + 1] = vertices.get(i/2).y;
		}
	}
	
	public float[] getRawPhysicalVertices() {
		
		float[] rawPhysicalVertices = new float[rawVertices.length];
		
		for (int i = 0; i < rawPhysicalVertices.length; i++) {
			
			rawPhysicalVertices[i] = rawVertices[i] / Util.METERS_UNIT();
		}
		
		return rawPhysicalVertices;
	}
	public float[] getRawVertices() {return rawVertices;}
	public float[][] getTriangles(boolean relative,boolean PhysValue){
		
		float[][] Buff = new float[indexes.size()/3][6];
		
		for (int i = 0; i < indexes.size(); i+=3) {
			
			Buff[i/3] = getTriangle(indexes.get(i), indexes.get(i + 1), indexes.get(i + 2), relative, PhysValue);
		}
		
		return Buff;
	}
	public float[] getTriangle(int i1,int i2,int i3,boolean relative,boolean PhysValue) {
		
		float[] buff = new float[6];
		if(relative) {
			
			Vector2 v = getCenter(false);
			
			buff[0] = getX(i1) - v.x;
			buff[1] = getY(i1) - v.y;
			buff[2] = getX(i2) - v.x;
			buff[3] = getY(i2) - v.y;
			buff[4] = getX(i3) - v.x;
			buff[5] = getY(i3) - v.y;
		}else {
			
			buff[0] = getX(i1);
			buff[1] = getY(i1);
			buff[2] = getX(i2);
			buff[3] = getY(i2);
			buff[4] = getX(i3);
			buff[5] = getY(i3);
		}
		
		if(PhysValue) {
			
			for (int i = 0; i < buff.length; i++) {
				
				buff[i] /= Util.METERS_UNIT();
			}
		}
		
		return buff;
	}
	
	@Override
	public Vector2 getCenter(boolean PhysValue) {
		
		return compute2DPolygonCentroid(PhysValue);
	}
	
	private Vector2 compute2DPolygonCentroid(boolean physValue)
	{
		Vector2 centroid = new Vector2();
	    
		double signedArea = 0.0;
	    double x0 = 0.0; // Current vertex X
	    double y0 = 0.0; // Current vertex Y
	    double x1 = 0.0; // Next vertex X
	    double y1 = 0.0; // Next vertex Y
	    double a = 0.0;  // Partial signed area

	    // For all vertices
	  
	    for (int i = 0 ; i < vertices.size(); i++)
	    {
	        x0 = vertices.get(i).x;
	        y0 = vertices.get(i).y;
	        x1 = vertices.get((i + 1) % vertices.size()).x;
	        y1 = vertices.get((i + 1) % vertices.size()).y;
	        a = x0*y1 - x1*y0;
	        signedArea += a;
	        centroid.x += (x0 + x1)*a;
	        centroid.y += (y0 + y1)*a;
	    }

	    signedArea *= 0.5;
	    centroid.x /= (6.0*signedArea);
	    centroid.y /= (6.0*signedArea);

	    return physValue ? centroid.scl(1f/Util.METERS_UNIT()) : centroid;
	}
	public float getY(int index) {
		
		return get(index).y;
	}
	public float getX(int index) {
		
		return get(index).x;
	}
	
	public PositionVector get(int index) {
		
		return vertices.get(index);
	}
	
	@Override
	public ArrayList<Fixture> createFixtures(Body b) {
		
		float[][] vert = getTriangles(true, true);
		
		ArrayList<Fixture> fixtures = new ArrayList<>();
		
		for (int i = 0; i < vert.length; i++) {
			
			FixtureDef fdef = createFixtureDefinition();
			
			PolygonShape shape = new PolygonShape();
			shape.set(vert[i]);
			fdef.shape = shape;
			b.createFixture(fdef);
		}
		
		for (int i = 0; i < physicalVertx.length; i++) {
			if(i % 2 == 0)physicalVertx[i] -= b.getPosition().x;
			else physicalVertx[i] -= b.getPosition().y;
		}
		return fixtures;
	}
	
	float[][] vert;
	float[][] buff;
	
	float[] physicalVertx;
	public float[] physicalVertxproj;
	@Override
	protected void initForSimulation() {
		vert = getTriangles(true, true);
		buff = new float[vert.length][6];
		physicalVertx = getRawPhysicalVertices();
		for (int i = 0; i < physicalVertx.length; i++) {
			System.out.println(physicalVertx[i]);
		}
		physicalVertxproj = new float[physicalVertx.length];
	}
	
	public float[] updateVertxArray(Vector2 pos,float anglerad) {
		
		for (int i = 0; i < physicalVertx.length; i+=2) {
			
			float x = physicalVertx[i];
			float y = physicalVertx[i+1];
			
			physicalVertxproj[i] = Util.rotx(x, y, anglerad) + pos.x;
			physicalVertxproj[i+1] = Util.roty(x, y, anglerad) + pos.y;
			
		}
		
		return physicalVertxproj;
	}
	
	final Vector2 t1 = new Vector2();
	final Vector2 t2 = new Vector2();
	final Vector2 t3 = new Vector2();
	@Override
	protected void render(Body b) {
		
		for (int i = 0; i < vert.length; i++) {
			

			Vector2 pos = b.getPosition().scl(Util.METERS_UNIT());
			
			t1.set(vert[i][0], vert[i][1]).scl(Util.METERS_UNIT()).add(pos);
			t2.set(vert[i][2],vert[i][3]).scl(Util.METERS_UNIT()).add(pos);
			t3.set(vert[i][4], vert[i][5]).scl(Util.METERS_UNIT()).add(pos);
		
			
			t1.set(Util.rotPivot(pos, t1, b.getAngle()));
			t2.set(Util.rotPivot(pos, t2, b.getAngle()));
			t3.set(Util.rotPivot(pos, t3, b.getAngle()));

			NPhysics.currentStage.shapefill.triangle(t1.x, t1.y, t2.x, t2.y, t3.x, t3.y);
			
		}
	}
}
