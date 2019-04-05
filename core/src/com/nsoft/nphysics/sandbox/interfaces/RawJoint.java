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