package com.nsoft.nphysics.sandbox.ui.option;

import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.nsoft.nphysics.sandbox.ui.Option;

public class UIOptionCheckBox extends UIOptionComponent<Float, VisCheckBox>{

	private boolean lastCheck;
	
	public UIOptionCheckBox(Option master) {
		this(master,false);
	}
	
	public UIOptionCheckBox(Option master,boolean initial) {
		super(master);
		lastCheck = initial;
		init();
	}
	@Override
	public Float getValue() {
		return getComponent().isChecked() ? 1f : 0;
	}

	@Override
	public boolean setValue(Float newVal) {
		getComponent().setChecked(newVal == 1);
		return true;
	}

	@Override
	public void createComponent() {
		
		setComponent(new VisCheckBox(""));
		getComponent().setChecked(lastCheck);
	}
	
	@Override
	public void act() {
		
		if(lastCheck != getComponent().isChecked()) {
			
			getForm().updateValuesFromForm();
			lastCheck = getComponent().isChecked();
		}
	}
	
}

