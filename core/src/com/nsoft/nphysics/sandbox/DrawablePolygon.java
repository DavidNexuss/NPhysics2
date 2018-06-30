package com.nsoft.nphysics.sandbox;

public class DrawablePolygon {

	private float[][] vertices; 
	private float[][] buffervertices;
	
	private boolean isComplete = false;
	
	public float xoffset,yoffset;
	public float anglerad;
	
	private float vertexcount;
	
	public DrawablePolygon(int vertexcount) {
	
		this.vertexcount = vertexcount;
		createArrays(vertexcount);
	}
	
	private void createArrays(int vertexcount) {
		
		vertices = new float[vertexcount][2];
		buffervertices = new float[vertexcount][2];
	}
	
	private void setVertex(int index,float x,float y) {
		
		if(isComplete) throw new IllegalStateException();
		vertices[index][0] = x;
		vertices[index][1] = y;
	}
	
	private void complete() {
		
		if(isComplete) throw new IllegalStateException();
		isComplete = true;
	}
	
	private void update() {
		
		for (int i = 0; i < buffervertices.length; i++) {
			
		}
	}
}
