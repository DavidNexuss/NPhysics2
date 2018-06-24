package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class SimpleAxis extends AlphaActor{

	static float MAX = 400;
	Position center;
	float angle;
	
	public SimpleAxis(Position p) {
		
		center = p;
		setAlpha(0);
	}
	
	public void show() {
		
		addAction(Actions.fadeIn(1f, Interpolation.exp5));
	}
	public void hide() {
		
		addAction(Actions.fadeOut(1f, Interpolation.exp5));
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Gdx.gl.glLineWidth(2f);
		
		Sandbox.shapeline.begin(ShapeType.Line);
		
		Sandbox.shapeline.setColor(Color.RED);
		Sandbox.shapeline.line(-MAX*getAlpha() + center.getX(), center.getY(), MAX*getAlpha() + center.getX(), center.getY());
		
		Sandbox.shapeline.setColor(Color.GREEN);
		Sandbox.shapeline.line(center.getX(), -MAX*getAlpha() + center.getY(), center.getX(), MAX*getAlpha() + center.getY());
		
		Sandbox.shapeline.end();
	}
}
