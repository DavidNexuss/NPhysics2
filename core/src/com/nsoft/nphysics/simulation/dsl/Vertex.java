package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;

public class Vertex {

	private Vector2 position;
	private Solid solid;
	
	public Vertex(Vector2 pos,Solid sol) {
		
		position = pos;
		solid = sol;
	}
	
	public Vector2 getPosition() {return position;}
	public Solid getSolid() {return solid;}
}
