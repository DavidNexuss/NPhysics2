package com.nsoft.nphysics.sandbox.interfaces;

import java.util.ArrayList;

import com.nsoft.nphysics.sandbox.SelectHandle;

public interface Draggable {

	public void addDragListener();
	public SelectHandle getHandler();
	public default void doDrag(boolean pool,float x,float y) {
		
		if(pool) {
			
			ArrayList<ClickIn> list = getHandler().getSelecteds();
			
			for (ClickIn clickIn : list) {
				
				if(clickIn instanceof Draggable && clickIn != this) ((Draggable) clickIn).doDrag(false, x, y);
			}
		}
	}
}
