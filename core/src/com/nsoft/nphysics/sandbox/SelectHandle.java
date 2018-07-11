package com.nsoft.nphysics.sandbox;

import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Handler;

public class SelectHandle {

	private ClickIn selected;
	
	public boolean isSelected(ClickIn object) {return selected == object;}
	public boolean hasSelection() {return selected != null;}
	public boolean setSelected(ClickIn newSelected) {
		
		if(hasSelection())selected.unselect();
		if(selected == newSelected) {
			
			choose(null);
			return false;
		}
		choose(newSelected);
		selected.select();
		return true;
	};
	public ClickIn getSelected() {return selected;}
	
	public void unselect() {
		
		if(hasSelection())selected.unselect();
		choose(null);
	}
	
	private void choose(ClickIn in) {
		
		if(selected instanceof Handler) ((Handler)selected).getSelectHandleInstance().unselect();
		selected = in;
	}
}