package com.nsoft.nphysics.sandbox.ui.option;

import com.nsoft.nphysics.sandbox.ui.Option;

public class Options {
		
	public static Option createCheckBoxOption(String name) {
			
		return new Option(name, new UIOptionCheckBox(false));
	}
	public static Option createOptionNumber(String name) {
			
		return new Option(name, new UIOptionNumber());
	}
		
	public static Option createOptionSlider(String name,float min,float max,float step) {
			
		return new Option(name, new UIOptionSlider(min, max, step));
	}
		
	public static Option createOptionTypeSlider(String name,String ... args) {
		
		return new Option(name, new UIOptionSlider(args));
	}
		
	public static Option createOptionColorSelector(String name) {
			
		return new Option(name, new UIOptionColorPicker());
	}
	
}
