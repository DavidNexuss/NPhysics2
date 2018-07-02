package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.HashMap;

public class GameState {

	static GState current = GState.START;
	
	
	public static boolean is(GState s) {
		
		return current == s;
	}

	
	public static void set(GState s) {
		
		if(current.hasCleanTask())current.cleanTask.run();
		if(s.hasSetUpTask()) s.setUpTask.run();
		current = s;
	}
}
