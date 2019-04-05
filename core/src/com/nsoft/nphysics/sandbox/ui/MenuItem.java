package com.nsoft.nphysics.sandbox.ui;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.GState;
import com.nsoft.nphysics.sandbox.GameState;
import com.nsoft.nphysics.sandbox.Util;

public class MenuItem extends VisImageButton implements Say{
	
	public static HashMap<String, Runnable> runSet = new HashMap<>();
	public static Runnable StateChange;
	
	private Runnable run;
	private GState gstate;
	
	private VisLabel label;
	private VisTable table;
	
	
	public MenuItem(String name,GState gstate) {
		
		this(getTexture(name));
		
		table = new VisTable();
		if(gstate != null) {
			
			label = new VisLabel(gstate.description);
			table.add(label);
			table.pad(2);
			table.setVisible(false);
			table.setColor(new Color(0.9f, 0.9f, 0.8f, 1f));
			label.setStyle(new LabelStyle(FontManager.subtitle, Color.BLACK));
			table.pack();

			table.setBackground(OptionPane.generateBackground(table));
		}
		UIStage.labelGroup.addActor(table);
		this.gstate = gstate;
	}
	public MenuItem(Texture t) {
		
		super(Util.getDrawable(t));
		addInput();
	}
	
	private void addInput() {
		
		addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if(run != null) run.run();
				if(gstate != null) GameState.set(gstate);
				
			}
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				
				super.enter(event, x, y, pointer, fromActor);
				table.setVisible(true);		
				
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				table.setVisible(false);
			}
		});
	}
	
	@Override
	public void act(float delta) {
		
		super.act(delta);
		
		if(table.isVisible()) {
			table.setPosition(Gdx.input.getX() + 70, Gdx.graphics.getHeight() - Gdx.input.getY());
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		super.draw(batch, parentAlpha);
		
		if(gstate != null && table.isVisible()) {
			

			UIStage.shapeline.setColor(Color.BLACK);
			Gdx.gl.glLineWidth(2);
			UIStage.shapeline.rect(table.getX(), table.getY(), table.getWidth(), table.getHeight());
		}
	}
	public static Texture getTexture(String name) {
		
		return new Texture(Gdx.files.internal("menu/" + name));
	}
	
	public static MenuItem loadNewItem(String name,GState g) {
		
		MenuItem m = initMenuItem(name,g);
		return m;
		
	}
	
	public static MenuItem loadNewItem(String name,String runnableKey) {
		
		return loadNewItem(name, runSet.get(runnableKey));
	}
	public static MenuItem loadNewItem(String name,Runnable run) {
		
		MenuItem m = initMenuItem(name,null);
		m.run = run;
		return m;
	}
	
	public static MenuItem initMenuItem(String name,GState g) {
		
		MenuItem m = new MenuItem(name,g);
		m.setSize(32, 32);
		return m;
		
	}
}
