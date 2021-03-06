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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;
import com.nsoft.nphysics.simulation.dynamic.PolygonObject;

/**
 * Classe que defineix el component d'eix de suport dins la fase Sandbox.
 * @see PolygonObject
 * @author David
 */
public class AxisSupport extends ObjectChildren implements Form{

	public static int RADIUS = 16;
	public static int INPUT_RADIUS = RADIUS*2;
	
	public static Texture Axis;
	
	public static AxisSupport temp = new AxisSupport(null);
	public static SpriteBatch b;
	
	public float torque;
	public float speed;
	
	public AxisSupport(PhysicalActor<ObjectDefinition> parent) {
		
		super(parent);
		initBasicForm("Waxis");
		getForm().setSize(400, 300);
		setSize(32, 32);
		addInput();
		b = new SpriteBatch();

		getForm().addSeparator();
		getForm().addOption(Options.createOptionNumber("torque"));
		getForm().addOption(Options.createOptionNumber("mspeed"));

		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		b.begin();
		b.setProjectionMatrix(NPhysics.currentStage.camera.combined);
		b.setColor(new Color(1, 1, 1, getPolygon() == null ? 0.5f: 1));
		b.draw(Axis, getX() - 16, getY() - 16);
		b.end();

	}
	@Override public void setX(float x) { super.setX(x - INPUT_RADIUS/2f); }
	@Override public void setY(float y) { super.setY(y - INPUT_RADIUS/2f); }
	@Override public float getX() {return super.getX() + INPUT_RADIUS/2f;}
	@Override public float getY() {return super.getY() + INPUT_RADIUS/2f;}
	@Override public void setPosition(float x, float y) { super.setPosition(x- INPUT_RADIUS/2f, y - INPUT_RADIUS/2f);}

	public Vector2 getPosition() {
		
		return new Vector2(getX(), getY());
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		boolean hit =  x >= -INPUT_RADIUS/2f && x < getWidth() + INPUT_RADIUS/2f && y >= -INPUT_RADIUS/2f && y < getHeight() + INPUT_RADIUS/2f;
		return hit ? this : null;
	}
	@Override
	public boolean isInside(float x, float y) {
		
		float len2 = new Vector2(x - getX(), y - getY()).len2();
		return len2 < INPUT_RADIUS*INPUT_RADIUS;
		
	}
	
	//--------------------------FORM--------------------
	@Override
	public boolean updateValuesFromForm() {
		
		super.updateValuesFromForm();
		torque = getForm().getOption("torque").getValue();
		speed = getForm().getOption("mspeed").getValue();
		return true;
	}
	
}
