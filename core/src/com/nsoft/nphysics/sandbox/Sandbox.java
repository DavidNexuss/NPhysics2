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
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	public static SelectHandle mainSelect = new SelectHandle();
	
	public static BitmapFont bitmapfont;
	
	public Sandbox() {
		
		super(new ScreenViewport());
		
		bitmapfont = new BitmapFont();
		
	}
	
	//------------INIT-METHODS--------------
	
	public void init() {
		
		initTextures();
		addActor(Point.lastPoint);
		addActor(AxisSupport.temp);
		AxisSupport.temp.setVisible(false);
	}
	
	public void initTextures() {
		
		AxisSupport.Axis = new Texture(Gdx.files.internal("misc/axis.png"));
		
	}
	
	
	
	private void initdebug() {
		
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
	    
	    super.draw();
		
		Gdx.gl.glLineWidth(1);
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
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
	
	
	//---------------------INPUT--------------------------//
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		switch (GameState.current) {
		default:

			if(!super.touchDragged(screenX, screenY, pointer)) {
				
				if(GameState.is(GState.START))dragCamera(screenX, screenY);

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
			if(isSnapping())Point.lastPoint = new Point(snapGrid(screenx),snapGrid(screeny), true);
			else Point.lastPoint = new Point(screenx,screeny, true);
			addActor(Point.lastPoint);
			break;
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.unhook();
			break;
		case CREATE_AXIS:
			
			AxisSupport s = new AxisSupport((PolygonActor)mainSelect.getSelected());
			if(isSnapping())s.setPosition(snapGrid(screenx),snapGrid(screeny));
			else s.setPosition(screenx, screeny);
			addActor(s);
			break;
		case CREATE_FORCE:
			
			PolygonActor current = (PolygonActor)mainSelect.getSelected();
			if(current.handler.hasSelection()) {
				
				current.handler.unSelect();
			}else {
				
				ForceComponent f = new ForceComponent(current, getUnproject());
				f.getHandler().setSelected(f);
				addActor(f);
			}
			
			break;
		default:
			
			if(!super.touchDown(screenX, screenY, pointer, button)) {
				
				mainSelect.unSelect();
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
			
			if(isSnapping())Point.lastPoint.setPosition(snapGrid(screenx),snapGrid(screeny));
			else Point.lastPoint.setPosition(screenx,screeny);
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.updateHook(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			break;
		case CREATE_AXIS:
			
			if(isSnapping())AxisSupport.temp.setPosition(snapGrid(screenx), snapGrid(screeny));
			else AxisSupport.temp.setPosition(screenx, screeny);
			return true;
		
		default:
			break;
		}
		return super.mouseMoved(screenX, screenY);
	}
	
	public static float zoomVal = 1.2f;
	@Override
	public boolean scrolled(int amount) {
		
		if(camera.zoom == 0 && amount < 0) return true;
		camera.zoom *= amount > 0 ? zoomVal : 1f/zoomVal;
		camera.update();
		updateMatrix();
		return true;
	}
}
