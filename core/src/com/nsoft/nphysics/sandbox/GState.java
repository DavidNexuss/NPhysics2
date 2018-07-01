package com.nsoft.nphysics.sandbox;

public enum GState {
	
	START("Bienvenido a NPhysics2"),
	CREATE_POINT("Crea un punto"),
	CREATE_SEGMENT("Crea un segmento"),
	CREATE_SEGMENTS("Crea segmentos"),
	DRAG_POINT("Mueve un punto"),
	HOOK_FORCE_ARROW("Dibuja el vector de fuerza"),
	HOOK_FORCE_ARROW2("Dibuja el vector de fuerza"), 
;	
	public String description;
	
	GState(String description){
		
		this.description = description;
	}
	
}