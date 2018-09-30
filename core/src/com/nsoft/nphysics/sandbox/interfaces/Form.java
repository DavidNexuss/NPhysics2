package com.nsoft.nphysics.sandbox.interfaces;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.nsoft.nphysics.sandbox.AxisSupport;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;
/**
 * Interfície que dona la propietat a una classe de tindre un formulari.
 * @see {@link AxisSupport} {@link ObjectChildren}
 * @author David
 */
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
	
	public default void setValue(String optName,Object val) {
		
		getForm().getOption(optName).setValue(val);
	}
	
	public default <T> T getValue(String optName,Class<T> cls) {
		
		return getForm().getOption(optName).getValue(cls);
	}
	
	public default float getValue(String optName) {
		
		return getForm().getOption(optName).getValue();
	}
}
