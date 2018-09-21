package com.nsoft.nphysics.sandbox.interfaces;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;

public interface Form {

	public BaseOptionWindow getForm();
	
	public default void updateValuesFromForm(HashMap<String, Option> optionsMap) {};
	public default void updateValuesFromForm() {
		
		if(sendRaw()) updateValuesFromForm(getForm().getOptions());
	};
	
	public default boolean sendRaw() {return false;}
	public void updateValuesToForm();
	
	public default void showForm() {
		
		if(! (getForm() instanceof DynamicWindow)) return;
		getForm().setPosition(Gdx.graphics.getWidth() - getForm().getWidth() - 80, Gdx.graphics.getHeight() - getForm().getHeight() - 80);
		updateValuesToForm();
		DynamicWindow.showWindow((DynamicWindow) getForm());
	}
	
	public default void hideForm() {
		
		if(! (getForm() instanceof DynamicWindow)) return;
		DynamicWindow.hideWindow((DynamicWindow) getForm());
	}
}
