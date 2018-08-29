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
import com.nsoft.nphysics.sandbox.interfaces.UIOptionComponent;

public class Option extends VisTable{

	VisTextField textfield;
	VisSlider slider;
	VisCheckBox checkbox; 
	UIOptionComponent<Float, ?> option;
	private boolean lastCheck = false;
	private Label l;
	Form form;
	
	private boolean enable = true;
	private boolean ready = true;
	private boolean nullValaue = false;
	
	public boolean canCopy = true;
	String[] args;
	public Option(String name) {
		
		pad(5);
		setName(name);
	}
	
	public boolean isNull() {return nullValaue;}
	public boolean isReady() {return ready;}
	
	public void setEnable(boolean newEnable) {
		
		enable = newEnable;
		enableComponent(textfield, newEnable);
		enableComponent(slider, newEnable);
		enableComponent(checkbox, newEnable);
	}
	
	private void enableComponent(Actor a,boolean enable) {
		
		if(a == null)return;
		if(enable) {
			
			a.setColor(Color.WHITE);
			a.setTouchable(Touchable.enabled);
		}else {
			
			a.setColor(Color.GRAY);
			a.setTouchable(Touchable.disabled);
		}
	}
	@Override
	public void act(float delta) {
		
		if(slider != null) {
			
			if(args == null)l.setText(slider.getValue() + "");
			else l.setText(args[(int)slider.getValue()]);
		}
		
		if(checkbox != null) {
			
			if(lastCheck != checkbox.isChecked()) {
				
				lastCheck = checkbox.isChecked();
				getForm().updateValuesFromForm();
			}
		}
		super.act(delta);
	}
	public float getValue() {
		
		if(textfield != null) {
			
			return isNull(textfield.getText()) ? Float.MAX_VALUE : Float.parseFloat(textfield.getText() == "" ? "0" : textfield.getText());
		}
		
		if(slider != null) return slider.getValue();
		
		if(checkbox != null) return checkbox.isChecked() ? 1 : 0;
		
		if(option != null) return option.getValue();
		throw new IllegalStateException();
	}
	
	public void setForm(Form f) {form = f;}
	public Form getForm() {return form;}
	
	public Option setValue(float val) {
		

		if(textfield != null) {
			
			textfield.setText(val + "");
			return this;
		}
		
		if(slider != null) {
			
			slider.setValue(val);
			return this;
		}
		
		if(checkbox != null) {
			
			checkbox.setChecked(val == 1);
			return this;
		}

		if(option != null) {
			
			option.setValue(val);
		}
		return this;
	}
	
	public Cell<VisCheckBox> addCheckBoxInput(String text){
		
		checkbox = new VisCheckBox(text);
		return add(checkbox);
	}
	public Cell<Table> addSliderInput(float min, float max,float step){
		
		VisTable t = new VisTable();
		
		slider = new VisSlider(min, max, step, false);
		l = new Label("", VisUI.getSkin());
		t.add(slider).expand().fill();
		t.add(l).expand().fill();
		
		return add(t);
	}
	public Cell<Table> addSliderTypeInput(String ... strings){
		
		VisTable t = new VisTable();
		this.args = strings;
		slider = new VisSlider(0, strings.length - 1,1, false);
		l = new Label("", VisUI.getSkin());
		t.add(slider).expand().fill().row();
		t.add(l).expand().fill().row();
		
		return add(t);
	}
	public Cell<Actor> addNumberInput(){
		
		option = new UIOptionNumber(this);
		VisTextField a = (VisTextField)option.getComponent();
		return add(option.getCell());

	}
	
	public void setNull(boolean nullv) {
		
		if(textfield != null) {
			
			if (nullv) {
				
				textfield.setColor(Color.YELLOW);
				textfield.setText("NULL");
			}else {
				
				textfield.setColor(Color.WHITE);
				textfield.setText("0.0");
			}
		}
		
		nullValaue = nullv;
	}
	private boolean isNull(String n) {
		
		return n.equals("NULL");
	}
	private boolean isNumber(char c) {
		
		return c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '0' || c == '.' || c == '-'; 
	}
	
	private boolean isMessageNumber() {
		
		if(textfield.getText().length() == 0) return false;
		for (int i = 0; i < textfield.getText().length(); i++) {
			
			char c = textfield.getText().charAt(i);
			if(!isNumber(c)) return false;
		}
		
		return true;
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
