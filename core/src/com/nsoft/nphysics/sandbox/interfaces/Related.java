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
