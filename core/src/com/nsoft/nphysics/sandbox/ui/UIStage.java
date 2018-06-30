package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
		addActor(options);
		
		//setDebugAll(true);
	}
}
