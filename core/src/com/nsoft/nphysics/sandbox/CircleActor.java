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

package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.simulation.dynamic.CircleDefinition;

/**
 * Classe que defineix un cos amb forma de cercle per la fase de Sandbox.
 * @author David
 */
public class CircleActor extends PhysicalActor<CircleDefinition> implements Form{

	private Point center;
	private Point extreme;
	
	private float radius;
	public CircleActor(Point center) {
		super();
		this.center = center;
	}


	@Override
	void initDefinition() {
		definition = new CircleDefinition();
	}
	
	@Override
	public CircleActor addPoint(Point p) {
		if(isEnded()) throw new IllegalStateException();
		extreme = p;
		end();
		return this;
	}
	@Override
	public void end() {

		points.add(center);
		points.add(extreme);
		
		center.setZIndex(1);
		extreme.setZIndex(1);
		
		updatePosition();
		
		super.end();
		
	}
	
	@Override
	float getArea() {
		return (float)(Math.PI) * (radius)*(radius);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {

		super.draw(batch, parentAlpha);
		
		NPhysics.sandbox.shapefill.circle(center.getX(), center.getY(), radius);
		Gdx.gl.glLineWidth(3);
		NPhysics.currentStage.shapeline.setColor(Segment.line);
		NPhysics.sandbox.shapeline.begin(ShapeType.Line);
		NPhysics.sandbox.shapeline.circle(center.getX(), center.getY(), radius);
		NPhysics.sandbox.shapeline.end();
	}
	@Override
	public boolean isInside(float x, float y) {
		return new Vector2(x, y).dst(center.getVector()) < radius;
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
		radius = new Vector2(extreme.getVector()).dst(center.getVector());
		definition.radius = radius / Util.METERS_UNIT();
		definition.center = center.getVector();
		
		super.updatePosition(x, y, p);
	}
}
