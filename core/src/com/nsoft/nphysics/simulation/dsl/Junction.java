package com.nsoft.nphysics.simulation.dsl;

public class Junction {

	private Force ROX,ROY;
	private Vertex pos;
	private Solid sol;
	
	public Junction(Vertex pos, Solid sol) {
		
		this.pos = pos;
		this.sol = sol;
	}
	
	public Force getROX() { return ROX; }
	public Force getROY() { return ROY; }

	public Vertex getPos() { return pos; }

	public Solid getSol() { return sol; }
	
	
}
