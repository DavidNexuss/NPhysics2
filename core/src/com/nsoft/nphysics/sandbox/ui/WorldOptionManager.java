package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;
import java.util.Map.Entry;

import com.nsoft.nphysics.NDictionary.Languages;
import com.nsoft.nphysics.Options;
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
				break;
			case "worldwait":
				SolveJob.waitTime =e.getValue().getValue();
				break;
			case "chooselang":
				
				float v = e.getValue().getValue();
				if(v == 0)Options.options.currentLanguage = Languages.ESP;
		   else if(v == 1)Options.options.currentLanguage = Languages.CAT;
		   else if(v == 2)Options.options.currentLanguage = Languages.ENG;
				
				Options.save();
				break;
			case "epsilon_exp":
				
				SolveJob.exp = e.getValue().getValue();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void updateValuesToForm() {
		
		
	}
}
