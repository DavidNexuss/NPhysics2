package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class SimpleAxis extends Actor implements Showable{

	static float MAX = 400;
	Position center;
	float angle;
	
	public SimpleAxis(Position p) {
		
		center = p;
		setAlpha(0);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Gdx.gl.glLineWidth(2f);
		
		Sandbox.shapeline.begin(ShapeType.Line);
		
		Sandbox.shapeline.setColor(Color.RED);
		Sandbox.shapeline.line(-MAX*getAlpha() + center.getX(), center.getY(), MAX*getAlpha() + center.getX(), center.getY());
		
		Sandbox.shapeline.setColor(new Color(0.1f, 0.5f, 0.1f, 1f));
		Sandbox.shapeline.line(center.getX(), -MAX*getAlpha() + center.getY(), center.getX(), MAX*getAlpha() + center.getY());
		
		Sandbox.shapeline.end();
	}
}
