package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.SelectHandle;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.option.Options;
import com.nsoft.nphysics.simulation.dynamic.ObjectDefinition;

public abstract class ObjectChildren extends Group implements ClickIn,Form,Removeable{

	private PhysicalActor<ObjectDefinition> parent;

	DynamicWindow form;
	
	public ObjectChildren(PhysicalActor<ObjectDefinition> parent) {
		
		if(parent == null) return;
		this.parent = parent;
		
		parent.addComponent(this);
		addDragListener();
	}
	
	public void initBasicForm(String windowName) {
		
		form = DynamicWindow.createDefaultWindowStructure(NDictionary.get(windowName),this);
		form.addText("origin", NDictionary.get("origin"));
		form.addOption(Options.createOptionNumber("originx").setValue(getX() / Util.METERS_UNIT()));
		form.addOption(Options.createOptionNumber("originy").setValue(getY() / Util.METERS_UNIT()));
		
		form.getOption("originx").canCopy = false;
		form.getOption("originy").canCopy = false;
		form.setVisible(false);
		
		NPhysics.ui.addActor(form);
	}
	
	public DynamicWindow getForm() {
		
		return form;
	}
	
	@Override
	public void setX(float x) {
		
		super.setX(x + NPhysics.currentStage.getAxisPosition().getX());
		form.getOption("originx").setValue(getX() / Util.METERS_UNIT() - NPhysics.currentStage.getAxisPosition().getX() / Util.METERS_UNIT());
		
		
	}
	
	@Override
	public void setY(float y) {

		super.setY(y + NPhysics.currentStage.getAxisPosition().getY());
		form.getOption("originy").setValue(getY() / Util.METERS_UNIT() - NPhysics.currentStage.getAxisPosition().getY() / Util.METERS_UNIT());
		
	}
	
	public Vector2 getRelativePosition(boolean physValue) {
		
		Vector2 s = new Vector2(getX() - getPolygon().getDefinition().getCenter(false).x,getY() - getPolygon().getDefinition().getCenter(false).y);
		return physValue ? s.scl(1f/Util.METERS_UNIT()) : s;
		
	}
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		form.getOption("originx").setValue(getX() / Util.METERS_UNIT() - NPhysics.currentStage.getAxisPosition().getX() / Util.METERS_UNIT());
		form.getOption("originy").setValue(getY() / Util.METERS_UNIT() - NPhysics.currentStage.getAxisPosition().getY() / Util.METERS_UNIT());
	}
	public PhysicalActor<ObjectDefinition> getPolygon() {return parent;}
	
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
		    
		    @Override
		    public void dragStart(InputEvent event, float x, float y, int pointer) {
		    	
		    	if(!getPolygon().isSelected())getPolygon().getHandler().setSelected(getPolygon());
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
		showForm();
	}

	@Override
	public void unselect() {
		
		DynamicWindow.hideWindow(form);
	};

	
	@Override
	public SelectHandle getHandler() {
		
		return parent.getSelectHandleInstance();
	}
	
	@Override
	public void updateValuesFromForm() {
		
		setX(form.getOption("originx").getValue() * Util.METERS_UNIT());
		setY(form.getOption("originy").getValue() * Util.METERS_UNIT());
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
