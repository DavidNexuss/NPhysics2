package com.nsoft.nphysics.sandbox.interfaces;

public interface Ready {

	public boolean isReady();
	public default String readyError() {
		
		return getClass().getSimpleName().toLowerCase() + "-error";
	};
}
