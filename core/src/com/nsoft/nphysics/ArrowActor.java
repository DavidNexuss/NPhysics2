package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ArrowActor extends Actor implements ClickIn<ArrowActor>{

	//POLYGON-TYPE 1
	
	public static ArrowActor debug;
	public static ArrowActor selected;
	
	private Vector2 start,end;
	private Vector2 tempModulus;
	
	private float[][] vertices = new float[6][2];
	private float[][] buffervertices = new float[6][2];
	
	
	public ArrowActor(Vector2 start,Vector2 end) {
			
		this.start = start;
		this.end = end;

		getModulus();
		updateVertices();
		addInput();
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
		
		vertices[3][0] = len - height*2 -2;
		vertices[3][1] = height;
		vertices[4][0] = len - height*2 -2;
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
		
		float x;
		float y;
		float x2;
		float y2;
		
		x = buffervertices[0][0] < buffervertices[5][0] ? buffervertices[0][0] : buffervertices[5][0];
		y = buffervertices[0][1] < buffervertices[5][1] ? buffervertices[0][1] : buffervertices[5][1];
		x2 = buffervertices[0][0] < buffervertices[5][0] ? buffervertices[5][0] - buffervertices[0][0]: buffervertices[0][0] - buffervertices[5][0];
		y2 = buffervertices[0][1] < buffervertices[5][1] ? buffervertices[5][1] - buffervertices[0][1]: buffervertices[0][1] - buffervertices[5][1];
		
		setBounds(x, 
				y, 
				x2, 
				y2 );
		
		setDebug(true);
	}
	
	//-----------------ACTOR-FUNCTIONS----------------------
	@Override
	public void draw(Batch batch, float parentAlpha) {
		

		Sandbox.shapefill.setColor(getColor());
		Sandbox.shapefill.triangle(
				buffervertices[0][0], 
				buffervertices[0][1], 
				buffervertices[1][0],
				buffervertices[1][1], 
				buffervertices[2][0], 
				buffervertices[2][1]);
		Sandbox.shapefill.triangle(
				buffervertices[3][0], 
				buffervertices[3][1], 
				buffervertices[4][0],
				buffervertices[4][1], 
				buffervertices[5][0], 
				buffervertices[5][1]);
		
		if(selected == this) {
			
			Sandbox.shapeline.begin(ShapeType.Line);
			Sandbox.shapeline.setColor(Color.YELLOW);
			Gdx.gl.glLineWidth(6);
			
			Sandbox.shapeline.line(buffervertices[0][0], buffervertices[0][1], buffervertices[1][0], buffervertices[1][1]);
			Sandbox.shapeline.line(buffervertices[0][0], buffervertices[0][1], buffervertices[2][0], buffervertices[2][1]);
			Sandbox.shapeline.line(buffervertices[1][0], buffervertices[1][1], buffervertices[3][0], buffervertices[3][1]);
			Sandbox.shapeline.line(buffervertices[2][0], buffervertices[2][1], buffervertices[4][0], buffervertices[4][1]);
			Sandbox.shapeline.line(buffervertices[3][0], buffervertices[3][1], buffervertices[5][0], buffervertices[5][1]);
			Sandbox.shapeline.line(buffervertices[4][0], buffervertices[4][1], buffervertices[5][0], buffervertices[5][1]);
			
			Sandbox.shapeline.end();
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	//--------------INPUT--------------------------------
	
	public void addInput() {
		
		final ArrowActor dis = this;
		addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				x = Gdx.input.getX();
				y = Gdx.input.getY();
				Vector3 coords = getStage().getCamera().unproject(new Vector3(x, y, 0));
				x = coords.x;
				y = coords.y;
				
				boolean inSide = isInside(x, y);
				if(inSide) { event.cancel(); selected = dis;}
			}
		});
	}
	
	@Override
	public void select() {
		
		selected = this;
	}
	//---------------CHECK-IF-POINT-IS-INSIDE-ARROW---------------
	
	float sign (Vector2 p1, Vector2 p2, Vector2 p3)
	{
	    return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}
	
	public boolean isInside(float x,float y) {
		
		boolean b1,b2,b3;
		
		Vector2 pos = new Vector2(x, y);
		
		Vector2 v1 = new Vector2(buffervertices[0][0], buffervertices[0][1]);
		Vector2 v2 = new Vector2(buffervertices[1][0], buffervertices[1][1]);
		Vector2 v3 = new Vector2(buffervertices[2][0], buffervertices[2][1]);
		
		b1 = sign(pos, v1, v2) < 0.0f;
	    b2 = sign(pos, v2, v3) < 0.0f;
	    b3 = sign(pos, v3, v1) < 0.0f;
	    
	    if((b1 == b2) && (b2 == b3)) return true;
	    
	    v1.set(buffervertices[3][0], buffervertices[3][1]);
	    v2.set(buffervertices[4][0], buffervertices[4][1]);
	    v3.set(buffervertices[5][0], buffervertices[5][1]);
	    
	    b1 = sign(pos, v1, v2) < 0.0f;
	    b2 = sign(pos, v2, v3) < 0.0f;
	    b3 = sign(pos, v3, v1) < 0.0f;
	    
	    return ((b1 == b2) && (b2 == b3));
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
