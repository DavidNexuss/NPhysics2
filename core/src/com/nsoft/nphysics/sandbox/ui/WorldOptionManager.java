package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.nsoft.nphysics.sandbox.interfaces.Form;

public class WorldOptionManager implements Form{

	
	@Override
	public BaseOptionWindow getForm() {
		return null;
	}

	@Override
	public boolean sendRaw() {
		return true;
	}
	@Override
	public void updateValuesFromForm(HashMap<String, Option> optionsMap) {

	}

	@Override
	public void updateValuesToForm() {
		
		
	}
}
