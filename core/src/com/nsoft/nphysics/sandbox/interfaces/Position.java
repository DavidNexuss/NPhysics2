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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public interface Position {

	public float getX();
	public float getY();
	
	public default Vector2 getVector() {
		
		return new Vector2(getX(), getY());
	}
	
	public default float getLen2() {
		
		return getX()*getX() + getY()*getY();
	}
	public default float getLen() {
		
		return (float) Math.sqrt(getLen2());
	}
	public default float getAngleRad() {
		
		return MathUtils.atan2(getY(), getX());
	}
	public default float getAngle() {
		
		return getAngleRad()*MathUtils.radDeg;
	}
}

