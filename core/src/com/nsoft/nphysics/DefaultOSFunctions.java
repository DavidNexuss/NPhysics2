package com.nsoft.nphysics;

/**
 * Clase que defineix 
 * @author David
 */
public class DefaultOSFunctions implements SpecificOSFunctions{

	@Override
	public void playSound(String name) {}
	
	@Override
	public Thread getCurrentThread() {
		return null;
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return null;
	}
}
