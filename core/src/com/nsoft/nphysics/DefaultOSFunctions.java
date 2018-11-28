package com.nsoft.nphysics;

/**
 * Clase que defineix funcions predefinides per a un sistema operatiu no reconegut
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
