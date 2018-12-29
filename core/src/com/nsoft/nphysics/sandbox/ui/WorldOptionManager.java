package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Form;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;
import com.nsoft.nphysics.simulation.dynamic.SolveJob;

public class WorldOptionManager implements Form{

	public static BaseOptionWindow current;
	@Override
	public BaseOptionWindow getForm() {
		
		return current;
	}

	@Override
	public boolean sendRaw() {
		return true;
	}
	@Override
	public void updateValuesFromForm(HashMap<String, Option> optionsMap) {
		
		for (Entry<String, Option> e: optionsMap.entrySet()) {
		

			System.out.println(e.getValue().getValue());
			switch (e.getKey()) {
			case "gravity":
				
				SimulationStage.gravity.set(0, e.getValue().getValue());
				break;
			case "gridnewtonscale":
				
				Util.NEWTON_FACTOR = e.getValue().getValue();
				break;
			case "gridmeterscale":
				
				Util.METER_FACTOR = e.getValue().getValue();
				break;
			case "gridscale": 
				Util.UNIT = (int) e.getValue().getValue();
			case "worldwait":
				SolveJob.waitTime =e.getValue().getValue();
			default:
				break;
			}
		}
	}

	@Override
	public void updateValuesToForm() {
		
		
	}
}
