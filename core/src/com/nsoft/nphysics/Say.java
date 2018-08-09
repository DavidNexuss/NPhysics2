package com.nsoft.nphysics;

public interface Say {

	public default String say(Object s) {
		
		System.out.println(getClass().getSimpleName() + "#" + hashCode() + ": " + s);
		return s.toString();
	}
}
