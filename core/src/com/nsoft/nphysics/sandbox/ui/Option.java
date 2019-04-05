package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionComponent;

public class Option extends VisTable{

	UIOptionComponent<?, ?> component;
	Form form;
	
	public boolean canCopy = true;
	String[] args;
	public BaseOptionWindow parent;
	public Option(String name,UIOptionComponent<?, ?> component) {
		
		pad(5);
		setName(name);
		
		String label = NDictionary.get(name);
		add(new Label(Util.capable(200, label) + ":", VisUI.getSkin())).expand().fill().uniform();
		
		component.setMaster(this);
		component.init();
		add(component.getCell()).expand().fill().uniform();
		
		this.component = component;
	}
	
	public boolean isNull() {return component.isNull();}
	public boolean isReady() {return component.isReady();}
	
	public void setEnable(boolean newEnable) {
		
		component.enableComponent(newEnable);
	}
	@Override
	public void act(float delta) {
		
		component.act();
		super.act(delta);
	}
	public <T> T getValue(Class<T> clas) {
		
		return (T)component.getValue();
	}
	public float getValue() {

		return getValue(Float.class);
	}
	
	public void setForm(Form f) {form = f;}
	public Form getForm() {return form;}
	
	public <T> Option setValue(T val) {
		
		UIOptionComponent<T, ?> a = (UIOptionComponent<T, ?>)component;
		a.setValue(val);
		return this;
	}
	public Option setValueAsFloat(float val) {
		
		UIOptionComponent<Float, ?> a = (UIOptionComponent<Float, ?>)component;
		a.setValue(val);
		return this;
	}
	
}
