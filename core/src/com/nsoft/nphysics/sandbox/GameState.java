package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;
import java.util.HashMap;

import com.nsoft.nphysics.sandbox.ui.UIStage;

public class GameState {

	static GState current = GState.START;
	
	static {
		
		set(GState.START);
	}
	
	
	public static boolean is(GState s) {
		
		return current == s;
	}

	
	public static void set(GState s) {
		
		if(current.hasCleanTask())current.cleanTask.run();
		if(s.hasSetUpTask()) s.setUpTask.run();
		current = s;
		UIStage.setOperationText(current.description);
	}
}
