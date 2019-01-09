package com.nsoft.nphysics.sandbox.ui.option;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;

public class UIOptionSlider extends UIOptionComponent<Float, VisSlider>{

	private VisLabel text;
	private String[] args;
	
	private float min,max,stepSize;
	
	public UIOptionSlider(String ... args) {
		this(0,args.length - 1,1);
		this.args = args;
	}
	
	
	public UIOptionSlider(float min,float max,float stepSize) {
		
		super();
		
		this.max = max;
		this.min = min;
		this.stepSize = stepSize;
		
	}
	@Override
	public Float getValue() {
		return getComponent().getValue();
	}

	@Override
	public boolean setValue(Float newVal) {

		getComponent().setValue(newVal);
		l = newVal;
		return true;
	}

	@Override
	public void createComponent() {
		
		setComponent(new VisSlider(min, max, stepSize, false));
		text = new VisLabel();
	}
	
	@Override
	public void createCell(VisTable cell) {
		super.createCell(cell);
		cell.add(text).center();
	}
	
	boolean change;
	float l;
	@Override
	public void act() {
		
		if(args == null)text.setText(getValue() + "");
		else {
			text.setText(args[(int)getValue().floatValue()]);
			if(getValue() != l) {
				
				l = getValue();
				updateValue();
			}
		}
	}

}
