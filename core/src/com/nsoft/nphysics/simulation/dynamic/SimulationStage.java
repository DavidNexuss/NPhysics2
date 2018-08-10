package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nsoft.nphysics.GridStage;
import com.nsoft.nphysics.sandbox.DoubleAxisComponent;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.RopeComponent;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class SimulationStage extends GridStage{

	public static final float ForceMultiplier = 10f;
	static ArrayList<PolygonObject> objects;
	static HashMap<PolygonActor, PolygonObject> objectsMap;
	static HashMap<Body, PolygonObject> bodiesMap;
	Body centre;
	static Vector2 gravity = new Vector2(0, -9.8f);
	static World world;
	static Matrix4 mat;
	static Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	
	public SimulationStage(Camera camera) {
		
		super(new ScreenViewport(camera));
		updateMatrix();
	}
	
	@Override
	public void setUp(){
		
		clear();
		initWorld();
		initObjects();
		initRawJoints();
		updateMatrix();
	}
	private void initWorld() {
		
		world = new World(gravity, true);
	}
	private void initObjects() {
		
		objects = new ArrayList<>();
		objectsMap = new HashMap<>();
		for (PolygonActor d: SimulationPackage.polygons)  {
			
			PolygonObject o = new PolygonObject(d.getDefinition());
			objects.add(o);
			objectsMap.put(d, o);
			addActor(o);
		}
		
		BodyDef centre = new BodyDef();
		centre.position.set(getWidth()/2, getHeight()/2);
		centre.type = BodyType.StaticBody;
		this.centre = world.createBody(centre);
	}
	
	private void initRawJoints() {
		
		for (RawJoint joint : SimulationPackage.rawJoints) {
			
			if (joint instanceof DoubleAxisComponent) {
				
				DoubleAxisComponent d = (DoubleAxisComponent) joint;
				if(d.temp) continue;
				RevoluteJointDef def = new RevoluteJointDef();
				def.initialize(objectsMap.get(d.A).b,objectsMap.get(d.B).b, new Vector2(d.getPosition()).scl(1f/Util.UNIT));
				world.createJoint(def);
			}
			
			if(joint instanceof RopeComponent) {
				
				RopeComponent c = (RopeComponent)joint;
				RopeJointDef def = new RopeJointDef();
				def.bodyA = objectsMap.get(c.getPolygonA()).b;
				def.bodyB = objectsMap.get(c.getPolygonB()).b;
				
				def.maxLength = c.getRopeVector().len() / Util.UNIT;
				
				def.localAnchorA.set(new Vector2(c.getAnchorA().getVector()).scl(1f/Util.UNIT).sub(def.bodyA.getPosition()));
				def.localAnchorB.set(new Vector2(c.getAnchorB().getVector()).scl(1f/Util.UNIT).sub(def.bodyB.getPosition()));
				
				world.createJoint(def);
			}
		}
	}
	private void aplyForces() {
		
		for (PolygonObject polygonObject : objects) {
			
			polygonObject.aplyForce();
		}
	}
	@Override
	public void draw() {
		
		aplyForces();
		world.step(Gdx.graphics.getDeltaTime(), 8, 6);
		renderer.render(world, mat);
		super.draw();
	}

	@Override
	public void updateMatrix() {
		
		super.updateMatrix();
		mat = camera.combined.cpy().scale(30, 30, 0);
	}
	
	QueryCallback callback = new QueryCallback() {
		
		@Override
		public boolean reportFixture(Fixture fixture) {
			
			if(fixture.testPoint(tmp.x, tmp.y)) {
				
				hit = fixture.getBody();
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
		
		screen.set(screenX, screenY, 0);
		tmp.set(camera.unproject(screen)).scl(1f/Util.UNIT);
		
		hit = null;
		world.QueryAABB(callback, tmp.x - 0.1f, tmp.y - 0.1f, tmp.x + 0.1f, tmp.y + 0.1f);
		
		if(hit != null && hit.getType() == BodyType.DynamicBody) {
			
			MouseJointDef def = new MouseJointDef();
			def.bodyA = centre;
			def.bodyB = hit;
			def.collideConnected = true;
			def.target.set(tmp.x, tmp.y);
			def.maxForce = 1000.0f * hit.getMass();

			mousejoint = (MouseJoint)world.createJoint(def);
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
			tmp.set(camera.unproject(screen.set(screenX, screenY, 0))).scl(1f/Util.UNIT);
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
			world.destroyJoint(mousejoint);
			mousejoint = null;
			return true;
		}
		return false;
	}
}
