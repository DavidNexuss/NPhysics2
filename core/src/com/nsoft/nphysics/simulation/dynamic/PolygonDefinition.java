package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;

public class PolygonDefinition {

	public ArrayList<PositionVector> vertices = new ArrayList<>();
	public ArrayList<Integer> indexes = new ArrayList<>();
	public BodyType type = BodyType.DynamicBody;
	
	public float density = 1f;
	public float friction;
	public float restitution;
	
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
		return new PositionVector(sumx/vertices.size(), sumy/vertices.size());
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
