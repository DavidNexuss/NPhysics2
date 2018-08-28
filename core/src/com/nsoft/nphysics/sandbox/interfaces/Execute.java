package com.nsoft.nphysics.sandbox.interfaces;

import java.util.ArrayList;

public interface Execute<T> {
	
	public void operation(T obj);
	
	public default void execute(ArrayList<T> objects) {
		
		for (T t : objects) {
			
			operation(t);
		}
	}
}
