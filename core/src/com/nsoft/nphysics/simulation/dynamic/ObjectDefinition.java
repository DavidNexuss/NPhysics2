/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Classe encarregada de definir el procediment necessari per poder crear les fixtures del objecte
 * a simular en Box2D
 * @author David
 */
public abstract class ObjectDefinition {

	public ArrayList<ObjectChildren> childrens = new ArrayList<>();
	
	public BodyType type = BodyType.DynamicBody;
	
	public float density = 1f;
	public float friction;
	public float restitution;
	public boolean isBullet = false;
	
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
	
	abstract protected void initForSimulation(); //Prepara l'objecte per la simulació
	abstract protected void render(Body b);
}
