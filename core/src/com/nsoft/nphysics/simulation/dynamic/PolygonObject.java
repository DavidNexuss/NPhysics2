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
import com.badlogic.gdx.physics.box2d.joints.MotorJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.AxisSupport;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class PolygonObject extends Actor{

	PolygonDefinition def;
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
		
		SimulationStage.fill.setColor(col);
		
		for (int i = 0; i < vert.length; i++) {
			

			Vector2 pos = b.getPosition().scl(Util.UNIT);
			
			t1.set(vert[i][0], vert[i][1]).scl(Util.UNIT).add(pos);
			t2.set(vert[i][2],vert[i][3]).scl(Util.UNIT).add(pos);
			t3.set(vert[i][4], vert[i][5]).scl(Util.UNIT).add(pos);
		
			
			t1.set(Util.rotPivot(pos, t1, b.getAngle()));
			t2.set(Util.rotPivot(pos, t2, b.getAngle()));
			t3.set(Util.rotPivot(pos, t3, b.getAngle()));

			SimulationStage.fill.triangle(t1.x, t1.y, t2.x, t2.y, t3.x, t3.y);
			
			if(anchors.size() != 0) {
				
				for (Body body : anchors) {
					
					batch.draw(AxisSupport.Axis, body.getPosition().x*Util.UNIT - 16, body.getPosition().y*Util.UNIT - 16);
				}
			}
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
