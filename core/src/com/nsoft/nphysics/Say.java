package com.nsoft.nphysics.simulation.dsl;

public interface Say {

	public default void say(Object s) {
		
		System.out.println(getClass().getSimpleName() + "#" + hashCode() + ": " + s);
	}
}
