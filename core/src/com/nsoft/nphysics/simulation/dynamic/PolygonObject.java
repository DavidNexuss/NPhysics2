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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.AxisSupport;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PrismaticComponent;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;

public class PolygonObject extends Actor{

	public static float PHYSICAL_EPSILON = 0.01f;
	PolygonDefinition def;
	SimpleArrow gravityArrow;
	SimpleArrow velocityArrow;
	AxisSupport pivot;
	ArrayList<DynamicForce> forces = new ArrayList<>();
	boolean usePivot = false;
	boolean reactY = false;
	boolean reactX = false;
	
	float[][] vert;
	float[][] buff;
	Body b;
	
	World owner;
	
	ArrowLabel gravityLabel;
	ArrowLabel velLabel;
	
	ArrayList<SimulationJoint> simjoints = new ArrayList<>();
	
	public PolygonObject(PolygonDefinition def,World owner) {
		
		this.owner = owner;
		this.def = def;
		initVertexBuffer();
		createObject();
		Vector2 center = new Vector2(b.getMassData().center).add(b.getPosition()).scl(Util.UNIT);
		Vector2 force = new Vector2(SimulationStage.gravity).scl(Util.UNIT / 10f);
		
		gravityArrow = new SimpleArrow(center, force.add(center));
		velocityArrow = new SimpleArrow(center, new Vector2(b.getLinearVelocity()).add(center));
		
		velLabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
		gravityLabel = new ArrowLabel(NPhysics.currentStage.getUiGroup());
	
		gravityLabel.setFloat(SimulationStage.gravity.y * b.getMass());
		gravityLabel.conc("N");
		

	}
	
	final Color col = new Color(0.2f, 0.8f, 0.2f, 0.5f);
	final Color col2 = new Color(0.7f,0.7f,0.2f,0.5f);
	
	final Vector2 t1 = new Vector2();
	final Vector2 t2 = new Vector2();
	final Vector2 t3 = new Vector2();
	final Vector2 centre = new Vector2();
	
	public static boolean hide = false;
	public static boolean showVel = false;
	
	final static float hidealpha = 0.1f;
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		boolean selected = NPhysics.simulation.currentSesion.selected == this;
		
		NPhysics.currentStage.shapefill.setColor(selected ? col2.cpy().mul(1, 1, 1, hide && !selected ? hidealpha : 1) : col.cpy().mul(1, 1, 1, hide && !selected ? hidealpha : 1));
		
		gravityLabel.setColor(Color.YELLOW.cpy().mul(1, 1, 1, hide && !selected ? hidealpha : 1));
		velLabel.setColor(Color.CYAN.cpy().mul(1, 1, 1, hide && !selected ? hidealpha : 1));
		
		gravityArrow.setColor(Color.YELLOW.cpy().mul(1, 1, 1, hide && !selected ? hidealpha : 1));
		velocityArrow.setColor(Color.CYAN.cpy().mul(1, 1, 1, hide && !selected ? hidealpha : 1));
		for (int i = 0; i < vert.length; i++) {
			

			Vector2 pos = b.getPosition().scl(Util.UNIT);
			
			t1.set(vert[i][0], vert[i][1]).scl(Util.UNIT).add(pos);
			t2.set(vert[i][2],vert[i][3]).scl(Util.UNIT).add(pos);
			t3.set(vert[i][4], vert[i][5]).scl(Util.UNIT).add(pos);
		
			
			t1.set(Util.rotPivot(pos, t1, b.getAngle()));
			t2.set(Util.rotPivot(pos, t2, b.getAngle()));
			t3.set(Util.rotPivot(pos, t3, b.getAngle()));

			NPhysics.currentStage.shapefill.triangle(t1.x, t1.y, t2.x, t2.y, t3.x, t3.y);
			
		}
		
		NPhysics.currentStage.shapefill.setColor(Color.GRAY.mul(1, 1, 1, hide && !selected ? hidealpha : 1));
		centre.set(b.getPosition()).add(b.getMassData().center);
		NPhysics.currentStage.shapefill.circle(centre.x * Util.UNIT , centre.y * Util.UNIT, 3);
		if(def.childrens.size() != 0) {
			
			for (ObjectChildren c : def.childrens) {
				
				if(c instanceof AxisSupport) {
					c.draw(batch, parentAlpha);
				}
			}
		}
		
		Vector2 center = new Vector2(b.getMassData().center).add(b.getPosition()).scl(Util.UNIT);
		Vector2 force = new Vector2(SimulationStage.gravity).scl(Util.UNIT / SimulationStage.ForceMultiplier * b.getMass());
		
		if(def.type != BodyType.StaticBody) {
				
				gravityArrow.setStart(center);
				gravityArrow.setEnd(force.add(center));
				gravityArrow.updateVertexArray();
				
				gravityArrow.draw(batch,parentAlpha);
				
				gravityLabel.setPosition(gravityArrow.getStart().add(new Vector2(60, -50)));
			
				if(showVel) {
					

					velocityArrow.setStart(center);
					velocityArrow.setEnd(new Vector2(new Vector2(b.getLinearVelocity()).scl(Util.UNIT / SimulationStage.ForceMultiplier * b.getMass())).add(center));
					velocityArrow.updateVertexArray();
					
					velocityArrow.draw(batch, parentAlpha);
					
					velLabel.setVisible(true);
					velLabel.setFloat(b.getLinearVelocity().len() * b.getMass());
					velLabel.conc("kg m/s");
					
					velLabel.setPosition(velocityArrow.getStart().add(new Vector2(60,50)));
				}else {
					
					velLabel.setVisible(false);
				}
		}else {
			
			
			velLabel.setVisible(false);
			gravityLabel.setVisible(false);
		}
		
