package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.environment.AmbientCubemap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SimpleArrow extends Actor implements VertexBuffer,Pointer{

	private float[][] vertices = new float[5][2];
	private float[][] buffervertices = new float[5][2];
	
	Vector2 start,end;
	private Vector2 modulus;
	
	public SimpleArrow(Vector2 start,Vector2 end) {
		
		this.start = start;
		this.end = end;
		
		updateVertexArray();
	}

	//-------------COMPONENTS---------------------------/
	
	@Override public Vector2 getStart() { return start; }
	@Override public Vector2 getEnd() 	{ return end; }
	
	//-------------CREATE-VERTEX-ARRAY--------------/
	
	@Override
	public float[][] getVertexBuffer() { return buffervertices; }
	
	@Override
	public void createVertexArray(){
		
		float len = modulus.len();
		
		//LINE
		vertices[0][0] = 0;
		vertices[0][1] = 0;
		vertices[1][0] = len -10;
		vertices[1][1] = 0;
		
		//TRIANGLE
		vertices[2][0] = len - 10;
		vertices[2][1] = 5;
		vertices[3][0] = len - 10;
		vertices[3][1] = -5;
		vertices[4][0] = len;
		vertices[4][1] = 0;
		
	}
	
	@Override
	public void updateVertexArray() {
		
		getModulus();
		createVertexArray();
		
		Util.proj(vertices, buffervertices, start.x, start.y, modulus.angleRad());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Sandbox.shapeline.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(2);
		Sandbox.shapeline.setColor(Color.BLACK);
		Sandbox.shapeline.line(buffervertices[0][0], buffervertices[0][1], 
							buffervertices[1][0], buffervertices[1][1]);
		Sandbox.shapeline.end();
		
		Sandbox.shapefill.setColor(Color.BLACK);
		Sandbox.shapefill.triangle(buffervertices[2][0], buffervertices[2][1], 
					buffervertices[3][0], buffervertices[3][1], 
					buffervertices[4][0], buffervertices[4][1]);
		
	}
}
