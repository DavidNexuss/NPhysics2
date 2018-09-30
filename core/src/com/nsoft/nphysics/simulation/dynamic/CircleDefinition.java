package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nsoft.nphysics.sandbox.Util;

public class CircleDefinition extends ObjectDefinition{

	
	public float radius;
	public Vector2 center;
	
	public CircleDefinition() {}
	
	public CircleDefinition(float radius,Vector2 center) {
		this.radius = radius;
		this.center.set(center);
	}
	
	
	@Override
	public ArrayList<Fixture> createFixtures(Body b) {
		
		FixtureDef def = createFixtureDefinition();
		CircleShape s = new CircleShape();
		s.setPosition(center);
		s.setRadius(radius);
		def.shape = s;
		
		Fixture f = b.createFixture(def);
		
		ArrayList<Fixture> fixtures = new ArrayList<>();
		fixtures.add(f);
		return fixtures;
	}

	@Override
	public Vector2 getCenter(boolean physValue) {
		return physValue ? new Vector2(center).scl(1f/Util.UNIT) : new Vector2(center);
	}
}