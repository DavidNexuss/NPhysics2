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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;

public class PrismaticComponent extends ObjectChildren{


	public static int RADIUS = 16;
	public static int INPUT_RADIUS = RADIUS*2;
	public static float LENGHT = 100;
	public static SpriteBatch b;
	public static Texture Axis;
	
	public static PrismaticComponent temp = new PrismaticComponent(null);
	
	public float angle = 0;
	public PrismaticComponent(PhysicalActor<ObjectDefinition> parent) {
		
		super(parent);
		initBasicForm("Wprismatic");
		getForm().setSize(400, 300);
		setSize(32, 32);
		addInput();
		b = new SpriteBatch();

		getForm().addSeparator();
		getForm().addOption(Options.createOptionNumber("pangle"));
		getForm().getOption("pangle").setValue(angle);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		b.begin();
		b.setProjectionMatrix(getStage().getCamera().combined);
		b.setColor(new Color(1, 1, 1, getPolygon() == null ? 0.5f: 1));
		b.draw(Axis, getX() - 16, getY() - 16);
		b.end();
		
		Gdx.gl.glLineWidth(2);
		
		NPhysics.currentStage.shapeline.begin(ShapeType.Line);
		NPhysics.currentStage.shapeline.setColor(Color.RED);
		NPhysics.currentStage.shapeline.line(new Vector2(-LENGHT, 0).rotate(angle).add(getPosition()), new Vector2(LENGHT, 0).rotate(angle).add(getPosition()));
		NPhysics.currentStage.shapeline.end();
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
	
	public float getAngle() {
		
		return angle;
	}
	@Override
	public boolean updateValuesFromForm() {
		super.updateValuesFromForm();
		angle = getForm().getOption("pangle").getValue();
		return true;
	}
}
