package com.nsoft.nphysics.sandbox;

import static com.nsoft.nphysics.sandbox.Util.METERS_UNIT;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nsoft.nphysics.GridStage;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Selector;
import com.nsoft.nphysics.ThreadManager;
import com.nsoft.nphysics.ThreadManager.Task;
import com.nsoft.nphysics.sandbox.GState.Flag;
import com.nsoft.nphysics.sandbox.drawables.ArrowActor;
import com.nsoft.nphysics.sandbox.drawables.Pulley;
import com.nsoft.nphysics.sandbox.drawables.SimpleAxis;
import com.nsoft.nphysics.sandbox.drawables.Spring;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;
import com.nsoft.nphysics.sandbox.interfaces.Ready;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;
import com.nsoft.nphysics.simulation.dynamic.SimulationPackage;
import com.nsoft.nphysics.simulation.dynamic.SolveJob;

/**
 * Fase encarregada de gestionar tot el procès de la creació i plantejament del problema
 * a resoldre. Conté una llista de tots els objectes a simular i gestionar l'input en la 
 * selecció d'aquests a part de renderitzar-los a la pantalla.
 * @author David
 */
public class Sandbox extends GridStage implements Handler{
	
	public ArrayList<PhysicalActor<?>> polygonlist = new ArrayList<>();
	public static SelectHandle mainSelect = new SelectHandle();
//	public static ArrayList<ForceComponent> unknownForcesList = new ArrayList<ForceComponent>();
	
	public static SimpleAxis axis;
	
	@Override 
	public SelectHandle getSelectHandleInstance() { return mainSelect; }
	
	public static BitmapFont bitmapfont;
	public static Selector selector;
	public Sandbox() {
		
		super(new ScreenViewport());
		bitmapfont = new BitmapFont();
		selector = new Selector();
		addActor(selector);
		
	}
	
	private String errorMessage;
	@Override
	public boolean isReady() {
		
		for (Actor a : getActors()) {
			
			if(a instanceof Ready) {
				
				if(!((Ready)a).isReady()) {
					
					errorMessage = NDictionary.get(((Ready) a).readyError());
					return false;
				}
			}
		}
		
		if(!ForceComponent.isReady()) {
			
			errorMessage = NDictionary.get("force-error");
			return false;
		}
		return true; 
	}
	
