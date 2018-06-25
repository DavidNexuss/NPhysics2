package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.nsoft.nphysics.Util.*;
public class Sandbox extends Stage {

	public static ShapeRenderer shapefill;
	public static ShapeRenderer shapeline;
	public static ShapeRenderer shapepoint;
	
	public static BitmapFont bitmapfont;
	
	public Sandbox() {
		super();
		
		//VARIABLE INIT:
		shapefill = new ShapeRenderer();
		shapeline = new ShapeRenderer();
		shapepoint = new ShapeRenderer();
		
		bitmapfont = new BitmapFont();
		
		init();
	}
	
	//------------INIT-METHODS--------------
	private void init() {
		
		initdebug();
		addActor(Point.lastPoint);
	}
	
	private void initdebug() {
		

		Point A = new Point(50, 80, false);
		Point B = new Point(600, 400, false);
		
		Segment R = new Segment(A, B);
		R.select();
		
		addActor(A);
		addActor(B); 
		addActor(R);
		
		Point AB = new Point(200, 80, false);
		Point BB = new Point(100, 200, false);
		
		Segment RB = new Segment(AB, BB);
		
		addActor(AB);
		addActor(BB); 
		addActor(RB);
		
		/*GameState.set(State.HOOK_FORCE_ARROW);
		ArrowActor.debug = new ArrowActor(new Vector2(center.x, center.y));
		ArrowActor.hook(ArrowActor.debug);
		ArrowActor.debug.setColor(Color.PURPLE);
		
		
		Axis axis = new Axis(new Vector2(center.x, center.y), 0);
		
		
		ArrowActor blue = new ArrowActor(new Vector2(0, 0), new Vector2(200, 200));
		ArrowActor yellow = new ArrowActor(center, new Vector2(center).add(-UNIT*5,UNIT*6));
		ArrowActor cyan = new ArrowActor(center, new Vector2(center).add(UNIT*5,-UNIT*5));
		
		blue.setColor(Color.BLUE);
		yellow.setColor(Color.OLIVE);
		cyan.setColor(Color.CYAN);
		
		addActor(blue);
		addActor(yellow);
		addActor(cyan);
		
		addActor(ArrowActor.debug);
		addActor(axis);
		*/
	//	addActor(new SimpleArrow(center, new Vector2(center).add(200,200)));
	}
	
	
	//-----------LOOP-METHODS---------------
	@Override
	public void draw() {
		

		  Gdx.gl.glEnable(GL20.GL_BLEND);
	        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		drawGrid();
		shapefill.begin(ShapeType.Filled);

		super.draw();
		
		shapefill.end();
		Gdx.gl.glLineWidth(1);


		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	

	public float snapGrid(float v) {
		
		return Util.UNIT*Math.round(v/Util.UNIT);
	}
	@Override
	public void act() {

		super.act();
	}
	
	//--------DRAW-METHODS-----------------
	
	
	public static void drawGrid() {
		
		shapeline.begin(ShapeType.Line);
		shapeline.setColor(Color.GRAY);
		Gdx.gl.glLineWidth(1);
		
		int X = Gdx.graphics.getWidth()/Util.UNIT;
		int Y = Gdx.graphics.getHeight()/Util.UNIT;
		
		for (int i = 0; i < Gdx.graphics.getWidth(); i+=UNIT) {
			
			shapeline.line(0, i, Gdx.graphics.getWidth(), i);
			shapeline.line(i, 0, i, Gdx.graphics.getHeight());
		}
		
		shapeline.end();

	}
	
	//---------------------INPUT--------------------------//
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		switch (GameState.current) {
		case DRAG_POINT:
			
			break;

		default:

			return super.touchDragged(screenX, screenY, pointer);
		}
		
		return true;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		//System.out.println(GameState.current);
		switch (GameState.current) {
		case CREATE_POINT:
			
			Point.lastPoint.isTemp = false;
			Point.lastPoint = new Point(snapGrid(screenX),snapGrid(Gdx.graphics.getHeight()- screenY), true);
			addActor(Point.lastPoint);
			break;
		case DRAG_POINT:
			
			break;
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.unhook();
			break;
		case CREATE_SEGMENT:
			
			return super.touchDown(screenX, screenY, pointer, button);
		default:
			return super.touchDown(screenX, screenY, pointer, button);
		}
		return true;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		switch (GameState.current) {
		case CREATE_POINT:
			
			Point.lastPoint.setPosition(snapGrid(screenX),snapGrid(Gdx.graphics.getHeight()- screenY));
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.updateHook(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			break;

		default:
			break;
		}
		return super.mouseMoved(screenX, screenY);
	}
}
