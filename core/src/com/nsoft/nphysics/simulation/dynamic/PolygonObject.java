package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MotorJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.AxisSupport;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class PolygonObject extends Actor{

	PolygonDefinition def;
	AxisSupport pivot;
	ArrayList<DynamicForce> forces = new ArrayList<>();
	boolean usePivot = false;
	
	float[][] vert;
	float[][] buff;
	Body b;
	
	public PolygonObject(PolygonDefinition def) {
		
		this.def = def;
		initVertexBuffer();
		createObject();
	}
	
	final Color col = new Color(0.2f, 0.8f, 0.2f, 0.5f);
	
	final Vector2 t1 = new Vector2();
	final Vector2 t2 = new Vector2();
	final Vector2 t3 = new Vector2();
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		NPhysics.currentStage.shapefill.setColor(col);
		
		for (int i = 0; i < vert.length; i++) {
			

			Vector2 pos = b.getPosition().scl(Util.UNIT);
			
			t1.set(vert[i][0], vert[i][1]).scl(Util.UNIT).add(pos);
			t2.set(vert[i][2],vert[i][3]).scl(Util.UNIT).add(pos);
			t3.set(vert[i][4], vert[i][5]).scl(Util.UNIT).add(pos);
		
			
			t1.set(Util.rotPivot(pos, t1, b.getAngle()));
			t2.set(Util.rotPivot(pos, t2, b.getAngle()));
			t3.set(Util.rotPivot(pos, t3, b.getAngle()));

			NPhysics.currentStage.shapefill.triangle(t1.x, t1.y, t2.x, t2.y, t3.x, t3.y);
			
			if(anchors.size() != 0) {
				
				for (Body body : anchors) {
					
					batch.draw(AxisSupport.Axis, body.getPosition().x*Util.UNIT - 16, body.getPosition().y*Util.UNIT - 16);
				}
			}
		}
	}
	
	public void aplyForce() {
		
		for (DynamicForce  d: forces) {
			
			d.update(b,new Vector2(usePivot ? pivot.getPosition().scl(1f/Util.UNIT) : b.getPosition()));
			b.applyForce(new Vector2(d.getPhysicalForce()).scl(10), d.getPhysicalOrigin(), true);
		}
	}
	private void createObject() {
		

		BodyDef bdef = new BodyDef();
		bdef.type = def.type;
		bdef.position.set(def.getCenter(true));
		b = SimulationStage.world.createBody(bdef);
		createFixtures();
		createJoints();
	}
	
	private ArrayList<Fixture> createFixtures(){
		
		ArrayList<Fixture> fixtures = new ArrayList<>();
		
		for (int i = 0; i < vert.length; i++) {
			
			FixtureDef fdef = new FixtureDef();
			fdef.density = def.density;
			fdef.friction = def.friction;
			fdef.restitution = def.restitution;
			
			PolygonShape shape = new PolygonShape();
			shape.set(vert[i]);
			fdef.shape = shape;
			b.createFixture(fdef);
		}
		
		return fixtures;
	}

	private void createJoints() {
		
		for (ObjectChildren c : def.childrens) {
			
			if(c instanceof AxisSupport) {
				
				RevoluteJointDef def = new RevoluteJointDef();
				def.bodyA = b;
				def.bodyB = createAnchor(c.getX()/Util.UNIT, c.getY()/Util.UNIT);
				def.initialize(b, def.bodyB,new Vector2(c.getX()/Util.UNIT,c.getY()/Util.UNIT));
				anchor = def.bodyB;
				anchors.add(anchor);
				SimulationStage.world.createJoint(def);
				

				if(pivot == null) { pivot = (AxisSupport)c; usePivot = true;}
				else {usePivot = false;}
			}
			
			if (c instanceof ForceComponent) {
				
				ForceComponent f = (ForceComponent)c;
				Vector2 force = f.getForce().scl(1f/Util.UNIT);
				Vector2 origin = f.getOrigin().scl(1f/Util.UNIT);
				
				DynamicForce d = new DynamicForce();
				d.force = force;
				d.origin = origin;
				d.init();
				
				forces.add(d) ;
			}
		}
	}
	
	ArrayList<Body> anchors = new ArrayList<>();
	Body anchor;
	private Body createAnchor(float x,float y) {
		
		BodyDef def = new BodyDef();
		def.position.set(x, y);
		def.type = BodyType.StaticBody;
		
		FixtureDef fdef = new FixtureDef();
		
		PolygonShape s = new PolygonShape();
		s.setAsBox(1, 1);
		fdef.shape = s;
		
		Body b = SimulationStage.world.createBody(def);
		b.createFixture(fdef);
		
		return b;
		
	}
	private void initVertexBuffer() {
		
		vert = def.getTriangles(true, true);
		buff = new float[vert.length][6];
	}
}
