package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class AngleArc extends AlphaActor{

	Position center;
	float radius;
	float angle;
	
	private TextArea angleInput;
	
	public AngleArc(Position center) {
		
		this.center = center;
		setAlpha(0);
	}
	
	public void show() {
		
		addAction(Actions.fadeIn(2f, Interpolation.exp5));

	}
	
	public void hide() {
		
		addAction(Actions.fadeOut(2f,Interpolation.exp5));
	}
	
	public void setAngle(float angle) {
		
		this.angle = angle;
	}
	public void setRadius(float newRadius) {
		
		radius = newRadius;
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Sandbox.shapefill.setColor(new Color(0.5f, 0.5f, 0.9f, 0.4f));
		
		Sandbox.shapefill.arc(center.getX(), center.getY(), radius*getAlpha()/3, 0, angle);

		Sandbox.bitmapfont.setColor(Color.BLACK);
		Sandbox.bitmapfont.draw(batch, "Ángulo actual" + angle, 100, 100);
	}
	
}
