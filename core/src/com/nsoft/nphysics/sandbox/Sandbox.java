package com.nsoft.nphysics.sandbox;

import static com.nsoft.nphysics.sandbox.Util.UNIT;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nsoft.nphysics.GridStage;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.sandbox.GState.Flag;
import com.nsoft.nphysics.sandbox.drawables.ArrowActor;
import com.nsoft.nphysics.sandbox.drawables.SimpleAxis;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.simulation.dsl.Builder;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationPackage;
import com.nsoft.nphysics.simulation.dynamic.SolveJob;


public class Sandbox extends GridStage implements Handler{
	
	public ArrayList<PhysicalActor<ObjectDefinition>> polygonlist = new ArrayList<>();
	public static SelectHandle mainSelect = new SelectHandle();
	public static ArrayList<ForceComponent> unknownForcesList = new ArrayList<ForceComponent>();
	
	public static SimpleAxis axis;
	
	@Override 
	public SelectHandle getSelectHandleInstance() { return mainSelect; }
	
	public static BitmapFont bitmapfont;
	
	public Sandbox() {
		
		super(new ScreenViewport());
		
		bitmapfont = new BitmapFont();
		
	}
	
	//------------INIT-METHODS--------------
	
	@Override
	public boolean isReady() {
		
		return true;
	}
	public void init() {
		
		initTextures();
		addActor(Point.lastPoint);
		addActor(DoubleAxisComponent.tmp);
		addActor(AxisSupport.temp);
		addActor(PrismaticComponent.temp);
		
		AxisSupport.temp.setVisible(false);
		DoubleAxisComponent.tmp.setVisible(false);
		PrismaticComponent.temp.setVisible(false);
		
		axis = new SimpleAxis(new PositionVector(Vector2.Zero));
		axis.show();
		setAxisPosition(axis.getCenter());
		addActor(axis);

		initdebug(); //per propisits de depuració

	}
	
	public void initTextures() {
		
		Gdx.files.internal("misc/axis.png");
		AxisSupport.Axis = new Texture(Gdx.files.internal("misc/axis.png"));
		PrismaticComponent.Axis = new Texture(Gdx.files.internal("misc/rollaxis.png"));
		
	}
	
	@Override
	public void addActor(Actor actor) {
		
		if(actor instanceof RawJoint) SimulationPackage.rawJoints.add((RawJoint)actor);
		super.addActor(actor);
	}
	
	@Override
	public void clean() {
		
	}
	
	@Override
	public void setUp() {
		super.setUp();
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

	//--------DRAW-METHODS-----------------
	
	//Metode antic per dibuixar la cuadricula
	@Deprecated
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
	
	public void solveDSLs() {
		
		
	}
	
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
			
			AxisSupport s = new AxisSupport((PhysicalActor<ObjectDefinition>)mainSelect.getLastSelected());
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
		case CREATE_PRISMATIC:
			
			PrismaticComponent c = new PrismaticComponent((PhysicalActor<ObjectDefinition>)mainSelect.getLastSelected());
			if(isSnapping())c.setPosition(snapGrid(screenx),snapGrid(screeny));
			else c.setPosition(screenx, screeny);
			addActor(c);
			break;
		case CREATE_FORCE:
			
			PhysicalActor<ObjectDefinition> current = (PhysicalActor<ObjectDefinition>)mainSelect.getLastSelected();
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
		case CREATE_ROPE:
			
			if(RopeComponent.temp == null) {
				
				RopeComponent.temp = new RopeComponent();
				Point p = Point.getPoint(screenx, screeny);
				ArrayList<Parent<Point>> parents = p.getObjectParentList(PolygonActor.class);
				System.out.println(parents.size());
				
				if(parents.get(0) == RopeComponent.temp.getPolygonA()) RopeComponent.temp.setAnchorAPoint(p);
				if(parents.get(0) == RopeComponent.temp.getPolygonB()) RopeComponent.temp.setAnchorBPoint(p);
				
				addActor(RopeComponent.temp);
			}else {
				
				Point p = Point.getPoint(screenx, screeny);
				ArrayList<Parent<Point>> parents = p.getObjectParentList(PolygonActor.class);
				
				if(parents.get(0) == RopeComponent.temp.getPolygonA()) RopeComponent.temp.setAnchorAPoint(p);
				if(parents.get(0) == RopeComponent.temp.getPolygonB()) RopeComponent.temp.setAnchorBPoint(p);
				
				RopeComponent.temp = null;
			}
			return true;
		default:
			
			if(GameState.current.fl == Flag.POLYGON) {
				
				FastPolygonCreator.handleClick(isSnapping() ? snapGrid(screenx) : screenx, isSnapping() ? snapGrid(screeny) : screeny);
				return true;
			}
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
		case CREATE_PRISMATIC:
			if(isSnapping())PrismaticComponent.temp.setPosition(snapGrid(screenx), snapGrid(screeny));
			else PrismaticComponent.temp.setPosition(screenx, screeny);
			return true;
		default:
			break;
		}
		return super.mouseMoved(screenX, screenY);
	}
	
	//---------------------KEYBOARD-INPUT-------------------
	
	public static boolean SHIFT = false;
	@Override
	public boolean keyDown(int keyCode) {
		
		if(super.keyDown(keyCode)) return true;
		for (Actor p: getActors()) {
			
			if(p instanceof PhysicalActor<?>) {
				
				PhysicalActor<ObjectDefinition> P = (PhysicalActor<ObjectDefinition>)p;
				if(P.isSelected()) {
					if(P.keyDown(keyCode)) {
						
						return true;
					}
				}
			}
		}
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
		case Keys.A:
			axis.hide();
			final SimpleAxis temp = axis;
			ThreadManager.createTask(()->{temp.addAction(Actions.removeActor());}, temp.getFadeDuration());
			
			axis = new SimpleAxis(new PositionVector(getUnproject()));
			axis.show();
			setAxisPosition(axis.getCenter());
			addActor(axis);
			
			for (Actor a : getActors()) {
				
				if(a instanceof Form) {
					
					a.setPosition(a.getX(), a.getY());
					((Form)a).updateValuesToForm();
				}
			}
			return true;
		case Keys.R:
			
			actrotate(false);
			return true;
		case Keys.S:
			actrotate(true);
			return true;
		case Keys.C:
			
			if(mainSelect.getLastSelected() instanceof PolygonActor) {
				
				addActor(((PolygonActor)mainSelect.getLastSelected()).createCopy(new Vector2(getUnproject())));
			}
			return true;
		case Keys.P:
			
			SolveJob l = new SolveJob(ForceComponent.list.get(0));
			System.out.println(l.start());
			return true;
		case Keys.O:
			
			Builder.solve((PolygonActor)mainSelect.getLastSelected());
		default:
			return super.keyDown(keyCode);
		}
	}
	
	public void destroy() {
		
		try {
			finalize();
		} catch (Throwable e) {
		
			e.printStackTrace();
		}
	}
	private void actrotate(boolean useAxis) {
		
		boolean snap = isSnapping();
		setSnapping(false);
		if(mainSelect.getLastSelected() instanceof PhysicalActor<?>) {
			
			PhysicalActor<ObjectDefinition> a = (PhysicalActor<ObjectDefinition>)mainSelect.getLastSelected();
			a.hookRotation(!a.hookRotation,useAxis);
		}
		setSnapping(snap);
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
