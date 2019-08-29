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

package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.NDictionary;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.sandbox.ui.option.UIOptionComponent;

public class Option extends VisTable{

	public static Color WARN = new Color(1f, 0.2f, 0.2f,1f);

	private UIOptionComponent<?, ?> component;
	private Form form;
	private boolean isWarned;
	public boolean canCopy = true;
	String[] args;
	
	public BaseOptionWindow parent;
	public Option(String name,UIOptionComponent<?, ?> component) {
		
		pad(5);
		setName(name);
		
		String label = NDictionary.get(name);
		add(new Label(Util.capable(200, label) + ":", VisUI.getSkin())).expand().fill().uniform();

		component.setMaster(this);
		component.init();
		add(component.getCell()).expand().fill().uniform();
		
		this.component = component;
		canCopy = component.canCopy;
	}
	
	public boolean isNull() {return component.isNull();}
	public boolean isReady() {return component.isReady();}
	
	public void setEnable(boolean newEnable) {
		
		component.enableComponent(newEnable);
	}
	@Override
	public void act(float delta) {
		
		component.act();
		super.act(delta);
	}
	public <T> T getValue(Class<T> clas) {
		
		return (T)component.getValue();
	}
	public float getValue() {

		return getValue(Float.class);
	}
	public String getValueAsString(){

		return getValue(String.class);
	}
	
	public void setForm(Form f) {form = f;}
	public Form getForm() {return form;}
	
	public <T> Option setValue(T val) {
		
		UIOptionComponent<T, ?> a = (UIOptionComponent<T, ?>)component;
		a.setValue(val);
		return this;
	}
	public Option setValueAsFloat(float val) {
		
		UIOptionComponent<Float, ?> a = (UIOptionComponent<Float, ?>)component;
		a.setValue(val);
		return this;
	}
	
	public void warn(){

		if(!isWarned){
			isWarned = true;
			component.getComponent().setColor(WARN);
		}
	}

	public void unwarn(){
		if(isWarned)
			component.getComponent().setColor(Color.WHITE);
	}

	public boolean isWarned(){

		return isWarned;
	}
}
