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

import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.simulation.dynamic.SimulationPackage;

public class RawJoint extends Related{

	private PhysicalActor<?> A,B;
	

	public PhysicalActor<?> getPhysicalActorA() {return A;}
	public PhysicalActor<?> getPhysicalActorB() {return B;}

	public void setPhysicalActorA(PhysicalActor<?> a) {
		A = a;
		A.addRelated(this);
	}
	
	public void setPhysicalActorB(PhysicalActor<?> b) {
		B = b;
		B.addRelated(this);
	}

	@Override
	public void dispose() {
		
		SimulationPackage.rawJoints.remove(this);
	}
	
	@Override
	public boolean remove() {
		
		destroy();
		RawJoint j = this;
		if(j instanceof Form) {
			
			((Form)j).hideForm();
		}
		return true;
	}
	public void defaultInit() {
		
		setPhysicalActorA((PhysicalActor<?>) Sandbox.mainSelect.getSelecteds().get(0));
		setPhysicalActorB((PhysicalActor<?>) Sandbox.mainSelect.getSelecteds().get(1));
	}
	
}