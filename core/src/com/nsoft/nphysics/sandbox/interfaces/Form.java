/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.sandbox.interfaces;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.nsoft.nphysics.sandbox.AxisSupport;
import com.nsoft.nphysics.sandbox.ui.BaseOptionWindow;
import com.nsoft.nphysics.sandbox.ui.DynamicWindow;
import com.nsoft.nphysics.sandbox.ui.Option;
/**
 * Interf�cie que dona la propietat a una classe de tindre un formulari.
 * @see {@link AxisSupport} {@link ObjectChildren}
 * @author David
 */
public interface Form {

	public BaseOptionWindow getForm();
	
	public default boolean updateValuesFromForm(HashMap<String, Option> optionsMap) { return true;};
	public default boolean updateValuesFromForm() {
		
		if(sendRaw()) return updateValuesFromForm(getForm().getOptions());
		return true;
	};
	
	public default boolean sendRaw() {return false;}
	public default void updateValuesToForm() {}
	
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

	public default String getValueAsString(String optName) {
		
		return getForm().getOption(optName).getValueAsString();
	}
}
