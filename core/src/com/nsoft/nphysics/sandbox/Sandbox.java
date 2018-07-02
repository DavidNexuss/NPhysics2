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
import com.badlogic.gdx.scenes.scene2d.Stage;
public class Sandbox extends Stage {

	public static ShapeRenderer shapefill;
	public static ShapeRenderer shapeline;
	public static ShapeRenderer shapepoint;
	
	public static ShaderProgram gridShader;
	public static SpriteBatch gridBatch;
	
	
	public static Texture nullTexture;
	
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
		
		initOrtographicCamera();
		initGridShader();
		initdebug();
		addActor(Point.lastPoint);

	}
	
	private void initGridShader() {
		
		 String vertexShader = Gdx.files.internal("shaders/vertexShader").readString();
	     String fragmentShader = Gdx.files.internal("shaders/gridShader").readString();
	     
	     gridShader = new ShaderProgram(vertexShader, fragmentShader);
	     gridShader.pedantic = false;
	     System.out.println("Shader compiler log: " + gridShader.getLog());
	     
	     Pixmap p = new Pixmap(1, 1, Format.RGB888);
	     p.setColor(Color.WHITE);
	     p.drawPixel(0, 0);
	     nullTexture = new Texture(p);
	     
	     gridBatch = new SpriteBatch();
	     gridBatch.setShader(gridShader);
	}
	
	private void initdebug() {
		
		
		
		Point A = new Point(0, 0, false);
		Point B = new Point(300, 300, false);
		
		Segment R = new Segment(A, B);
		R.select();
		
		addActor(A);
		addActor(B); 
		addActor(R);
		
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
//		drawGrid();
		shapefill.begin(ShapeType.Filled);
		
		gridBatch.begin();
		gridShader.setUniformf("grid", UNIT/camera.zoom);
		gridShader.setUniformf("yoffset", camera.position.y/camera.zoom);
		gridShader.setUniformf("xoffset", camera.position.x/camera.zoom);
		gridBatch.draw(nullTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridBatch.end();
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
	
	
	float centerX;
	float centerY;
	
	float offsetX;
	float offsetY;
	
	public OrthographicCamera camera;
	
	public void initOrtographicCamera() {
		
		camera = (OrthographicCamera)getCamera();
	}
	public void setCenter(float screenX,float screenY) {
		
		centerX = screenX;
		centerY = Gdx.graphics.getHeight() - screenY;
	}
	
	public void updateMatrix() {
		
		shapefill.setProjectionMatrix(getCamera().combined);
		shapeline.setProjectionMatrix(getCamera().combined);
		shapepoint.setProjectionMatrix(getCamera().combined);
	}
	public void dragCamera(float screenX,float screenY) {
		
		float screeny = Gdx.graphics.getHeight() - screenY;
		
		((OrthographicCamera)getCamera()).translate(centerX - screenX,centerY - screeny);
		setCenter(screenX , screenY);
		getCamera().update();
		updateMatrix();
		offsetX = screenX;
		offsetY = screeny;
	}
	
	//---------------------INPUT--------------------------//
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		switch (GameState.current) {
		case DRAG_POINT:
			
			break;

		default:

			if(!super.touchDragged(screenX, screenY, pointer)) {
				
				dragCamera(screenX, screenY);

			}
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
		default:
			
			if(!super.touchDown(screenX, screenY, pointer, button)) {
				
				setCenter(screenX, screenY);
			}
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
	
	@Override
	public boolean scrolled(int amount) {
		
		if(camera.zoom == 1 && amount < 0) return true;
		camera.zoom += amount;
		camera.update();
		updateMatrix();
		return true;
	}
}
