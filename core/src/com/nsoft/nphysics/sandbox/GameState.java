package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.HashMap;

public class GameState {

	static State current = State.START;
	
	
	public static boolean is(State s) {
		
		return current == s;
	}

	
	public static void set(State s) {
		
		current = s;
	}
}

enum State {
	
	START("Bienvenido a NPhysics2"),
	CREATE_POINT("Crea un punto"),
	CREATE_SEGMENT("Crea un segmento"),
	CREATE_SEGMENTS("Crea segmentos"),
	DRAG_POINT("Mueve un punto"),
	HOOK_FORCE_ARROW("Dibuja el vector de fuerza"),
	HOOK_FORCE_ARROW2("Dibuja el vector de fuerza"), 
;	
	public String description;
	State(String description){
		
		this.description = description;
	}
}