package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class AxisSupport extends ObjectChildren {

	public static int RADIUS = 16;
	public static int INPUT_RADIUS = RADIUS*2;
	
	public static Texture Axis;
	
	public static AxisSupport temp = new AxisSupport(null);
	public static SpriteBatch b;
	public AxisSupport(PolygonActor parent) {
		
		super(parent);
		setSize(32, 32);
		b = new SpriteBatch();
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
	public void select() {
		
	}

	@Override
	public void unselect() {
		
	}
	
}
