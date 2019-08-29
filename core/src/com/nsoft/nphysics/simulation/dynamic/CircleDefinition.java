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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Util;

/**
 * ObjectDefinition d'un cercle
 * @author David
 */
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
		s.setPosition(new Vector2());
		s.setRadius(radius * Util.METER_FACTOR);
		def.shape = s;
		
		Fixture f = b.createFixture(def);
		
		ArrayList<Fixture> fixtures = new ArrayList<>();
		fixtures.add(f);
		return fixtures;
	}

	@Override
	public Vector2 getCenter(boolean physValue) {
		return physValue ? new Vector2(center).scl(1f/Util.METERS_UNIT()) : new Vector2(center);
	}
	
	@Override
	protected void render(Body b) {
		
		NPhysics.currentStage.shapefill.circle(b.getPosition().x * Util.METERS_UNIT(), b.getPosition().y * Util.METERS_UNIT(), radius * Util.METERS_UNIT() * Util.METER_FACTOR);
	}

	@Override
	protected void initForSimulation() {}
}
