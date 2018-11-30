package com.nsoft.nphysics.sandbox.interfaces;

import java.util.ArrayList;

/**
 * Interfície per poder executar una mateixa funció en un grup d'objectes de tipus <T> continguts
 * en una llista
 * @author David
 * @param <T> Tipus del objecte a ser executat
 */
public interface Execute<T> {
	
	public void operation(T obj);
	
	public default void execute(ArrayList<T> objects) {
		
		for (T t : objects) {
			
			operation(t);
		}
	}
}
