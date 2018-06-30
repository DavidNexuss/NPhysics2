package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

public class UILoader {

	public static Skin skin;
	public static final String SKINPATH = "skin/neutralizer-ui.json";
	public static final String DEFAULTSKINPATH = "default-skin/uiskin.json";
	
	public static void loadUI() {
		
		skin = new Skin(Gdx.files.internal(DEFAULTSKINPATH));
		VisUI.load();
	}
}
