package com.nsoft.nphysics.sandbox;

import static com.nsoft.nphysics.sandbox.Util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;

/**
 * Actor encarregat de renderitzar un eix amb linies discontinues a la seva posició.
 * Utilitza un renderitzat immediat amb el ShapeRenderer.
 * @author David
 */
public class Axis extends Actor implements Say{

	//POLYGON-TYPE-2
	
	private Vector2 point;
	private float anglerad;
	
	private float[][] vertices = new float[6][2];
	private float[][] buffervertices = new float[6][2];
	
	private float size = 5;
	private float lineDistance;
	
	public Axis(Vector2 point, float anglerad) {

		this.point = point;
		this.anglerad = anglerad;
		
		updateVertices();
	}

	public Vector2 getPoint() { return point; }

	public void setPoint(Vector2 point) { this.point = point;  updateProjection();}

	public float getAnglerad() { return anglerad; }

	public void setAnglerad(float anglerad) { this.anglerad = anglerad; updateProjection();}
	
	public void setSize(float size) {this.size = size; updateVertices();}
	
	private void updateVertices() {
		
		//HORITZONTAL LINE
		vertices[0][0] = -METERS_UNIT()*size;
		vertices[0][1] = 0;
		vertices[1][0] = METERS_UNIT()*size;
		vertices[1][1] = 0;
		
		say("dw");
		//VERTICAL LINE
		vertices[2][0] = 0;
		vertices[2][1] = -METERS_UNIT()*size;
		vertices[3][0] = 0;
		vertices[3][1] = METERS_UNIT()*size;

		
		//X-TEXT-POS
		vertices[4][0] = 10;
		vertices[4][1] = -10;
		
		//Y-TEXT-POS
		vertices[5][0] = -10;
		vertices[5][1] = 10;
		
		lineDistance = METERS_UNIT()*size;
		
		updateProjection();
	}
	
	private void updateProjection() {
		
		Util.proj(vertices, buffervertices, point.x, point.y, anglerad);
	}
	//----------------ACTOR-METHODS-------------------
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		Gdx.gl.glLineWidth(3);
		
		for (int j = 0; j < 4; j++) {
			
			if(j > 1)NPhysics.currentStage.shapeline.setColor(Color.RED);
			else NPhysics.currentStage.shapeline.setColor(Color.GREEN);
			
			float cos = (buffervertices[j][0] - point.x)/lineDistance;
			float sin = (buffervertices[j][1] - point.y)/lineDistance;
			
			int pair = 0;
			for (float i = -lineDistance + lineDistance/10; i < lineDistance*2; i+=lineDistance/10) {
				
				if(pair % 2 == 0) {
					
					NPhysics.currentStage.shapeline.line(
							(i*cos) + buffervertices[j][0], 
							(i*sin) + buffervertices[j][1], 
							((i + lineDistance/10) * cos) + buffervertices[j][0], 
							((i + lineDistance/10) * sin) + buffervertices[j][1]);
				}
				
				pair++;
			}
		}
		
		NPhysics.currentStage.shapeline.end();
		Sandbox.bitmapfont.draw(batch, "x", buffervertices[4][0], buffervertices[4][1]);
		Sandbox.bitmapfont.draw(batch, "y", buffervertices[5][0], buffervertices[5][1]);
		
	}
}
