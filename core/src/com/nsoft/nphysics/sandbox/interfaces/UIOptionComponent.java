package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.sandbox.ui.Option;

public abstract class UIOptionComponent<T,A extends Actor>{

	private A component;
	private VisTable cell;
	private boolean enable = true;
	private boolean isnull = false;
	private boolean ready = true;
	private Option master;
	
	public abstract T getValue();
	public abstract boolean setValue(T newVal);
	public abstract void createComponent();
	
	public UIOptionComponent(Option master) {
		
		this.master = master;
	}
	
	public void init() {
		
		createComponent();
		initCell();
	}
	private void initCell() {
		
		cell = new VisTable();
		createCell(cell);
	}
	public void createCell(VisTable cell) {
		
		cell.add(component).expand().fill();
	}
	public VisTable getCell() {
		
		return cell;
	}
	
	public A getComponent() {return component;}
	public void setComponent(A neww) {component = neww;}
	
	public boolean isEnabled() {return enable;}
	
	public void setNull(boolean newnull) {
		isnull = newnull;
		if(enable) {
			component.setColor(newnull ? Color.YELLOW : Color.WHITE);
		}
	}
	public boolean isNull() {return isnull;}
	
	public void setReady(boolean ready) {
		this.ready = ready;
		if(enable) {
			component.setColor(ready ? Color.WHITE : Color.RED);
		}
	}
	public boolean isReady() {
		
		return ready;
	}

	public void enableComponent(boolean enable) {
		
		if(enable) {
			
			component.setColor(Color.WHITE);
			component.setTouchable(Touchable.enabled);
		}else {
			
			component.setColor(Color.GRAY);
			component.setTouchable(Touchable.disabled);
		}
		
		this.enable = enable;
		
		setReady(isReady());
		setNull(isNull());
	}
	
	public Form getForm() { return master.getForm(); }
}
