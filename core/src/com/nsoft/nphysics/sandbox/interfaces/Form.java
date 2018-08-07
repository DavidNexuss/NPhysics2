package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.Gdx;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;

public interface Form {

	public DynamicWindow getForm();
	public void updateValuesFromForm();
	public void updateValuesToForm();
	
	public default void showForm() {
			
		getForm().setPosition(Gdx.graphics.getWidth() - getForm().getWidth() - 80, Gdx.graphics.getHeight() - getForm().getHeight() - 80);
		updateValuesToForm();
		DynamicWindow.showWindow(getForm());
	}
	
	public default void hideForm() {
		
		DynamicWindow.hideWindow(getForm());
	}
}
