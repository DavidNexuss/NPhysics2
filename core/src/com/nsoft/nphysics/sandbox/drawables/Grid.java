package com.nsoft.nphysics.sandbox.drawables;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.sandbox.interfaces.VertexBuffer;

public class Grid implements VertexBuffer{

	private Vector2 point;
	private float size;
	private float anglerad;
	private float[][] buffervertices;
	
	public Grid(Vector2 point, float size, float angledeg) {
		
		this.point = point;
		this.size = size;
		this.anglerad = MathUtils.degRad * angledeg;
		
		createVertexArray();
	}
	public Vector2 getPoint() { return point; }
	public void setPoint(Vector2 point) { this.point = point; }
	public float getSize() { return size; }
	public void setSize(float size) { this.size = size; }
	public float getAnglerad() { return anglerad; }
	public void setAnglerad(float anglerad) { this.anglerad = anglerad; }
	
	@Override
	public float[][] getVertexBuffer() { return buffervertices; }
	
	@Override
	public void createVertexArray() {
		
		
	}
	@Override
	public void updateVertexArray() {
		
		
	}
	
}
