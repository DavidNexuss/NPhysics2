package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class CircleDefinition extends ObjectDefinition{

	
	public float radius;
	public final Vector2 center = new Vector2();
	
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
}
