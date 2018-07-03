package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class ViewTab extends Tab{

	private String name;
	public ViewTab(String name) {
		
		super(false, false);
		this.name = name;
	}
	@Override
	public String getTabTitle() {
		
		return name;
	}

	@Override
	public Table getContentTable() {
		
		return null;
	}

}
