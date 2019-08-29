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

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.interfaces.AxisComponent;

public class ArrowComponent extends SimpleArrow implements AxisComponent { //TODO: INCOMPLETE

	public ArrowComponent(Vector2 start, Vector2 end) {
		super(start, end);
		
	}

	@Override
	public void onAngleChange(Vector2 pivot,float difAngleRad) {
		
		Vector2 newEnd = Util.rotPivot(pivot, getEnd(), difAngleRad);
		Vector2 newStart = Util.rotPivot(pivot, getStart(), difAngleRad);
		
		setStart(newStart.x, newStart.y);
		setEnd(newEnd.x,newEnd.y);
	}
}
