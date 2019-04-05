package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.drawables.Spring;
import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.interfaces.Parent;
import com.nsoft.nphysics.sandbox.interfaces.Position;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;
import com.nsoft.nphysics.sandbox.interfaces.Ready;
import com.nsoft.nphysics.sandbox.interfaces.Removeable;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionNumber;

public class SpringComponent extends RawJoint implements ClickIn,Parent<Point>,Form,Ready,Removeable{

	public Spring spring = new Spring();
	private DynamicWindow form;
	private float k = 1;
	
	public static SpringComponent tmp;
	
	public SpringComponent() {
		defaultInit();
		addInput();
		initForm();
	}
	
	@Override
	public boolean isReady() {
		
		return spring.isEnd() && 
		((getPhysicalActorA().isStatic() && !getPhysicalActorB().isStatic()) ^ 
		(!getPhysicalActorA().isStatic() && getPhysicalActorB().isStatic()));
	}
	
	private void initForm() {
		
		form = DynamicWindow.createDefaultWindowStructure(NDictionary.get("spring-form"),this);
		form.addOption(new Option("k-constant", new UIOptionNumber()));
		
		getForm().getOption("k-constant").setValue(1f);
		
		form.setVisible(false);
		form.setSize(450, form.getPrefHeight());
		NPhysics.ui.addActor(form);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		spring.render();
	}
	public void addAnchorA(Point A) {
		if(spring.isEnd()) throw new IllegalStateException();
		spring.addAnchorA(A);
		A.setObjectParent(this);
	}
	
	public void addAnchorB(Point B) {
		if(spring.isEnd())throw new IllegalStateException();
		spring.addAnchorB(B);
		B.setObjectParent(this);
	}
	
	public Position getAnchorA() { return spring.getAnchorA();}
	public Position getAnchorB() { return spring.getAnchorB();}
	
	@Override
	public void updatePosition(float x, float y, Point p) {
		
		spring.updateSpring();
	}
	
	public float getKConstant() {return k;}

	@Override
	public ArrayList<Point> getChildList() {
		return null;
	}

	@Override
	public boolean isInside(float x, float y) {

		return spring.isInside(x, y);
	}
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY())) ? this : null;
	}
	
	@Override
	public void select(int pointer) {
		spring.selectedFlag = true;
		showForm();
	}

	@Override
	public void unselect() {
		spring.selectedFlag = false;
		hideForm();
	}

	@Override
	public SelectHandle getHandler() {
		return Sandbox.mainSelect;
	}

	@Override
	public BaseOptionWindow getForm() { return form; }

	@Override
	public void updateValuesToForm() {}

	@Override
	public void updateValuesFromForm() {
		
		k = getForm().getOption("k-constant").getValue();
	}
}
