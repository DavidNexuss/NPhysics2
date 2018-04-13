package com.nsoft.nphysics;

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

enum State{
	
	START("Bienvenido a NPhysics2"),
	HOOK_FORCE_ARROW("Dibuja el vector de fuerza"),
	HOOK_FORCE_ARROW2("Dibuja el vector de fuerza")
;	
	public String description;
	State(String description){
		
		this.description = description;
	}
}