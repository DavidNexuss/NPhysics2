package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class ForceComponent extends ObjectChildren implements Handler{

	ArrowActor arrow;
	Vector2 origin,force;
	SelectHandle handler = new SelectHandle();
	
	@Override
	public SelectHandle getSelectHandleInstance() { return handler; }
	
	boolean relative = false;
	private boolean hook = false;
	public ForceComponent(PolygonActor parent,Vector2 start) {
		
		super(parent);
		origin = start;
		System.out.println(start);
		arrow = new ArrowActor(start);
		arrow.setHandler(handler);
		arrow.setColor(Color.BLACK);
	}

	public boolean isHooking() {return hook;}
	public void hook() {hook = true;}
	public void unhook() {hook = false;}
	public void shook() {hook = !hook;}
	
	public Vector2 getOrigin() {
		
		return origin;
	}
	
	public void update() {
		
		force = arrow.getEnd().sub(origin);
	}
	
	public Vector2 getForce() {return new Vector2(force);}
	@Override
	public void act(float delta) {
		
		if(hook) {
			arrow.setEnd(NPhysics.currentStage.getUnprojectX(Sandbox.snapping), NPhysics.currentStage.getUnprojectY(Sandbox.snapping));
			update();
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		arrow.draw(batch, parentAlpha);
	}
	@Override
	public boolean isInside(float x, float y) {
		return arrow.isInside(x, y);
	}

	@Override
	public void select() {
		shook();
		arrow.getHandler().setSelected(arrow);
	}

	@Override
	public void unselect() {
		shook();
		arrow.getHandler().unSelect();
	}
}
