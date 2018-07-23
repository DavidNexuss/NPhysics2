package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;
import com.nsoft.nphysics.sandbox.interfaces.Form;

public class Option extends VisTable{

	VisTextField textfield;
	VisSlider slider;
	Label l;
	Form form;
	
	String[] args;
	public Option(String name) {
		
		pad(5);
		setName(name);
	}
	
	@Override
	public void act(float delta) {
		
		if(slider != null) {
			
			if(args == null)l.setText(slider.getValue() + "");
			else l.setText(args[(int)slider.getValue()]);
		}
		super.act(delta);
	}
	public float getValue() {
		
		if(textfield != null) {
			
			return Float.parseFloat(textfield.getText() == "" ? "0" : textfield.getText());
		}
		
		if(slider != null) {
			
			return slider.getValue();
		}
		
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
		throw new IllegalStateException();
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
	public Cell<Widget> addNumberInput(){
		
		textfield = new VisTextField("0.0");
		textfield.setFocusTraversal(false);
		textfield.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(VisTextField textField, char c) {
				
				if(c == '\r') {
					
					getForm().updateValuesFromForm();
				}
			
			}
		});
		return add(textfield);

	}	
	public static Option initEmtyOption(String name,String definition) {
		
		Option o = new Option(name);
		o.add(new Label(definition + ":", VisUI.getSkin())).expand().fill().uniform();
		return o;
	}
	public static Option createOptionNumber(String name,String definition) {
		
		Option o = initEmtyOption(name,definition);
		o.addNumberInput().expand().fill().uniform();
		return o;
	}
	
	public static Option createOptionSlider(String name,String definition,float min,float max,float step) {
		
		Option o = initEmtyOption(name,definition);
		o.addSliderInput(min, max, step).expand().fill().uniform();
		return o;
	}
	
	public static Option createOptionTypeSlider(String name,String definition,String ... args) {
		
		Option o = initEmtyOption(name, definition);
		o.addSliderTypeInput(args).expand().fill().uniform();
		return o;
	}
}
