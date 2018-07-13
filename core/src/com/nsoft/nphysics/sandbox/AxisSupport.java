package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.Focusable;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;

public class AxisSupport extends ObjectChildren implements Form{

	public static int RADIUS = 16;
	public static int INPUT_RADIUS = RADIUS*2;
	
	public static Texture Axis;
	
	public static AxisSupport temp = new AxisSupport(null);
	public static SpriteBatch b;
	
	public DynamicWindow form;
	public float torque;
	public float speed;
	public AxisSupport(PolygonActor parent) {
		
		super(parent);
		setSize(32, 32);
		addInput();
		b = new SpriteBatch();
		
		form = DynamicWindow.createDefaultWindowStructure("Axis Configuration",400,200);
		form.addOption(Option.createOptionNumber("Maximum torque (N-m)"));
		form.addOption(Option.createOptionNumber("Motor speed (rad/s)"));
		
		form.setVisible(false);
		form.setAsForm(this);
		
		NPhysics.ui.addActor(form);
		setDebug(true);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		b.begin();
		b.setProjectionMatrix(getStage().getCamera().combined);
		b.setColor(new Color(1, 1, 1, getPolygon() == null ? 0.5f: 1));
		b.draw(Axis, getX() - 16, getY() - 16);
		b.end();

	}
	@Override public void setX(float x) { super.setX(x - INPUT_RADIUS/2f);}
	@Override public void setY(float y) { super.setY(y - INPUT_RADIUS/2f);}
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

	@Override
	public void click(int pointer) {
		
		form.setPosition(Gdx.graphics.getWidth() - form.getWidth() - 80, Gdx.graphics.getHeight() - form.getHeight() - 80);
		DynamicWindow.showWindow(form);
		System.out.println(pointer);
	}
	@Override
	public void select(int pointer) {
		
	}

	@Override
	public void unselect() {
		
	}
	
	//--------------------------FORM--------------------
	
	public DynamicWindow getForm() {
		return form;
	}
	
	@Override
	public void updateValuesFromForm() {
		
		torque = form.getOption("Maximum torque (N-m)").getValue();
		speed = form.getOption("Motor speed (rad/s)").getValue();
	}
	
}
