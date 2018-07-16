package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager {

	public static BitmapFont title;
	public static BitmapFont subtitle;
	
	static void init() {
		
		title = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
		subtitle = new BitmapFont(Gdx.files.internal("fonts/subtitle.fnt"));
	}
}
