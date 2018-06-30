package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class ViewTab extends Tab{

	
	public ViewTab() {
		
		super(false, false);
	}
	@Override
	public String getTabTitle() {
		
		return "Test";
	}

	@Override
	public Table getContentTable() {
		
		return null;
	}

}
