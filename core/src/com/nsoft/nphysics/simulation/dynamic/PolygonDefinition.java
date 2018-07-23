package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class PolygonDefinition {

	public ArrayList<PositionVector> vertices = new ArrayList<>();
	public ArrayList<ObjectChildren> childrens;
	public ArrayList<Integer> indexes = new ArrayList<>();
	public BodyType type = BodyType.DynamicBody;
	
	public float density = 1f;
	public float friction;
	public float restitution;
	
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
	public float[] getRawVertices() {return rawVertices;}
	public float[][] getTriangles(boolean relative,boolean PhysValue){
		
		float[][] Buff = new float[indexes.size()/3][6];
		
		for (int i = 0; i < indexes.size(); i+=3) {
			
			System.out.println(indexes.size());
			Buff[i/3] = getTriangle(indexes.get(i), indexes.get(i + 1), indexes.get(i + 2), relative, PhysValue);
		}
		
		return Buff;
	}
	public float[] getTriangle(int i1,int i2,int i3,boolean relative,boolean PhysValue) {
		
		float[] buff = new float[6];
		if(relative) {
			
			PositionVector v = getCenter(false);
			
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
				
				buff[i] /= Util.UNIT;
			}
		}
		
		return buff;
	}
	public float getCenterY(boolean PhysValue) {
		
		return getCenter(PhysValue).y;
	}
	public float getCenterX(boolean PhysValue) {
		
		return getCenter(PhysValue).x;
	}
	public PositionVector getCenter(boolean PhysValue) {
	/*	
		float sumx = 0;
		float sumy = 0;
		
		for (PositionVector v : vertices) {
			
			sumx+= v.x;
			sumy+= v.y;
		}
		
		if(PhysValue) {
			
			sumx /= Util.UNIT;
			sumy /= Util.UNIT;
		}
		return new PositionVector(sumx/vertices.size(), sumy/vertices.size());*/
		
		return compute2DPolygonCentroid(PhysValue);
	}
	
	public PositionVector compute2DPolygonCentroid(boolean physValue)
	{
		PositionVector centroid = new PositionVector(new Vector2());
	    
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

	    return physValue ? (PositionVector) centroid.scl(1f/Util.UNIT) : centroid;
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
}