		for (DynamicForce  d : forces) {
			
			d.show = !(hide && !selected);
			d.updateLabel();
		}
		
		for (SimulationJoint j : simjoints) {
			
			j.show = !(hide && !selected);
			j.draw(batch,parentAlpha);
		}
		
	}
	
	public Vector2 getCenter() {
		
		return new Vector2(b.getMassData().center).add(b.getPosition()).scl(Util.UNIT);
	}
	
	public Vector2 getGravityForce() {
		
		return new Vector2(SimulationStage.gravity).scl(Util.UNIT / SimulationStage.ForceMultiplier * b.getMass()); 
	}
	public void aplyForce() {
		 if(b.getType() == BodyType.StaticBody) return;
		for (DynamicForce  d: forces) {

			boolean threshold = false;
			
			if (usePivot) {
				
				b.applyForce(d.getPhysicalForce(), d.getPhysicalOrigin(), true);
				d.update(b,b.getPosition(),false);
			}else {
				
				if(!d.isCentered) {

					float l = new Vector2(d.getPhysicalOrigin()).sub(b.getPosition()).len2();
					threshold =   l < PHYSICAL_EPSILON;
					
					d.isCentered = threshold;
				}

				if(d.isCentered) {
					d.isCentered = true;
					b.applyForceToCenter(d.getPhysicalForce(), true);
					d.update(b, b.getPosition(), true);
					
				}
				else {
					b.applyForce(d.getPhysicalForce(), d.getPhysicalOrigin(), true);
					d.update(b, b.getPosition(), true);
				}
			}
			
		}
	}
	private void createObject() {
		

		BodyDef bdef = new BodyDef();
		bdef.type = def.type;
		bdef.position.set(def.getCenter(true));
		bdef.linearVelocity.set(def.linearVelocity);
		b = owner.createBody(bdef);
		createFixtures();
		if(bdef.type != BodyType.StaticBody)createJoints();
	}
	
	private BodyType checkStatic(BodyType t) {
		
		if(t == BodyType.StaticBody) return BodyType.StaticBody;
		int n = 0;
		for (ObjectChildren c : def.childrens) {
			
			if(c instanceof AxisSupport) {
				n += 1;
				if(n > 1) return BodyType.StaticBody;
			}
		}
		
		return t;
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
				
				AxisSupport s = (AxisSupport)c;

				RevoluteJointDef def = new RevoluteJointDef();
				def.bodyB = createAnchor(c.getX()/Util.UNIT, c.getY()/Util.UNIT);
				def.initialize(b, def.bodyB,new Vector2(c.getX()/Util.UNIT,c.getY()/Util.UNIT));
				def.enableMotor = true;
				def.maxMotorTorque = s.torque;
				def.motorSpeed = s.speed;
				anchor = def.bodyB;
				anchors.add(anchor);
				
				
				if(def.bodyB.getPosition().epsilonEquals(def.bodyA.getPosition(), PHYSICAL_EPSILON)) {
					
					reactX = true;
					reactY = true;
				}

				if(pivot == null) { pivot = (AxisSupport)c; usePivot = true;}
				else {usePivot = false;}
				
				simjoints.add(new SimulationJoint(owner.createJoint(def)));
				
			}

			if (c instanceof PrismaticComponent) {
				
				PrismaticComponent p = (PrismaticComponent)c;
				PrismaticJointDef def = new PrismaticJointDef();
				Vector2 anchor = new Vector2(c.getX()/Util.UNIT, c.getY()/Util.UNIT);
				def.initialize(b, createAnchor(anchor.x,anchor.y), anchor, new Vector2(1,0).rotate(p.getAngle()));
				def.enableMotor = true;
				simjoints.add(new SimulationJoint(owner.createJoint(def)));
				
				if(p.getAngle() == 0 || p.getAngle() == 180) reactY = true;
				if(p.getAngle() == 90 || p.getAngle() == 270) reactX = true;
			}
			if (c instanceof ForceComponent) {
				
				ForceComponent f = (ForceComponent)c;
				if(f.isVariable()) return ;
				Vector2 force = f.getForce().scl(1f/Util.UNIT);
				Vector2 origin = f.getPosition().scl(1f/Util.UNIT);
				
				DynamicForce d = new DynamicForce();
				d.force = force;
				d.origin = origin;
				d.diff = new Vector2(origin).sub(b.getPosition());
				d.init(((ForceComponent) c).getType());
				
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
		
		Body b = owner.createBody(def);

		
		return b;
		
	}
	private void initVertexBuffer() {
		
		vert = def.getTriangles(true, true);
		buff = new float[vert.length][6];
	}
}
