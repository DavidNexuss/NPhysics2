package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.sandbox.interfaces.ClickIn;

public class SelectHandle {

	private static ClickIn selected;
	
	public static boolean isSelected(ClickIn object) {return selected == object;}
	public static boolean hasSelection() {return selected != null;}
	public static boolean setSelected(ClickIn newSelected) {
		
		if(hasSelection())selected.unselect();
		if(selected == newSelected) {
			
			selected = null;
			return false;
		}
		selected = newSelected;
		selected.select();
		return true;
	};
	public static ClickIn getSelected() {return selected;}
	public static void unselect() {
		
		if(hasSelection())selected.unselect();
		selected = null;
	}
}
