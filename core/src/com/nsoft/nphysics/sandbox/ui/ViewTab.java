package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class ViewTab extends Tab{

	private String name;
	private Runnable action;
	
	public ViewTab(String name) {
		
		this(name,null);
	}
	
	public ViewTab(String name,Runnable action) {
		
		super(false,false);
		this.name = name;
		this.action = action;
	}
	
	public boolean hasAction() {return action != null;}
	public void runAction() {action.run();}
	@Override
	public String getTabTitle() {
		
		return name;
	}

	@Override
	public Table getContentTable() {
		
		return null;
	}

}
