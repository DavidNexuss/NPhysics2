package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class ObjectDefinition {

	public ArrayList<ObjectChildren> childrens = new ArrayList<>();
	
	public BodyType type = BodyType.DynamicBody;
	
	public float density = 1f;
	public float friction;
	public float restitution;
	
	public final Vector2 linearVelocity = new Vector2();
	
	public abstract ArrayList<Fixture> createFixtures(Body b);
	
	public abstract Vector2 getCenter(boolean physValue);
	
	public float getCenterY(boolean PhysValue) {
		
		return getCenter(PhysValue).y;
	}
	public float getCenterX(boolean PhysValue) {
		
		return getCenter(PhysValue).x;
	}
	
	protected FixtureDef createFixtureDefinition() {
		
		FixtureDef def = new FixtureDef();
		def.density = density;
		def.friction = friction;
		def.restitution = restitution;
		
		return def;
	}
	
	abstract protected void initForSimulation();
	abstract protected void render(Body b);
}
