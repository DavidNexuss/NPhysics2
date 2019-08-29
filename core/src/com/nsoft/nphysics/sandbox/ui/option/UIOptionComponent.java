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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.ui.FixedWindow;
import com.nsoft.nphysics.sandbox.ui.Option;
import com.nsoft.nphysics.sandbox.ui.WorldOptionManager;

public abstract class UIOptionComponent<T,A extends Actor>{

	private A component;
	private VisTable cell;
	private boolean enable = true;
	private boolean isnull = false;
	private boolean ready = true;
	private Option master;
	
	public abstract T getValue();
	public abstract boolean setValue(T newVal);
	public abstract void createComponent();
	
	public boolean canCopy = true;
	public UIOptionComponent() {}
	
	public void init() {
		if(master == null) throw new IllegalStateException();
		createComponent();
		initCell();
	}
	private void initCell() {
		
		cell = new VisTable();
		createCell(cell);
	}
	public void createCell(VisTable cell) {
		
		cell.add(component).expand().fill().row();
	}
	public VisTable getCell() {
		
		return cell;
	}
	
	public A getComponent() {return component;}
	public void setComponent(A neww) {component = neww;}
	
	public void setMaster(Option o) {master = o;}
	
	public boolean isEnabled() {return enable;}
	
	public void setNull(boolean newnull) {
		isnull = newnull;
		if(enable) {
			component.setColor(newnull ? Color.YELLOW : Color.WHITE);
		}
	}
	public boolean isNull() {return isnull;}
	
	public void setReady(boolean ready) {
		this.ready = ready;
		if(enable) {
			component.setColor(ready ? Color.WHITE : Color.RED);
		}
	}
	public boolean isReady() {
		
		return ready;
	}

	public void enableComponent(boolean enable) {
		
		if(enable) {
			
			component.setColor(Color.WHITE);
			component.setTouchable(Touchable.enabled);
		}else {
			
			component.setColor(Color.GRAY);
			component.setTouchable(Touchable.disabled);
		}
		
		this.enable = enable;
		
		setReady(isReady());
		setNull(isNull());
	}
	
	public Form getForm() { return master.getForm(); }
	
	public void updateValue() {
		
		if(master.parent instanceof FixedWindow) {
			
			WorldOptionManager.current = master.parent;
			WorldOptionManager.current.getForm().updateValuesFromForm();
		}else {
			getForm().updateValuesFromForm();
		}
	}
	public void act() {}
	
}
