package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Showable;
import com.nsoft.nphysics.sandbox.interfaces.VertexBuffer;

public class DoubleArrow extends Actor implements VertexBuffer,Showable {

	private float[][] vertexBuffer  = new float[8][2];
	private float[][] bufferVertices = new float[8][2];
	private Position A,B;
	
	public DoubleArrow(Position A,Position B) {
		
		this.A = A;
		this.B = B;
		
		update();
		setAlpha(0);
		setVisible(false);
	}
	public void setPositionA(Position A) {setPositionA(A, true);}
	public void setPositionB(Position B) {setPositionA(B, true);}
	public void setPosition(Position A,Position B) {setPositionA(A,false); setPositionB(B,false); update();}
	public void setPositionA(Position A,boolean update) {this.A = A; if(update)update();}
	public void setPositionB(Position B,boolean update) {this.B = B; if(update)update();}
	//----------------UPDATE-------------------
	
	public void update() {
		
		if(getAlpha() == 1 || getAlpha() == 0) {
			
			getModulus();
		}
		createVertexArray();
		updateVertexArray();
	}
	
	//---------------VECTOR-------------------
	
	private float modulus;
	public Vector2 getVectorDist(){return new Vector2(B.getX() - A.getX(), B.getY() - A.getY());}
	public float getModulus(){return modulus = getVectorDist().len();}
	
	//----------VERTEX-BUFFER-----------------
	@Override
	public void createVertexArray() {
		
		//TRIANGLE A
		vertexBuffer[0][0] = 0;
		vertexBuffer[0][1] = 0;
		
		vertexBuffer[1][0] = 8;
		vertexBuffer[1][1] = 5;
		
		vertexBuffer[2][0] = 8;
		vertexBuffer[2][1] = -5;
		
		//LINE
		
		vertexBuffer[3][0] = 5;
		vertexBuffer[3][1] = 0;
		
		vertexBuffer[4][0] = modulus - 8;
		vertexBuffer[4][1] = 0;
		
		//TRIANGLE B
		
		vertexBuffer[5][0] = modulus - 8;
		vertexBuffer[5][1] = 5;
		
		vertexBuffer[6][0] = modulus - 8;
		vertexBuffer[6][1] = -5;
		
		vertexBuffer[7][0] = modulus;
		vertexBuffer[7][1] = 0;
	}

	@Override
	public void updateVertexArray() {
		
		Util.proj(vertexBuffer, bufferVertices, A.getX(), A.getY(), getVectorDist().angleRad());
	}

	@Override
	public float[][] getVertexBuffer() { return bufferVertices; }
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		
			
		modulus = getModulus()*getAlpha();
		update();
		
		NPhysics.currentStage.shapefill.setColor(Color.BLACK);
		NPhysics.currentStage.shapefill.triangle(bufferVertices[0][0], bufferVertices[0][1], 
								   bufferVertices[1][0], bufferVertices[1][1], 
								   bufferVertices[2][0], bufferVertices[2][1]);
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(3);
		NPhysics.currentStage.shapeline.line(bufferVertices[3][0], bufferVertices[3][1], 
							   bufferVertices[4][0], bufferVertices[4][1]);
		NPhysics.currentStage.shapeline.end();
		
		NPhysics.currentStage.shapefill.triangle(bufferVertices[5][0], bufferVertices[5][1], 
				   				   bufferVertices[6][0], bufferVertices[6][1], 
				   				   bufferVertices[7][0], bufferVertices[7][1]);
		
		
		super.draw(batch, parentAlpha);
	}
	

}
