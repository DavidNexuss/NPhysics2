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

package com.nsoft.nphysics.sandbox.drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class SimpleAxis extends Actor implements Showable{

	static float MAX = 400;
	private Position center;
	float angle;
	
	public SimpleAxis(Position p) {
		
		center = p;
		setAlpha(0);
	}
	
	public Position getCenter() {return new PositionVector(center.getVector());}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Gdx.gl.glLineWidth(2f);
		
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		
		NPhysics.currentStage.shapeline.setColor(Color.RED);
		NPhysics.currentStage.shapeline.line(-MAX*getAlpha() + center.getX(), center.getY(), MAX*getAlpha() + center.getX(), center.getY());
		
		NPhysics.currentStage.shapeline.setColor(new Color(0.1f, 0.5f, 0.1f, 1f));
		NPhysics.currentStage.shapeline.line(center.getX(), -MAX*getAlpha() + center.getY(), center.getX(), MAX*getAlpha() + center.getY());
		
		NPhysics.currentStage.shapeline.end();
	}
}
