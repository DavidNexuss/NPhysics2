package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.SelectHandle;

public abstract class ObjectChildren extends Actor implements ClickIn{

	private PolygonActor parent;
	
	public ObjectChildren(PolygonActor parent) {
		
		if(parent == null) return;
		this.parent = parent;
		
		parent.addComponent(this);
		addDragListener();
	}
	
	public PolygonActor getPolygon() {return parent;}
	private void addDragListener() {
		
		ObjectChildren dis = this;
		addListener(new DragListener() {
		    public void drag(InputEvent event, float x, float y, int pointer) {
		    	
		    	if(!getHandler().isSelected(dis)) getHandler().setSelected(dis);
		    	if (NPhysics.currentStage.isSnapping()) {
		    		
		    		setPosition(Sandbox.snapGrid(getX()), Sandbox.snapGrid(getY()));
		    		moveBy(Sandbox.snapGrid(x - getWidth() / 2),Sandbox.snapGrid( y - getHeight() / 2));
				}else {
					moveBy(x - getWidth() / 2, y - getHeight() / 2);
				}
		    }
		});
	}

	@Override
	public abstract boolean isInside(float x, float y);

	@Override
	public abstract void select();

	@Override
	public abstract void unselect();

	@Override
	public SelectHandle getHandler() {
		
		return parent.handler;
	}
}
