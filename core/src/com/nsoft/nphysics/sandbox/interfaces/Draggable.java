package com.nsoft.nphysics.sandbox.interfaces;

import java.util.Stack;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.SelectHandle;

/**
 * Interfície per definir la propietat de ser arrastrat d'un Actor.
 * Simplement proveeix unes funcions básiques per poder implementar rápidament un sistema per
 * arrastrar. Així aquest serà sempre el mateix procediment.
 * @author David
 */
public interface Draggable {

	public default void addDragListener() {
		
		addListener(new DragListener() {
		    public void drag(InputEvent event, float x, float y, int pointer) {
		    	
		    	doDrag(true,x,y,event);
		    }
		});
	}
	public SelectHandle getHandler();
	public boolean addListener(EventListener listener);
	
	//TODO: Fix pool
	public default void doDrag(boolean pool,float x,float y,InputEvent event) {
		
		if(pool) {
			
			Stack<ClickIn> list = getHandler().getSelecteds();
			
			for (ClickIn clickIn : list) {
				
				if(clickIn instanceof Draggable && clickIn != this) {
					if(!(Sandbox.SHIFT || Sandbox.mainSelect.multiSelection)) clickIn.getHandler().unSelect(clickIn);
					((Draggable) clickIn).doDrag(false, x, y,event);
				}
			}
		}
	}
}
