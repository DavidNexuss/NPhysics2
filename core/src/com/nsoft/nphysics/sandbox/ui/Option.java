package com.nsoft.nphysics.sandbox.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionCheckBox;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionColorPicker;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionComponent;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionNumber;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionSlider;

public class Option extends VisTable{

	UIOptionComponent<?, ?> component;
	private boolean lastCheck = false;
	private Label l;
	Form form;
	
	public boolean canCopy = true;
	String[] args;
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
