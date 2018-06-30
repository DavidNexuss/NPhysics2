package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.nsoft.nphysics.sandbox.Util;

public class MenuItem extends VisImageButton{
	
	private Runnable run;
	public MenuItem(String name) {
		
		this(getTexture(name));
	}
	public MenuItem(Texture t) {
		
		super(Util.getDrawable(t));
	}
	
	
	public static Texture getTexture(String name) {
		
		return new Texture(Gdx.files.internal("menu/" + name));
	}
	
	public static MenuItem loadNewItem(String name,Runnable run) {
		
		MenuItem m = new MenuItem(name);
		m.run = run;
		m.setSize(32, 32);
		return m;
	}
	
}
