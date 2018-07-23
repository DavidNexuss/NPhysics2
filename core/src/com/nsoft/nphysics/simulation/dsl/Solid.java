package com.nsoft.nphysics.simulation.dsl;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Solid {

	Vertex[] v;
	ArrayList<Force> extraForces = new ArrayList<>();
	public Solid(Vector2 ... a) {
		
		v = new Vertex[a.length];
		for (int i = 0; i < a.length; i++) {
			
			v[i] = new Vertex(a[i], this);
		}
	}
	
	public Vertex closestPoint(Vector2 a) {
		
		float dist = Float.MAX_VALUE;
		Vertex current = null;
		for (int i = 0; i < v.length; i++) {
			
			if(a.dst2(v[i].getPosition()) < dist) {
				current = v[i];
				dist = a.dst2(v[i].getPosition());
			}
		}
		
		return current;
	}
}
