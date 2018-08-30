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
import com.nsoft.nphysics.sandbox.ui.option.UIOptionComponent;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionNumber;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionSlider;

public class Option extends VisTable{

	UIOptionComponent<?, ?> option;
	private boolean lastCheck = false;
	private Label l;
	Form form;
	
	public boolean canCopy = true;
	String[] args;
	public Option(String name) {
		
		pad(5);
		setName(name);
	}
	
	public boolean isNull() {return option.isNull();}
	public boolean isReady() {return option.isReady();}
	
	public void setEnable(boolean newEnable) {
		
		option.enableComponent(newEnable);
	}
	@Override
	public void act(float delta) {
		
		option.act();
		super.act(delta);
	}
	public <T> T getValue(Class<T> clas) {
		
		return (T)option.getValue();
	}
	public float getValue() {

		return getValue(Float.class);
	}
	
	public void setForm(Form f) {form = f;}
	public Form getForm() {return form;}
	
	public <T> Option setValue(T val) {
		
		UIOptionComponent<T, ?> a = (UIOptionComponent<T, ?>)option;
		a.setValue(val);
		return this;
	}
	public Option setValueAsFloat(float val) {
		
		UIOptionComponent<Float, ?> a = (UIOptionComponent<Float, ?>)option;
		a.setValue(val);
		return this;
	}
	
	public Cell<VisTable> addCheckBoxInput(String text){
		
		option = new UIOptionCheckBox(this);
		return add(option.getCell()).expand().fill();
	}
	public Cell<VisTable> addSliderInput(float min, float max,float step){
		
		option = new UIOptionSlider(this, min, max, step);
		return add(option.getCell()).expand().fill();
	}
	public Cell<VisTable> addSliderTypeInput(String ... strings){
		option = new UIOptionSlider(this, strings);
		return add(option.getCell()).expand().fill();
	}
	public Cell<VisTable> addNumberInput(){
		
		option = new UIOptionNumber(this);
		return add(option.getCell()).expand().fill();

	}
	
	
	public static Option initEmtyOption(String name) {
		
		Option o = new Option(name);
		String label = NDictionary.get(name);
		o.add(new Label(Util.capable(200, label) + ":", VisUI.getSkin())).expand().fill().uniform();
		return o;
	}
	
	public static Option createCheckBoxOption(String name) {
		
		Option o = initEmtyOption(name);
		o.addCheckBoxInput("").expand().fill().uniform();
		return o;
	}
	public static Option createOptionNumber(String name) {
		
		Option o = initEmtyOption(name);
		o.addNumberInput().expand().fill().uniform();
		return o;
	}
	
	public static Option createOptionSlider(String name,float min,float max,float step) {
		
		Option o = initEmtyOption(name);
		o.addSliderInput(min, max, step).expand().fill().uniform();
		return o;
	}
	
	public static Option createOptionTypeSlider(String name,String ... args) {
		
		Option o = initEmtyOption(name);
		o.addSliderTypeInput(args).expand().fill().uniform();
		return o;
	}
	
	public static Option createOptionColorSelector(String name) {
		
		Option o = initEmtyOption(name);
		
		return o;
	}
}
