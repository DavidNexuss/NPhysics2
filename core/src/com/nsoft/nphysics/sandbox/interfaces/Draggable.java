package com.nsoft.nphysics.sandbox.interfaces;

import java.util.ArrayList;
import java.util.Stack;

import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.SelectHandle;

public interface Draggable {

	public void addDragListener();
	public SelectHandle getHandler();
	public default void doDrag(boolean pool,float x,float y) {
		
		if(pool) {
			
			
			Stack<ClickIn> list = getHandler().getSelecteds();
			
			for (ClickIn clickIn : list) {
				
				if(clickIn instanceof Draggable && clickIn != this && clickIn != null) {
					if(!Sandbox.SHIFT) clickIn.getHandler().unSelect(clickIn);
					((Draggable) clickIn).doDrag(false, x, y);
				}
			}
		}
	}
}
