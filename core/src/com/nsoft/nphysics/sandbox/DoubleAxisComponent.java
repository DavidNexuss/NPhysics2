package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class DoubleAxisComponent extends Actor implements RawJoint{

	public static int RADIUS = 16;
	public static int INPUT_RADIUS = RADIUS*2;
	
	public static DoubleAxisComponent tmp = new DoubleAxisComponent(true);
	public static SpriteBatch b = new SpriteBatch();
	
	private static ArrayList<DoubleAxisComponent> axis = new ArrayList<>();
	
	public PolygonActor A,B;
	Body bA,bB;
	
	public boolean temp;
	
	public DoubleAxisComponent(boolean temp) {
		
		this.temp = temp;
		if(!temp) {
			
			A = (PolygonActor) Sandbox.mainSelect.getSelecteds().get(0);
			B = (PolygonActor) Sandbox.mainSelect.getSelecteds().get(1);
			axis.add(this);
		}
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
	public void draw(Batch batch, float parentAlpha) {
		
		b.begin();
		b.setProjectionMatrix(getStage().getCamera().combined);
		b.draw(AxisSupport.Axis, getX() - 16, getY() - 16);
		b.end();
	}
}
