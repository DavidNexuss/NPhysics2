package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.nsoft.nphysics.sandbox.GState;
import com.nsoft.nphysics.sandbox.GameState;
import com.nsoft.nphysics.sandbox.Util;

public class MenuItem extends VisImageButton{
	
	public static HashMap<String, Runnable> runSet = new HashMap<>();
	public static Runnable StateChange;
	
	private Runnable run;
	private GState gstate;
	
	private Texture texture;
	
	public MenuItem(String name) {
		
		this(getTexture(name));
	}
	public MenuItem(Texture t) {
		
		super(Util.getDrawable(t));
		texture = t;
		
		addInput();
	}
	
	private void addInput() {
		
		addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if(run != null) run.run();
				if(gstate != null) GameState.set(gstate);
				
				if(getDebug()) System.out.println(this + ": Handled");
			}
		});
	}
	public static Texture getTexture(String name) {
		
		return new Texture(Gdx.files.internal("menu/" + name));
	}
	
	public static MenuItem loadNewItem(String name,GState g) {
		
		MenuItem m = initMenuItem(name);
		m.gstate = g;
		return m;
		
	}
	
	public static MenuItem loadNewItem(String name,String runnableKey) {
		
		return loadNewItem(name, runSet.get(runnableKey));
	}
	public static MenuItem loadNewItem(String name,Runnable run) {
		
		MenuItem m = initMenuItem(name);
		m.run = run;
		return m;
	}
	
	public static MenuItem initMenuItem(String name) {
		
		MenuItem m = new MenuItem(name);
		m.setSize(32, 32);
		return m;
		
	}
}
