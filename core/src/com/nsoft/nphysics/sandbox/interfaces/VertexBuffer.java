package com.nsoft.nphysics.sandbox.interfaces;

import com.nsoft.nphysics.NPhysics;

public interface VertexBuffer {

	void createVertexArray();
	void updateVertexArray();
	
	float[][] getVertexBuffer();
	
	default float[][] createEmptyVertexArray(int size){
		
		return new float[size][2];
	}
	default float[] getVertex(int index) {
		
		return getVertexBuffer()[index];
	}
	
	default float getVX(int index) {
		
		return getVertex(index)[0];
	}
	
	default float getVY(int index) {
		
		return getVertex(index)[1];
	}
	
	default void drawTriangle(int v1,int v2,int v3) {
		
		NPhysics.currentStage.shapefill.triangle(getVX(v1), getVY(v1), getVX(v2), getVY(v2), getVX(v3), getVY(v3));
	}
}
