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
			
			updateValue();
			lastCheck = getComponent().isChecked();
		}
	}
	
}

