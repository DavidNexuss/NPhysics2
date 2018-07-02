package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.ui.UIStage;

public class NPhysics extends ApplicationAdapter {
	
	public static Sandbox sandbox;
	public static UIStage ui;
	static NPhysics current;

	
	@Override
	public void create () {
		current = this;
		UILoader.loadUI();
		sandbox = new Sandbox();
		ui = new UIStage();
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,sandbox));
	}

	boolean first = true;
	@Override
	public void render () {
		Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 0.7f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(first) { System.out.println(first = false);}
		sandbox.draw();
		sandbox.act();
		ui.draw();
		ui.act();
	}
	
	@Override
	public void dispose () {
		sandbox.dispose();
	}
}
