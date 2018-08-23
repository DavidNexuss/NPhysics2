package com.nsoft.nphysics;
/**
 * Interfas per fer debugging
 * @author David
 */
public interface Say {

	public default String say(Object s) {
		
		System.out.println(getClass().getSimpleName() + "#" + hashCode() + ": " + s);
		return s.toString();
	}
}
