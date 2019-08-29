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

import com.badlogic.gdx.math.Vector2;

public interface Pointer {

	Vector2 getStart();
	Vector2 getEnd();
	
	default void setStart	(Vector2 newStart) 	{getStart().set(newStart);}
	default void setStart	(float x,float y) 	{getStart().set(x,y);}
	default void setEnd		(Vector2 newEnd) 	{getEnd().set(newEnd);}
	default void setEnd		(float x,float y) 	{getEnd().set(x,y);}
	
	default Vector2 getModulus() {return new Vector2(getEnd()).sub(getStart());}
}
