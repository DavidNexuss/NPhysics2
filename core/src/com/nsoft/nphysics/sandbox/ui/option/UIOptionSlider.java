package com.nsoft.nphysics.sandbox.ui.option;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.sandbox.ui.Option;

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
	@Override
	public void act() {
		
		if(args == null)text.setText(getValue() + "");
		else text.setText(args[(int)getValue().floatValue()]);
	}

}
