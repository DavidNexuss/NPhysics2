package com.nsoft.nphysics.sandbox;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.Showable;

public class AngleArcActor extends Actor implements Showable{

	Position center;
	Segment s;
	float radius;
	float angle;
	
	private Table table;
	private TextButton input;
	
	
	public AngleArcActor(Position center,Segment s) {
		
		this.center = center;
		this.s = s;
		update();
		setAlpha(0);
		setDebug(true);
	}
	

	@Override
	public void drawDebug(ShapeRenderer shapes) {} //EMPTY SO IT DOESN'T RENDER ANYTHING
	
	public void setAngle(float angle) { this.angle = angle; }
	public void setRadius(float newRadius) { radius = newRadius; }
	
	
	public void update() {
		
		setHeight(MathUtils.sinDeg(angle)*radius/3f);
		setWidth(radius/3f);	
	}
	
	boolean hit = false;
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		if (touchable && this.getTouchable() != Touchable.enabled) return null;
		if(getAlpha() == 0) return null;
		if(x > 0 && y > 0 && x < getWidth() && y < getHeight() && MathUtils.atan2(y, x)*MathUtils.radDeg < angle && x*x + y*y < radius/3f*radius/3f) {
			
			if(!hit)addAction(Actions.scaleTo(1.2f, 1.2f, 0.5f, Interpolation.exp5));
			s.hit =false;
			hit = true;
			return this;
		}else {
			
			if(hit)addAction(Actions.scaleTo(1f, 1f, 0.5f, Interpolation.exp5));
			hit = false;
			return null;
		}
		
	}
	
	final static Color arcColor = new Color(0.5f, 0.5f, 0.9f, 0.4f);
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		NPhysics.sandbox.shapefill.setColor(arcColor);
		
		NPhysics.currentStage.shapefill.arc(center.getX(), center.getY(), radius*getAlpha()*getScaleX()/3f, 0, angle);
		Sandbox.bitmapfont.setColor(Color.BLACK);
		Sandbox.bitmapfont.draw(batch, "�ngulo actual" + angle, 100, 100);
		
		super.draw(batch, parentAlpha);
	}
}
