package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class ObjectDefinition {

	public BodyType type = BodyType.DynamicBody;
	
	public float density = 1f;
	public float friction;
	public float restitution;
	
	public final Vector2 linearVelocity = new Vector2();
	
	public abstract ArrayList<Fixture> createFixtures(Body b);
	
	protected FixtureDef createFixtureDefinition() {
		
		FixtureDef def = new FixtureDef();
		def.density = density;
		def.friction = friction;
		def.restitution = restitution;
		
		return def;
	}
}
