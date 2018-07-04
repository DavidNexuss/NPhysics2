package com.nsoft.nphysics.sandbox;

import static com.nsoft.nphysics.sandbox.Util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.layout.DragPane;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.GridStage;
public class Sandbox extends GridStage{

	public static boolean snapping = true;
	public static ShapeRenderer shapefill;
	public static ShapeRenderer shapeline;
	public static ShapeRenderer shapepoint;
	
	public static BitmapFont bitmapfont;
	
	public Sandbox() {
		
		super(new ScreenViewport());
		
		//VARIABLE INIT:
		shapefill = new ShapeRenderer();
		shapeline = new ShapeRenderer();
		shapepoint = new ShapeRenderer();
		
		bitmapfont = new BitmapFont();
		
		init();
		
	}
	
	//------------INIT-METHODS--------------
	
	private void init() {
		
		
		addActor(Point.lastPoint);

	}
	
	
	
	private void initdebug() {
		
		
		
		Point A = new Point(0, 0, false);
		Point B = new Point(300, 300, false);
		
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

	    
		shapefill.begin(ShapeType.Filled);
		
		super.draw();
		
		shapefill.end();
		Gdx.gl.glLineWidth(1);
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	

	public static float snapGrid(float v) {
		
		return Util.UNIT*Math.round(v/Util.UNIT);
	}
	@Override
	public void act() {

		super.act();
	}
	
	//--------DRAW-METHODS-----------------
	
	
	public void drawGrid() {
		
		shapeline.begin(ShapeType.Line);
		shapeline.setColor(Color.GRAY);
		Gdx.gl.glLineWidth(1);
		
		int X = Gdx.graphics.getWidth()/Util.UNIT;
		int Y = Gdx.graphics.getHeight()/Util.UNIT;
		
		for (int i = 0 ; i < Gdx.graphics.getWidth(); i+=UNIT) {
			
			shapeline.line(0, i, Gdx.graphics.getWidth(), i);
			shapeline.line(i, 0, i, Gdx.graphics.getHeight());
		}
		
		shapeline.end();

	}
	//-----------------------CAMERA---------------------------
	
	public void updateMatrix() {
		
		shapefill.setProjectionMatrix(getCamera().combined);
		shapeline.setProjectionMatrix(getCamera().combined);
		shapepoint.setProjectionMatrix(getCamera().combined);
	}
	
	//---------------------INPUT--------------------------//
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		switch (GameState.current) {
		default:

			if(!super.touchDragged(screenX, screenY, pointer)) {
				
				if(!GameState.is(GState.CREATE_POINT))dragCamera(screenX, screenY);

			}
		}
		
		return true;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		float screenx = unprojectX(screenX);
		float screeny = unprojectY(screenY);
		//System.out.println(GameState.current);
		switch (GameState.current) {
		case CREATE_POINT:
			
			Point.lastPoint.isTemp = false;
			if(snapping)Point.lastPoint = new Point(snapGrid(screenx),snapGrid(screeny), true);
			else Point.lastPoint = new Point(screenx,screeny, true);
			addActor(Point.lastPoint);
			break;
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.unhook();
			break;
		default:
			
			if(!super.touchDown(screenX, screenY, pointer, button)) {
				
				Point.selected = null;
				setCenter(screenX, screenY);
			}
		}
		return true;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		float screenx = unprojectX(screenX);
		float screeny = unprojectY(screenY);
		switch (GameState.current) {
		case CREATE_POINT:
			
			if(snapping)Point.lastPoint.setPosition(snapGrid(screenx),snapGrid(screeny));
			else Point.lastPoint.setPosition(screenx,screeny);
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.updateHook(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			break;

		default:
			break;
		}
		return super.mouseMoved(screenX, screenY);
	}
	
	@Override
	public boolean scrolled(int amount) {
		
		if(camera.zoom == 1 && amount < 0) return true;
		camera.zoom += amount;
		camera.update();
		updateMatrix();
		return true;
	}
}
