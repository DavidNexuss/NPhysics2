package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.HashMap;

public class GameState {

	static GState current = GState.START;
	
	
	public static boolean is(GState s) {
		
		return current == s;
	}

	
	public static void set(GState s) {
		
		current = s;
	}
}
