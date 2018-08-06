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
import com.nsoft.nphysics.Dictionary;
import com.nsoft.nphysics.sandbox.interfaces.Form;

public class Option extends VisTable{

	VisTextField textfield;
	VisSlider slider;
	VisCheckBox checkbox; private boolean lastCheck = false;
	private Label l;
	Form form;
	private boolean enable = true;
	private boolean ready = true;
	
	String[] args;
	public Option(String name) {
		
		pad(5);
		setName(name);
	}
	
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
		throw new IllegalStateException();
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
	public Cell<Widget> addNumberInput(){
		
		textfield = new VisTextField("0.0");
		textfield.setFocusTraversal(false);
		textfield.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(VisTextField textField, char c) {
				
				if(c == '\r') {
					
					getForm().updateValuesFromForm();
				}
				
				if(isNull(textfield.getText())) {
					
					textfield.setColor(Color.YELLOW);
					ready = true;
					return;
				}
				if(!isMessageNumber()) {
					
					textfield.setColor(Color.RED);
					ready = false;
				}else {
					
					textfield.setColor(Color.WHITE);
					ready = true;
				}
			
			}
		});
		return add(textfield);

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
	static GlyphLayout layout;
	static BitmapFont current;
	private static String capable(float dimensions,String text) {
		
		layout = new GlyphLayout(current, text);
		
		String[] parts = text.split(" ");
		if(parts.length == 1) return parts[0];
		
		ArrayList<String> lines = new ArrayList<>();
		lines.add(parts[0]);
		int c = 0;
		
		for (int i = 1; i < parts.length; i++) {
			
			if(new GlyphLayout(current, lines.get(c) + " " + parts[i]).width > dimensions) {
				
				lines.add(parts[i]);
				c++;
			}else {
				
				lines.set(c, lines.get(c) + " " + parts[i]);
			}
		}
		
		String fstring = "";
		for (String string : lines) {
			
			fstring += string != lines.get(lines.size() - 1) ? string + "\n" : string;
		}
		
		return fstring;
	}
	public static Option initEmtyOption(String name) {
		
		Option o = new Option(name);
		String label = Dictionary.get(name);
		current = new Label("",VisUI.getSkin()).getStyle().font;
		o.add(new Label(capable(200, label) + ":", VisUI.getSkin())).expand().fill().uniform();
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
}
