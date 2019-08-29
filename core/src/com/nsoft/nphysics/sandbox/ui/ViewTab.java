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

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class ViewTab extends Tab{

	private String name;
	private Runnable action;
	
	public ViewTab(String name) {
		
		this(name,null);
	}
	
	public ViewTab(String name,Runnable action) {
		
		super(false,false);
		this.name = name;
		this.action = action;
	}
	
	public boolean hasAction() {return action != null;}
	public void runAction() {action.run();}
	@Override
	public String getTabTitle() {
		
		return name;
	}

	@Override
	public Table getContentTable() {
		
		return null;
	}

}
