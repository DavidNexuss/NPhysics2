package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.layout.DragPane;
import com.nsoft.nphysics.UILoader;

public class UIStage extends Stage{

	OptionPane options;
	public UIStage() {
		
		options = new OptionPane();
		options.setPosition(0, 0);
		options.setHeight(Gdx.graphics.getHeight());
		options.setWidth(40);
		
		options.add(MenuItem.loadNewItem("home.png", null));
		options.pack();
		
		//DOWN-MENU
		MenuItem grid = MenuItem.loadNewItem("home.png", null);
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		addActor(grid);
		addActor(options);
		
		
		ViewSelection view= new ViewSelection();
		view.add(new ViewTab());
		
		Table p = view.getTable();
		p.setPosition(40, Gdx.graphics.getHeight() - 12);
		p.setWidth(Gdx.graphics.getWidth() - 40);
		addActor(p);
		//setDebugAll(true);
	}
}
