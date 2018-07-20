package com.nsoft.nphysics.sandbox;

import static com.nsoft.nphysics.sandbox.Util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.layout.DragPane;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.GridStage;
import com.nsoft.nphysics.sandbox.drawables.ArrowActor;
import com.nsoft.nphysics.sandbox.drawables.SimpleAxis;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.simulation.dynamic.SimulationPackage;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;
public class Sandbox extends GridStage implements Handler{
	
	public static SelectHandle mainSelect = new SelectHandle();
	public static SimpleAxis axis;
	@Override 
	public SelectHandle getSelectHandleInstance() { return mainSelect; }
	
	public static BitmapFont bitmapfont;
	
	public Sandbox() {
		
		super(new ScreenViewport());
		
		bitmapfont = new BitmapFont();
		
	}
	
	//------------INIT-METHODS--------------
	
	public void init() {
		
		initTextures();
		addActor(Point.lastPoint);
		addActor(DoubleAxisComponent.tmp);
		addActor(AxisSupport.temp);
		AxisSupport.temp.setVisible(false);
		DoubleAxisComponent.tmp.setVisible(false);
		
	}
	
	public void initTextures() {
		
		AxisSupport.Axis = new Texture(Gdx.files.internal("misc/axis.png"));
		
	}
	
	@Override
	public void addActor(Actor actor) {
		
		if(actor instanceof RawJoint) SimulationPackage.rawJoints.add((RawJoint)actor);
		super.addActor(actor);
	}
	
	@Override
	public void clean() {
		
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
			
			AxisSupport s = new AxisSupport((PolygonActor)mainSelect.getLastSelected());
			if(isSnapping())s.setPosition(snapGrid(screenx),snapGrid(screeny));
			else s.setPosition(screenx, screeny);
			addActor(s);
			break;
		case CREATE_DOUBLE_AXIS:
			
			DoubleAxisComponent d = new DoubleAxisComponent(false);
			if(isSnapping())d.setPosition(snapGrid(screenx),snapGrid(screeny));
			else d.setPosition(screenx, screeny);
			addActor(d);
			break;
			
		case CREATE_FORCE:
			
			PolygonActor current = (PolygonActor)mainSelect.getLastSelected();
			if(ForceComponent.temp != null) {
				ForceComponent.temp.unhook();
				ForceComponent.temp = null;
				break;
			}else {
				
				ForceComponent.temp = new ForceComponent(current, getUnproject());
				ForceComponent.temp.hook();
				addActor(ForceComponent.temp);
			}
			break;
		case CREATE_FAST_POLYGON:
			
			FastPolygonCreator.handleClick(isSnapping() ? snapGrid(screenx) : screenx, isSnapping() ? snapGrid(screeny) : screeny);
			return true;
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
		case CREATE_DOUBLE_AXIS:
			if(isSnapping())DoubleAxisComponent.tmp.setPosition(snapGrid(screenx), snapGrid(screeny));
			else DoubleAxisComponent.tmp.setPosition(screenx, screeny);
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
	
	//---------------------KEYBOARD-INPUT-------------------
	
	public static boolean SHIFT = false;
	@Override
	public boolean keyDown(int keyCode) {
		
		switch (keyCode) {
		case Keys.FORWARD_DEL:
			
			ClickIn in  = getSelectedChild();
			if(in instanceof Removeable) {
				
				((Removeable)in).remove();
			}
			return true;
		case Keys.SHIFT_LEFT:
			SHIFT = true;
			return true;
		default:
			return super.keyDown(keyCode);
		}
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		
		switch (keyCode) {
		case Keys.SHIFT_LEFT:
			mainSelect.cleanArray();
			SHIFT = false;
			
			return true;
		default:
			return super.keyUp(keyCode);
		}
	}
	public ClickIn getSelectedChild() {
		
		return getSelectedChild(getSelectHandleInstance().getLastSelected());
	}
	public ClickIn getSelectedChild(ClickIn in) {
		
		if(in instanceof Handler) {
			
			ClickIn child =getSelectedChild(((Handler)in).getSelectHandleInstance().getLastSelected());
			if(child == null) return in;
			return child;
		}else return in;
	}
}
