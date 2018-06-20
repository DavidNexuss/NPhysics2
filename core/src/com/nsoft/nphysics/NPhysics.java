package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NPhysics extends ApplicationAdapter {
	
	Sandbox sandbox;
	static NPhysics current;
	
	@Override
	public void create () {
		current = this;
		sandbox = new Sandbox();
		Gdx.input.setInputProcessor(sandbox);
		System.out.println(Thread.currentThread());
	}

	boolean first = true;
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(first) { System.out.println(Thread.currentThread()); first = false;}
		sandbox.draw();
		sandbox.act();
	}
	
	@Override
	public void dispose () {
		sandbox.dispose();
	}
}
