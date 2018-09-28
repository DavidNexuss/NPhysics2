package com.nsoft.nphysics.simulation.dynamic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObjectDefinition {

	public BodyType type = BodyType.DynamicBody;
	public float density = 1f;
	public float friction;
	public float restitution;
	
	public final Vector2 linearVelocity = new Vector2();
}
