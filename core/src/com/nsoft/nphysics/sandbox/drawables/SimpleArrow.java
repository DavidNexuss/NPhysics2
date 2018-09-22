package com.nsoft.nphysics.sandbox.drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.environment.AmbientCubemap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Pointer;
import com.nsoft.nphysics.sandbox.interfaces.VertexBuffer;

public class SimpleArrow extends Actor implements VertexBuffer,Pointer{

	private float[][] vertices = new float[5][2];
	private float[][] buffervertices = new float[5][2];
	
	Vector2 start,end;
	private Vector2 modulus;
	private Color color;
	
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
	public void setColor(Color color) {
		
		if(this.color == null) this.color = new Color(color);
		else this.color.set(color);
	}
	@Override
	public void updateVertexArray() {
		
		modulus = getModulus();
		createVertexArray();
		
		Util.proj(vertices, buffervertices, start.x, start.y, modulus.angleRad());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Gdx.gl.glLineWidth(2);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		NPhysics.currentStage.shapeline.setColor(color == null ? NPhysics.currentStage.getInvertedBackColor() : color.cpy().mul(1, 1, 1, parentAlpha));
		
		
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		
		
		NPhysics.currentStage.shapeline.line(buffervertices[0][0], buffervertices[0][1], 
							buffervertices[1][0], buffervertices[1][1]);

		NPhysics.currentStage.shapeline.end();
		
		NPhysics.currentStage.shapefill.setColor(color == null ? NPhysics.currentStage.getInvertedBackColor() : color.cpy().mul(1, 1, 1, parentAlpha));
		NPhysics.currentStage.shapefill.triangle(buffervertices[2][0], buffervertices[2][1], 
					buffervertices[3][0], buffervertices[3][1], 
					buffervertices[4][0], buffervertices[4][1]);
		
		
	}
}
