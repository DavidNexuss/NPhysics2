package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.Pulley;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionNumber;

public class PulleyComponent extends RawJoint implements ClickIn,Parent<Point>,Form{

	public Pulley pulley = new Pulley();
	public float ratio = 1;
	public static PulleyComponent tmp;
	
	private DynamicWindow form;
	public PulleyComponent() {
		addInput();
		getColor().a = 0;
		defaultInit();
		initForm();
	}
	
	private void initForm() {
		
		form = DynamicWindow.createDefaultWindowStructure(NDictionary.get("pulley-form"),this);
		form.setVisible(false);
		
		form.addOption(new Option("pulley-ratio", new UIOptionNumber()));
		form.getOption("pulley-ratio").setValue(ratio);
		
		NPhysics.ui.addActor(form);
	}
	public void setGroundA(Point p) {
		
		pulley.setGroundA(p);
		p.setObjectParent(this);
	}
	
	public void setGroundB(Point p) {
		
		pulley.setGroundB(p);
		p.setObjectParent(this);
	}
	
	public void setAnchorA(Point p) {
		
		pulley.setAnchorA(p);
		p.setObjectParent(this);
	}
	
	public void setAnchorB(Point p) {
		
		pulley.setAnchorB(p);
		p.setObjectParent(this);
	}
	@Override
	public void updatePosition(float x, float y, Point p) {
		pulley.update();
	}

	public Pulley getPullley() {return pulley;}
	@Override
	public ArrayList<Point> getChildList() { return null; }

	@Override
	public boolean isInside(float x, float y) {
		
		return pulley.isInside(x, y);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if(isInside(unproject(Gdx.input.getX(), Gdx.input.getY()))) {
			if(getActions().size == 0)addAction(Actions.fadeIn(0.3f, Interpolation.exp10));
			return this;
		}else {
			if(getActions().size == 0 && !isSelected())addAction(Actions.fadeOut(0.3f, Interpolation.exp10));
			return null;
		}
	}
	@Override
	public void select(int pointer) { 
		pulley.selectedFlag = true;
		showForm();
	}

	@Override
	public void unselect() {
		pulley.selectedFlag = false;
		hideForm();
	}

	@Override
	public SelectHandle getHandler() {
		return Sandbox.mainSelect;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		pulley.alphaCounter = getColor().a;
		pulley.render();
	}

	@Override
	public BaseOptionWindow getForm() {
		return form;
	}
	
	@Override
	public boolean updateValuesFromForm() {
		
		ratio = getForm().getOption("pulley-ratio").getValue();
		return true;
	}
}
