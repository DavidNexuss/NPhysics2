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

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nsoft.nphysics.sandbox.PhysicalActor;

public abstract class Related extends Actor{

	private ArrayList<PhysicalActor<?>> relateds = new ArrayList<>();
	
	public void addRelation(PhysicalActor<?> n) {relateds.add(n);}
	
	public void destroy() {
		
		for (PhysicalActor<?> physicalActor : relateds) {
			
			physicalActor.removeFromRelatedList(this);
		}
		
		super.remove();
		dispose();
	}
	
	public abstract void dispose();
}
