package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nsoft.nphysics.GridStage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.DoubleAxisComponent;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.RopeComponent;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class SimulationStage extends GridStage{

	
	public static Vector2 gravity = new Vector2(0, -9.8f);
	static Matrix4 mat;
	static Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	
	static boolean realTime = false;
	static float fakeDelta = 1f/60f;
	
	static boolean active = true;
	
	static boolean simulation = false;
	static Simulation dynamicSimulation;
	
	ArrayList<SimulationJoint> rawJointsDraw = new ArrayList<>();
	
	static class Sesion{
		
		PolygonObject selected;
		
	}
	
	public static class Simulation{
		
		World world;
		ArrayList<PolygonObject> objects;
		HashMap<PhysicalActor<ObjectDefinition>, PolygonObject> objectsMap;
		HashMap<Body, PolygonObject> bodiesMap;
		Body centre;
		
		public Simulation(World world) {
			this.world = world;
		}
	}
	public Sesion currentSesion;
	
	public SimulationStage(Camera camera) {
		
		super(new ScreenViewport(camera));
		updateMatrix();
	}
	
	public static float getPhysicsDelta() {
		
		return realTime ? Gdx.graphics.getDeltaTime() : fakeDelta;
	}
	@Override
	public void setUp(){
		
		super.setUp();
		
		active = true;
		simulation = true;
		
		
		initStage();
		dynamicSimulation = new Simulation(initWorld());
		initObjects(dynamicSimulation,true);
		initRawJoints(dynamicSimulation,true);
	}
	
	@Override
	public void clean() {
		
		super.clean(); //executa clear(), see DragStage.java
		simulation = false;
	}
	
	private void initStage() {
		
		currentSesion = new Sesion();
	}
	public static World initWorld() {
		
		return new World(gravity, true);
	}
	public static void initObjects(Simulation s,boolean dynamicSimulation) {
		
		s.objects = new ArrayList<>();
		s.objectsMap = new HashMap<>();
		s.bodiesMap = new HashMap<>();
		
		for (PhysicalActor<ObjectDefinition> d: SimulationPackage.polygons)  {
			
			PolygonObject o = new PolygonObject(d.getDefinition(),s.world);
			s.objects.add(o);
			s.objectsMap.put(d, o);
			s.bodiesMap.put(o.b, o);
			if(dynamicSimulation)NPhysics.simulation.addActor(o);
		}
		
		if(dynamicSimulation) {
			
			BodyDef centre = new BodyDef();
			centre.position.set(NPhysics.simulation.getWidth()/2, NPhysics.simulation.getHeight()/2);
			centre.type = BodyType.StaticBody;
			s.centre = s.world.createBody(centre);
		}
	}
	
	public static void initRawJoints(Simulation s,boolean dynamicSimulation) {
		
		for (RawJoint joint : SimulationPackage.rawJoints) {
			
			if (joint instanceof DoubleAxisComponent) {
				
				DoubleAxisComponent d = (DoubleAxisComponent) joint;
				if(d.temp) continue;
				RevoluteJointDef def = new RevoluteJointDef();
				def.initialize(s.objectsMap.get(d.getPhysicalActorA()).b,s.objectsMap.get(d.getPhysicalActorB()).b, new Vector2(d.getPosition()).scl(1f/Util.METERS_UNIT()));
			
				SimulationJoint a = new SimulationJoint(s.world.createJoint(def));
			
				a.a = s.objectsMap.get(d.getPhysicalActorA());
				a.b = s.objectsMap.get(d.getPhysicalActorB());
				
				if(dynamicSimulation) {
					NPhysics.simulation.addActor(a);
					NPhysics.simulation.rawJointsDraw.add(a);
				}
				
			}
			
			if(joint instanceof RopeComponent) {
				
				RopeComponent c = (RopeComponent)joint;
				RopeJointDef def = new RopeJointDef();
				def.bodyA = s.objectsMap.get(c.getPhysicalActorA()).b;
				def.bodyB = s.objectsMap.get(c.getPhysicalActorB()).b;
				
				def.maxLength = c.getRopeVector().len() / Util.METERS_UNIT();
				
				def.localAnchorA.set(new Vector2(c.getAnchorA().getVector()).scl(1f/Util.METERS_UNIT()).sub(def.bodyA.getPosition()));
				def.localAnchorB.set(new Vector2(c.getAnchorB().getVector()).scl(1f/Util.METERS_UNIT()).sub(def.bodyB.getPosition()));
				
				SimulationJoint a = new SimulationJoint(s.world.createJoint(def));
				a.drawMod = true;
				a.drawComponents = false;
				a.useMidPoint = true;
				
				a.a = s.objectsMap.get(c.getPhysicalActorA());
				a.b = s.objectsMap.get(c.getPhysicalActorB());
				
				if(dynamicSimulation) {
					
					NPhysics.simulation.addActor(a);
					NPhysics.simulation.rawJointsDraw.add(a);
				}
			}
		}
	}
	private void aplyForces() {
		
		for (PolygonObject polygonObject : dynamicSimulation.objects) {
			
			polygonObject.aplyForce();
		}
	}
	@Override
	public void draw() {
		
		if(active)stepSimulation();
		renderer.render(dynamicSimulation.world, mat);
		if(PolygonObject.hide) {
			
			for (SimulationJoint j : rawJointsDraw) {
				
				if(j.a == currentSesion.selected || j.b == currentSesion.selected) {
					j.show = true;
				}else j.show = false;
			}
		}
		super.draw();
	}

	public void stepSimulation() {
		
		aplyForces();
		dynamicSimulation.world.step(getPhysicsDelta(), 8, 6);
		
	}
	
	@Override
	public void updateMatrix() {
		
		super.updateMatrix();
		mat = camera.combined.cpy().scale(Util.METERS_UNIT(), Util.METERS_UNIT(), 0);
	}
	
	int button;
	QueryCallback callback = new QueryCallback() {
		
		@Override
		public boolean reportFixture(Fixture fixture) {
			
			if(fixture.testPoint(tmp.x, tmp.y)) {
				
				hit = fixture.getBody();
				currentSesion.selected = dynamicSimulation.bodiesMap.get(fixture.getBody());
				
				if(button == 1) hit = null;
				return false;
			}
			return true;
		}
	};
	final Vector3 tmp = new Vector3();
	final Vector3 screen = new Vector3();
	MouseJoint mousejoint;
	Body hit;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		this.button = button;
		screen.set(screenX, screenY, 0);
		tmp.set(camera.unproject(screen)).scl(1f/Util.METERS_UNIT());
		
		hit = null;
		dynamicSimulation.world.QueryAABB(callback, tmp.x - 0.1f, tmp.y - 0.1f, tmp.x + 0.1f, tmp.y + 0.1f);
		
		if(hit != null && hit.getType() == BodyType.DynamicBody) {
			
			MouseJointDef def = new MouseJointDef();
			def.bodyA = dynamicSimulation.centre;
			def.bodyB = hit;
			def.collideConnected = true;
			def.target.set(tmp.x, tmp.y);
			def.maxForce = 1000.0f * hit.getMass();

			mousejoint = (MouseJoint)dynamicSimulation.world.createJoint(def);
			hit.setAwake(true);
			return true;
		}
		if(!super.touchDown(screenX, screenY, pointer, button)) {
			
			setCenter(screenX, screenY);
		}
		
		return true;
	}
	
	Vector2 target = new Vector2();
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		if (mousejoint != null) {
			tmp.set(camera.unproject(screen.set(screenX, screenY, 0))).scl(1f/Util.METERS_UNIT());
			mousejoint.setTarget(target.set(tmp.x, tmp.y));
			return true;
		}
		if(!super.touchDragged(screenX, screenY, pointer)) {
			
			dragCamera(screenX, screenY);
		}

		return super.touchDragged(screenX, screenY, pointer);
	}
	
	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		if (mousejoint != null) {
			dynamicSimulation.world.destroyJoint(mousejoint);
			mousejoint = null;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		if(super.keyDown(keyCode)) return true;
		
		if(keyCode == Keys.ENTER) {
			active = !active;
			return true;
		}
		if(keyCode == Keys.U) {
			PolygonObject.hide = !PolygonObject.hide;
			return true;
		}
		if(keyCode == Keys.I) {
			PolygonObject.showVel = !PolygonObject.showVel;
			return true;
		}
		return false;
	}
	
}
