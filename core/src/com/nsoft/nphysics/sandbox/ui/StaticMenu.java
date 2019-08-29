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

import java.util.ArrayList;

import com.nsoft.nphysics.sandbox.interfaces.Execute;

public class StaticMenu{


	static Execute<FixedWindow> show = (obj)->{obj.show();};
	static Execute<FixedWindow> hide = (obj)->{obj.hide();};
	
	ArrayList<FixedWindow> windows = new ArrayList<>();
	
	public StaticMenu() {
		
	}
	public void addWindow(FixedWindow w){
		
		windows.add(w);
		w.setVisible(false);
		w.setColor(1, 1, 1, 0);
	}
	
	public void show() {
		
		show.execute(windows);
	}
	
	public void hide() {
		
		hide.execute(windows);
	}
	
}
