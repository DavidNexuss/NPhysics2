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

package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.sandbox.ui.UIStage;

public class GameState {

	public static GState current = GState.START;
	
	static {
		
		set(GState.START);
	}
	
	
	public static boolean is(GState s) {
		
		return current == s;
	}

	
	public static void set(GState s) {
		
		if(current.hasCleanTask())current.cleanTask.run();
		if(s.hasSetUpTask()) s.setUpTask.run();
		if(current.fl != null)current.fl.r.run();
		current = s;
		UIStage.setOperationText(current.description);
	}
}
