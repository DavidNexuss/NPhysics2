package com.nsoft.nphysics.sandbox.ui.option;

import com.kotcrab.vis.ui.widget.VisCheckBox;

public class UIOptionCheckBox extends UIOptionComponent<Float, VisCheckBox>{

	private boolean lastCheck;
	
	public UIOptionCheckBox() {
		this(false);
	}
	
	public UIOptionCheckBox(boolean initial) {
		super();
		lastCheck = initial;
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

