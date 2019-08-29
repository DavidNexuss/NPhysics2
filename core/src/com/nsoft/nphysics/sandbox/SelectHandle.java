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

package com.nsoft.nphysics.sandbox;

import java.util.Stack;

import com.nsoft.nphysics.sandbox.interfaces.ClickIn;
import com.nsoft.nphysics.sandbox.interfaces.Handler;
import com.nsoft.nphysics.sandbox.ui.UIStage;
/**
 * Classe encarregada de manejar els objectes per seleccionar i els selecionats.
 * @author David
 */
public class SelectHandle {

	private Stack<ClickIn> selecteds = new Stack<>();
	private Class<?> current_type;
	public boolean multiSelection = false;
	
	public ClickIn getLastSelected() {
		
		return hasSelection() ? selecteds.lastElement() : null;
	}

	public boolean isLastSelected(ClickIn c) {
		
		return hasSelection() ? getLastSelected() == c : false;
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
		
		if(!hasSelection()) {
			
			if(newSelected instanceof PhysicalActor<?>) current_type = PhysicalActor.class;
			else current_type = newSelected.getClass();
		}
		
		if(Sandbox.SHIFT || multiSelection) {
			
			if(isSelected(newSelected)) {
				
				choose(null, selecteds.indexOf(newSelected));
				newSelected.unselect();
			}else {
				
				if(current_type.isInstance(newSelected)) {
					
					choose(newSelected, selecteds.size());
					newSelected.select(pointer);
				}
			}
			return true;
		}
		if(!force) {
			
			if(hasSelection() && getLastSelected() != null)getLastSelected().unselect();
			if(isSelected(newSelected)) {
				
				unSelectLast();
				return false;
			}
		}
		
		choose(newSelected,selecteds.size() -1);
		getLastSelected().select(pointer);
		return true;
	}
	public Stack<ClickIn> getSelecteds() {return selecteds;}
	
	public void cleanArray() {
		
		Stack<ClickIn> clean = new Stack<>();
		
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
	public void unSelectLast() {
		
		unSelect(getLastSelected());
		cleanArray();
	}
	
	public void unSelect(ClickIn in) {
		
		if(in == null)return;
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
		
		if(selecteds.size() == 2 && selecteds.get(0) instanceof PhysicalActor<?> && selecteds.get(1) instanceof PhysicalActor<?>)
			UIStage.doubleContextMenu.show();
		else UIStage.doubleContextMenu.hide();
	}
}
