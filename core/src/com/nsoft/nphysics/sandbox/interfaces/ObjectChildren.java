package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.SelectHandle;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;

public abstract class ObjectChildren extends Group implements ClickIn,Form,Removeable{

	private PolygonActor parent;

	DynamicWindow form;
	
	public ObjectChildren(PolygonActor parent) {
		
		if(parent == null) return;
		this.parent = parent;
		
		parent.addComponent(this);
		addDragListener();
	}
	
	public void initBasicForm(String windowName) {
		
		form = DynamicWindow.createDefaultWindowStructure(windowName, 400, 400);
		form.setAsForm(this);
		form.addText("origin", "Set origin vector");
		form.addOption(Option.createOptionNumber("originx", "Origin in x").setValue(getX() / Util.UNIT));
		form.addOption(Option.createOptionNumber("originy", "Origin in y").setValue(getY() / Util.UNIT));
		form.setAsForm(this);
		form.setVisible(false);
		
		NPhysics.ui.addActor(form);
	}
	
	public DynamicWindow getForm() {
		
		return form;
	}
	
	@Override
	public void setX(float x) {
		
		super.setX(x + NPhysics.currentStage.getAxisPosition().getX());
		form.getOption("originx").setValue(getX() / Util.UNIT - NPhysics.currentStage.getAxisPosition().getX() / Util.UNIT);
		
		
	}
	
	@Override
	public void setY(float y) {

		super.setY(y + NPhysics.currentStage.getAxisPosition().getY());
		form.getOption("originy").setValue(getY() / Util.UNIT - NPhysics.currentStage.getAxisPosition().getY() / Util.UNIT);
		
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		form.getOption("originx").setValue(getX() / Util.UNIT - NPhysics.currentStage.getAxisPosition().getX() / Util.UNIT);
		form.getOption("originy").setValue(getY() / Util.UNIT - NPhysics.currentStage.getAxisPosition().getY() / Util.UNIT);
	}
	public PolygonActor getPolygon() {return parent;}
	
	private void addDragListener() {
		
		addListener(new DragListener() {
		    public void drag(InputEvent event, float x, float y, int pointer) {
		    	
		    	if(!drag) return;
		    	if (NPhysics.currentStage.isSnapping()) {
		    		
		    		setPosition(Sandbox.snapGrid(getX()), Sandbox.snapGrid(getY()));
		    		moveBy(Sandbox.snapGrid(x - getWidth() / 2),Sandbox.snapGrid( y - getHeight() / 2));
				}else {
					moveBy(x - getWidth() / 2, y - getHeight() / 2);
				}
		    	
		    }
		});
	}

	private boolean drag = true;
	public void setDrag(boolean newDrag) {drag = newDrag;}
	public void updateOnDrag(float x,float y) {}
	@Override
	public abstract boolean isInside(float x, float y);

	@Override
	public void select(int pointer) {
		
		if(!getPolygon().isSelected()) {
			getPolygon().getHandler().setSelected(getPolygon());
		}
		showWindow();
	}

	@Override
	public void unselect() {
		
		DynamicWindow.hideWindow(form);
	};

	private void showWindow() {
		
		getForm().setPosition(Gdx.graphics.getWidth() - getForm().getWidth() - 80, Gdx.graphics.getHeight() - getForm().getHeight() - 80);
		updateValuesToForm();
		DynamicWindow.showWindow(getForm());
		
	}
	@Override
	public SelectHandle getHandler() {
		
		return parent.handler;
	}
	
	@Override
	public void updateValuesFromForm() {
		
		setX(form.getOption("originx").getValue() * Util.UNIT);
		setY(form.getOption("originy").getValue() * Util.UNIT);
	}
	@Override
	public void updateValuesToForm() {}
	
	@Override
	public boolean remove() {
		
		DynamicWindow.hideWindow(form);
		parent.removeComponent(this);
		return super.remove();
	}
}
