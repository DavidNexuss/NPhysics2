/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.sandbox.ui.option;

import com.nsoft.nphysics.sandbox.ui.Option;

public class Options {
		
	public static Option createCheckBoxOption(String name) {
			
		return new Option(name, new UIOptionCheckBox(false));
	}
	public static Option createOptionNumber(String name) {
			
		return new Option(name, new UIOptionNumber());
	}
	public static Option createOptionField(String name) {
		
		return new Option(name, new UIOptionField());	
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
