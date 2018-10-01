package com.nsoft.nphysics.sandbox;

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
		if(current.fl != null)current.fl.r.run();
		current = s;
		UIStage.setOperationText(current.description);
	}
}
