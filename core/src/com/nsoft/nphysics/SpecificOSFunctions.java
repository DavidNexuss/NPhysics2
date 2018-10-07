package com.nsoft.nphysics;

public interface SpecificOSFunctions {

	public void playSound(String name);
	public Thread getCurrentThread();
	public StackTraceElement[] getStackTrace();
}
