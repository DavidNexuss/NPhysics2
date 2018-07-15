package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.Draggable.DragAdapter;
import com.nsoft.nphysics.DragStage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.interfaces.ObjectChildren;

public class ForceComponent extends ObjectChildren{

	ArrowActor arrow;
	Vector2 origin,force;
	
	boolean relative = false;
	private boolean hook = false;
	public ForceComponent(PolygonActor parent,Vector2 start) {
		
		super(parent);
		origin = start;
		System.out.println(start);
		
		arrow = new ArrowActor(origin);
		arrow.setHandler(parent.getSelectHandleInstance());
		arrow.setColor(Color.BLACK);
		addActor(arrow);
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
			arrow.setEnd(NPhysics.currentStage.getUnprojectX(), NPhysics.currentStage.getUnprojectY());
			update();
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		super.draw(batch, parentAlpha);
	}
	@Override
	public boolean isInside(float x, float y) {
		return arrow.isInside(x, y);
	}

	@Override
	public void select(int pointer) {
		shook();
	}

	@Override
	public void unselect() {
		shook();
	}
}
