package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.layout.DragPane;
import com.nsoft.nphysics.UILoader;
import com.nsoft.nphysics.sandbox.GState;

public class UIStage extends Stage{

	OptionPane options;
	public UIStage() {
		
		loadOptionMenu();
		loadSubMenu();
		loadViewMenu();
	}
	
	private void loadViewMenu() {
		
		Table container = new Table(UILoader.skin);
		container.setPosition(40, Gdx.graphics.getHeight() - 12);
		container.setWidth(Gdx.graphics.getWidth() - 40);

		ViewSelection view= new ViewSelection();
		view.add(new ViewTab());
		
		DragPane p = view.getTabsPane();
		container.add(p).expand().fill();
		
		addActor(container);
		
		container.setDebug(true);
	}
	private void loadSubMenu() {
		
		//DOWN-MENU
		MenuItem grid = MenuItem.loadNewItem("home.png", ()->{});
		grid.setPosition(Gdx.graphics.getWidth() - 40, 8);
		addActor(grid);
		addActor(options);	
	}
	private void loadOptionMenu() {
		
		options = new OptionPane();

		options.setPosition(0, 0);
		options.setHeight(Gdx.graphics.getHeight());
		options.setWidth(40);
			
		options.add(MenuItem.loadNewItem("start.png", GState.START));
		options.add(MenuItem.loadNewItem("home.png", GState.CREATE_POINT));
		options.pack();
	}
}
