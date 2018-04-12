package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ArrowActor extends Actor{

	public static ArrowActor debug;
	
	private Vector2 start,end;
	private Vector2 tempModulus;
	
	private float[][] vertices = new float[6][2];
	private float[][] buffervertices = new float[6][2];
	
	public ArrowActor(Vector2 start,Vector2 end) {
			
		this.start = start;
		this.end = end;

		getModulus();
		updateVertices();
	}
	
	public ArrowActor(Vector2 start) {
		
		this(start,new Vector2(Gdx.input.getX(), Gdx.input.getY()));
	}
	
	public static int height = 15;
	private void createVerticesArray() {
		
		float len = tempModulus.len();
		
		//TRIANGLE 1
		vertices[0][0] = 0;
		vertices[0][1] = 0;
		vertices[1][0] = len - height*2;
		vertices[1][1] = height/2;
		vertices[2][0] = len - height*2;
		vertices[2][1] = -height/2;
		
		//TRIANGLE 2
		
		vertices[3][0] = len - height*2;
		vertices[3][1] = height;
		vertices[4][0] = len - height*2;
		vertices[4][1] = -height;
		vertices[5][0] = len;
		vertices[5][1] = 0;
		
	}
	public Vector2 getModulus() {return tempModulus = new Vector2(end).sub(start); }
	public float getAngle() {return getModulus().angle();}
	
	public void updateVertices() {
		
		createVerticesArray();
		getModulus();
		float angle = tempModulus.angleRad();
		
		for (int i = 0; i < 6; i++) {
			
			buffervertices[i][0] = Util.rotx(vertices[i][0], vertices[i][1], angle) + start.x;
			buffervertices[i][1] = Util.roty(vertices[i][0], vertices[i][1], angle) + start.y;
		}
	}
	
	//-----------------ACTOR-FUNCTIONS----------------------
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
		Sandbox.shaperenderer.triangle(
				buffervertices[0][0], 
				buffervertices[0][1], 
				buffervertices[1][0],
				buffervertices[1][1], 
				buffervertices[2][0], 
				buffervertices[2][1]);
		Sandbox.shaperenderer.triangle(
				buffervertices[3][0], 
				buffervertices[3][1], 
				buffervertices[4][0],
				buffervertices[4][1], 
				buffervertices[5][0], 
				buffervertices[5][1]);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	//--------------CLASS-FUNCTIONS----------------------
	private static ArrowActor hook;

	public static boolean isHooking() {return GameState.is(State.HOOK_FORCE_ARROW2);}
		
	public static void hook(ArrowActor arrow) {
			
		if(GameState.is(State.HOOK_FORCE_ARROW)) {		
			hook = arrow;
			GameState.set(State.HOOK_FORCE_ARROW2);
		}
	}
	
	public static void unhook() {
		
		if(GameState.is(State.HOOK_FORCE_ARROW2)) {
			
			hook = null;
			GameState.set(State.HOOK_FORCE_ARROW);
		}
	}
	public static void updateHook(float x,float y) {
			
		if(isHooking()) {
				
			hook.end.set(x, y);
			hook.updateVertices();
		}
	}	
}
