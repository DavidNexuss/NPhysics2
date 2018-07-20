package com.nsoft.nphysics.sandbox;

import java.util.ArrayList;

import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.ui.UIStage;

public class SelectHandle {

	private ArrayList<ClickIn> selecteds = new ArrayList<>();
	private boolean multiSelection = false;
	
	public ClickIn getFirstSelected() {
		
		return hasSelection() ? selecteds.get(0) : null;
	}
	
	public void setFirstSelected(ClickIn c) {
		
		selecteds.add(0, c);
	}
	
	public boolean isFirstSelected(ClickIn c) {
		
		return hasSelection() ? selecteds.get(0) == c : false;
	}
	public boolean isSelected(ClickIn object) {
		return selecteds.contains(object);
	}
	public boolean hasSelection() {
		
		return selecteds.size() != 0;
	}
	
	public boolean setSelected(ClickIn newSelected) {
		
		return setSelected(newSelected, 0);
	}
	public boolean setSelected(ClickIn newSelected,int pointer) {
		
		return setSelected(newSelected, pointer, false);
	};
	
	public boolean setSelected(ClickIn newSelected,int pointer,boolean force) {
		
		if(Sandbox.SHIFT) {
			
			if(isSelected(newSelected)) {
				
				choose(null, selecteds.indexOf(newSelected));
				newSelected.unselect();
			}else {
				
				choose(newSelected, selecteds.size());
				newSelected.select(pointer);
			}
			return true;
		}
		if(!force) {
			
			if(hasSelection())getFirstSelected().unselect();
			if(isFirstSelected(newSelected)) {
				
				unSelectFirst();
				return false;
			}
		}
		
		choose(newSelected,0);
		getFirstSelected().select(pointer);
		return true;
	}
	public ArrayList<ClickIn> getSelecteds() {return selecteds;}
	
	public void cleanArray() {
		
		ArrayList<ClickIn> clean = new ArrayList<>();
		
		for (ClickIn clickIn : selecteds) {
			
			if(clickIn != null)clean.add(clickIn);
		}
		
		selecteds = clean;
	}
	public void unSelect() {
		
		for (int i = 0; i < selecteds.size(); i++) {
			
			unSelect(i);
		}
		
		cleanArray();
	}
	public void unSelectFirst() {
		
		unSelect(0);
		cleanArray();
	}
	
	public void unSelect(ClickIn in) {
		
		unSelect(selecteds.indexOf(in));
	}
	public void unSelect(int index) {
		
		if(selecteds.get(index) == null) return;
		if(hasSelection()) {
			getSelecteds().get(index).unselect();
		}
		choose(null,index);
	}
	private void choose(ClickIn in,int index) {
		
		if(!hasSelection() || index >= getSelecteds().size()) {
			
			selecteds.add(in);
			checkSelectedsState();
			return;
		}
		if(getSelecteds().get(index) instanceof Handler) 
			((Handler)getSelecteds().get(index)).getSelectHandleInstance().unSelect();
		
		getSelecteds().set(index, in);
		checkSelectedsState();
	}
	
	private void checkSelectedsState() {
		
		updateDoubleContextMenu();
	}
	
	private void updateDoubleContextMenu() {
		
		if(selecteds.size() == 2 && selecteds.get(0) instanceof PolygonActor && selecteds.get(1) instanceof PolygonActor)
			UIStage.doubleContextMenu.show();
		else UIStage.doubleContextMenu.hide();
	}
}
