package com.nsoft.nphysics.simulation.dsl;

import java.lang.reflect.Field;

import com.badlogic.gdx.utils.reflect.ReflectionException;

public interface Dev {

	public int uid = (int) (Math.random()*Integer.MAX_VALUE);
	public default void say(String s) {
		
		System.out.println(getClass().getSimpleName() + ": " + getid(this) + " > " + s);
	}
	
	public static String getid(Object o) {
		
		return o.toString().substring(o.toString().indexOf("@"));
	}
	/*
	public default void val(String field) {
		
		try {
			
			Field t = this.getClass().getField(field);
			say(t.getName() + ": " + t.get(this));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
