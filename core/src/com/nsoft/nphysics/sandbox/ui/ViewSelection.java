package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener;

public class ViewSelection extends TabbedPane{

	public ViewSelection() {

	}
	
	public void initInput() {
		
		addListener(new TabbedPaneAdapter() {
			
			@Override
			public void switchedTab(Tab tab) {
				
				ViewTab t = (ViewTab)tab;
				if(t.hasAction()) t.runAction();
			}
		});
	}
}
