package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class Option extends VisTable{

	VisTextField textfield;
	VisSlider slider;
	Label l;
	public Option(String name) {
		
		pad(5);
		setName(name);
	}
	
	@Override
	public void act(float delta) {
		
		if(slider != null) l.setText(slider.getValue() + "");
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
	public Cell<Widget> addSliderInput(float min, float max,float step){
		
		slider = new VisSlider(min, max, step, false);
		l = new Label("", VisUI.getSkin());
		return add(slider);
	}
	public Cell<Widget> addNumberInput(){
		
		textfield = new VisTextField("0.0");
		return add(textfield);

	}
	
	public static Option initEmtyOption(String name) {
		
		Option o = new Option(name);
		o.add(new Label(name + ":", VisUI.getSkin())).expand().align(Align.left);
		return o;
	}
	public static Option createOptionNumber(String name) {
		
		Option o = initEmtyOption(name);
		o.addNumberInput().expand().fill();
		return o;
	}
	
	public static Option createOptionSlider(String name,float min,float max,float step) {
		
		Option o = initEmtyOption(name);
		o.addSliderInput(min, max, step).expand().fill();
		o.add(o.l).padLeft(10f);
		return o;
	}
}