	public String getLastErrorMessage() { return errorMessage;}
	@Override
	public boolean removeGroups() {
		return false;
	}
	/**
	 * Inicialitza totes les variables necessàries, carrega les textures i afegeix
	 * a la fase tots els actors temporals
	 */
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
	private void initdebug() {
	/*	PulleyComponent p = new PulleyComponent();
		
		Point GroundA = new Point(200, 100, false);
		Point AnchorA = new Point(200,400,false);
		Point AnchorB = new Point(400, 400, false);
		Point GroundB = new Point(400, 100, false);
		
		p.setGroundA(GroundA);
		p.setAnchorA(AnchorA);
		p.setGroundB(GroundB);
		p.setAnchorB(AnchorB);
		
		addActor(GroundA);
		addActor(AnchorA);
		addActor(AnchorB);
		addActor(GroundB);
		
		addActor(p);*/
		
		/*Spring sp = new Spring();
		Point a = new Point(200, 100, false);
		Point b = new Point(300, 200, false);
		sp.addAnchorA(a);
		sp.addAnchorB(b);
		addActor(sp);
		addActor(a);
		addActor(b);*/
		/*GameState.set(State.HOOK_FORCE_ARROW);
		ArrowActor.debug = new ArrowActor(new Vector2(center.x, center.y));
		ArrowActor.hook(ArrowActor.debug);
		ArrowActor.debug.setColor(Color.PURPLE);
		
		
		Axis axis = new Axis(new Vector2(center.x, center.y), 0);
		
		
		ArrowActor blue = new ArrowActor(new Vector2(0, 0), new Vector2(200, 200));
		ArrowActor yellow = new ArrowActor(center, new Vector2(center).add(-METERS_UNIT()*5,METERS_UNIT()*6));
		ArrowActor cyan = new ArrowActor(center, new Vector2(center).add(METERS_UNIT()*5,-METERS_UNIT()*5));
		
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
	
	@Override
	public void draw() {
		super.draw();
		if(GameState.is(GState.CREATE_WATER)) {
			shapefill.begin(ShapeType.Filled);
			shapefill.setColor(WaterComponent.color.r, WaterComponent.color.g, WaterComponent.color.b, WaterComponent.color.a - 0.3f);
			float screenx = getUnprojectX();
			float screeny = getUnprojectY();
			
			shapefill.rect(screenx-Util.UNIT, screeny-Util.UNIT, Util.UNIT*2, Util.UNIT*2);
			shapefill.end();
		}
		
	}
	/*
	 * Les funcions següents touchDragged touchDown i MouseMove corresponen a la classe
	 * Stage i son executades cada cop que l'usuari mou el cursor clica o arrastra.
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		switch (GameState.current) {
		default:

			if(!super.touchDragged(screenX, screenY, pointer)) {
				
				if(Gdx.input.isButtonPressed(0) && GameState.is(GState.START))dragCamera(screenX, screenY);
				if(Gdx.input.isButtonPressed(1)) {
					selector.setEnd(getUnproject());
				} 
			}
		}
		
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		if(!super.touchUp(screenX, screenY, pointer, button)) {
			
			selector.addAction(Actions.fadeOut(0.4f, Interpolation.exp10));
		}
		return true; 
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		float screenx = unprojectX(screenX);
		float screeny = unprojectY(screenY);
		//System.out.println(GameState.current);
		switch (GameState.current) {
		case CREATE_POINT: //Es crea un punt no vinculat en cap objecte
			
			Point.lastPoint.isTemp = false;
			if(isSnapping())Point.lastPoint = new Point(snapGrid(screenx),snapGrid(screeny), true);
			else Point.lastPoint = new Point(screenx,screeny, true);
			addActor(Point.lastPoint);
			return true;
		case HOOK_FORCE_ARROW2: //Es finalitza la creació d'una força
			
			ArrowActor.unhook();
			return true;
		case CREATE_AXIS: //Es crea un eix
			
			AxisSupport s = new AxisSupport((PhysicalActor<ObjectDefinition>)mainSelect.getLastSelected());
			if(isSnapping())s.setPosition(snapGrid(screenx),snapGrid(screeny));
			else s.setPosition(screenx, screeny);
			addActor(s);
			return true;
		case CREATE_DOUBLE_AXIS: //Es crea un eix entre dos objectes
			
			DoubleAxisComponent d = new DoubleAxisComponent(false);
			if(isSnapping())d.setPosition(snapGrid(screenx),snapGrid(screeny));
			else d.setPosition(screenx, screeny);
			addActor(d);
			return true;
		case CREATE_PRISMATIC: //Es crea una via prismàtica
			
			PrismaticComponent c = new PrismaticComponent((PhysicalActor<ObjectDefinition>)mainSelect.getLastSelected());
			if(isSnapping())c.setPosition(snapGrid(screenx),snapGrid(screeny));
			else c.setPosition(screenx, screeny);
			addActor(c);
			return true;
		case CREATE_FORCE: //Es crea una força
			
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
			return true;
		case CREATE_ROPE: //Es crea una corda
			
			if(RopeComponent.temp == null) {
				
				RopeComponent.temp = new RopeComponent();
				Point p = Point.getPoint(screenx, screeny);
				ArrayList<PhysicalActor<?>> parents = p.getPhysicalParents();
				
				if(parents.get(0) == RopeComponent.temp.getPhysicalActorA()) RopeComponent.temp.setAnchorAPoint(p);
				if(parents.get(0) == RopeComponent.temp.getPhysicalActorB()) RopeComponent.temp.setAnchorBPoint(p);
				
				addActor(RopeComponent.temp);
			}else {
				
				Point p = Point.getPoint(screenx, screeny);
				ArrayList<PhysicalActor<?>> parents = p.getPhysicalParents();
				
				if(parents.get(0) == RopeComponent.temp.getPhysicalActorA()) RopeComponent.temp.setAnchorAPoint(p);
				if(parents.get(0) == RopeComponent.temp.getPhysicalActorB()) RopeComponent.temp.setAnchorBPoint(p);
				
				RopeComponent.temp = null;
			}
			return true;
		case CREATE_SPRING:
			
			if(SpringComponent.tmp == null) {
				
				SpringComponent.tmp = new SpringComponent();
				
				Point p = Point.getPoint(screenx, screeny);
				ArrayList<PhysicalActor<?>> parents = p.getPhysicalParents();
				
				if(parents.get(0) == SpringComponent.tmp.getPhysicalActorA()) SpringComponent.tmp.addAnchorA(p);;
				if(parents.get(0) == SpringComponent.tmp.getPhysicalActorB()) SpringComponent.tmp.addAnchorB(p);
				
				addActor(SpringComponent.tmp);
			}else {
				
				Point p = Point.getPoint(screenx, screeny);
				ArrayList<PhysicalActor<?>> parents = p.getPhysicalParents();
				
				if(parents.get(0) == SpringComponent.tmp.getPhysicalActorA()) SpringComponent.tmp.addAnchorA(p);
				if(parents.get(0) == SpringComponent.tmp.getPhysicalActorB()) SpringComponent.tmp.addAnchorB(p);
				
				SpringComponent.tmp = null;
			}
			return true;
		case CREATE_PULLEY:
			
			if(PulleyComponent.tmp == null) {
				PulleyComponent.tmp = new PulleyComponent();
				addActor(PulleyComponent.tmp);
			}
			Pulley p = PulleyComponent.tmp.getPullley();
			if(p.getGroundA() == null)p.setGroundA(Point.getPoint(screenx, screeny));
	   else if(p.getAnchorA() == null)p.setAnchorA(Point.getPoint(screenx, screeny));
	   else if(p.getAnchorB() == null)p.setAnchorB(Point.getPoint(screenx, screeny));
	   else if(p.getGroundB() == null)p.setGroundB(Point.getPoint(screenx, screeny));
			
			if(p.isComplete()) PulleyComponent.tmp = null;
			
			return true;
		case CREATE_WATER:
			
			screenx = getUnprojectX();
			screeny = getUnprojectY();
			
			Point pa = new Point(screenx-Util.UNIT,screeny-Util.UNIT, false);
			Point pb = new Point(screenx+Util.UNIT,screeny-Util.UNIT, false);
			Point pc = new Point(screenx-Util.UNIT,screeny+Util.UNIT, false);
			Point pd = new Point(screenx+Util.UNIT,screeny+Util.UNIT, false);
			
			addActor(pa);
			addActor(pb);
			addActor(pc);
			addActor(pd);
			
			WaterComponent water = new WaterComponent(pa, pb, pc, pd);
			addActor(water);
			
			SimulationPackage.waterComponents.add(water);
			return true;
		default:
			
			if(GameState.current.fl == Flag.POLYGON) {
				
				FastPolygonCreator.handleClick(isSnapping() ? snapGrid(screenx) : screenx, isSnapping() ? snapGrid(screeny) : screeny);
				return true;
			}
			if(!super.touchDown(screenX, screenY, pointer, button)) {
				
				
				if(button == 1) {
					if(selector.getColor().a == 0 && selector.getActions().size == 0) {
						selector.setVisible(true);
						selector.getColor().a = 1;
						selector.setStart(getUnproject());
						selector.setEnd(getUnproject());
						mainSelect.multiSelection = true;
					}
				}else {
					

					mainSelect.unSelect();
					setCenter(screenX, screenY);
					
					selector.pool.clear();
					
					mainSelect.multiSelection = false;
				}
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
		//Controla accions del teclat sobre els actors
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
		case Keys.FORWARD_DEL: //Borra un fill
			
			ClickIn in  = getSelectedChild();
			if(in instanceof Removeable) {
				
				((Removeable)in).remove();
			}
			return true;
		case Keys.SHIFT_LEFT: //Activa la multiselecció
			SHIFT = true;
			return true;
		case Keys.A: //Mou la mira mòbil
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
		case Keys.R: //Rota l'objecte seleccionat
			
			actrotate(false);
			return true;
		case Keys.S: //Rota l'objecte seleccionat en funció de la mira mòbil
			actrotate(true);
			return true;
		case Keys.C: //Copia l'objecte
			
			if(mainSelect.getLastSelected() instanceof PolygonActor) {
				
				addActor(((PolygonActor)mainSelect.getLastSelected()).createCopy(new Vector2(getUnproject())));
			}
			return true;
		case Keys.P: //Resol cualsevol incògnita
			
			if(mainSelect.getLastSelected() instanceof PhysicalActor<?>) {
				SolveJob j = new SolveJob((PhysicalActor<?>)mainSelect.getLastSelected(), ForceComponent.crrnt);
				j.start();
			}
			
			return true;
		case Keys.O: //Funció antigua per resoldre l'incògnita de forma tradicional
			//@Deprecated
			//Builder.solve((PolygonActor)mainSelect.getLastSelected());
		default:
			return super.keyDown(keyCode);
		}
	}
	
	/**
	 * Funció per assegurar la destrucció del objecte i que el GC el borri de la RAM
	 */
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
		case Keys.SHIFT_LEFT: //Desactiva la multiselecció
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
