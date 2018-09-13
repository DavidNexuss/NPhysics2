package com.nsoft.nphysics.sandbox.ui;

import com.nsoft.nphysics.sandbox.interfaces.Form;

public class WorldOptionManager{

	static Form dynamicSimulationOptions;
	
	public static void init() {
		
	}
	static {
		
		dynamicSimulationOptions = new Form() {
			
			@Override
			public void updateValuesToForm() {
				
			}
			
			@Override
			public void updateValuesFromForm() {
				
			}
			
			@Override
			public DynamicWindow getForm() {
				return null;
			}
		};
	}
}
