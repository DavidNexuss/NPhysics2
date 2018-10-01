package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.simulation.dynamic.CircleDefinition;

public class CircleActor extends PhysicalActor<CircleDefinition> implements Form{

	private Point center;
	private Point extreme;
	
	private float radius;
	public CircleActor(Point center) {
		super();
		this.center = center;
	}


	@Override
	void initDefinition() {
		definition = new CircleDefinition();
	}
	
	public void addExtreme(Point p) {
		if(isEnded()) throw new IllegalStateException();
		extreme = p;
		end();
	}
	@Override
	public void end() {

		points.add(center);
		points.add(extreme);
		
		updatePosition();
		
		super.end();
		
	}
	
	@Override
	float getArea() {
		return (float)(Math.PI) * (radius/ Util.UNIT)*(radius/ Util.UNIT);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {

		super.draw(batch, parentAlpha);
		
		NPhysics.sandbox.shapefill.circle(center.getX(), center.getY(), radius);
		Gdx.gl.glLineWidth(3);
		NPhysics.currentStage.shapeline.setColor(Segment.line);
		NPhysics.sandbox.shapeline.begin(ShapeType.Line);
		NPhysics.sandbox.shapeline.circle(center.getX(), center.getY(), radius);
		NPhysics.sandbox.shapeline.end();
	}
	@Override
	public boolean isInside(float x, float y) {
		return new Vector2(x, y).dst(center.getVector()) < radius;
	}

	@Override
	public void updatePosition(float x, float y, Point p) {
		
		radius = new Vector2(extreme.getVector()).dst(center.getVector());
		definition.radius = radius / Util.UNIT;
		definition.center = center.getVector();
		updateValuesToForm();
		
		super.updatePosition(x, y, p);
	}
	
	@Override
	public void updateValuesToForm() {
		
		setValue("polygon_phys_mass", getValue("polygon_phys_density") * getArea());
	}
}
