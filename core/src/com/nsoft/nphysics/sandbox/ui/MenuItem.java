package com.nsoft.nphysics.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
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
	
	float h,w;
	@Override
	public void draw(Batch batch, float parentAlpha) {

		h = getHeight();
		w =  getWidth();
		
		setHeight(h*getScaleY());
		setWidth(w*getScaleX());
		
		super.draw(batch, parentAlpha);
		
		setHeight(h);
		setWidth(w);
	}
	
}
