package com.nsoft.nphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;

public class NPhysics extends ApplicationAdapter {
	
	public static boolean useMultiThreading;
	public static Sandbox sandbox;
	public static UIStage ui;
	public static SimulationStage simulation;
	public static ThreadCase threads;
	public static GridStage currentStage;
	static NPhysics current;

	public static ThreadCase getThreadManager(){ return threads;}
	
	public NPhysics() {
		
		this(null,false);
	}
	public NPhysics(ThreadCase threads,boolean useMultiThreading) {
		
		this.threads = threads;
		NPhysics.useMultiThreading = useMultiThreading; 
	}
	
	@Override
	public void create () {
		current = this;
		
		UILoader.loadUI();
		GridStage.initGridShader();
		sandbox = new Sandbox();
		sandbox.init();
		simulation = new SimulationStage(sandbox.getCamera());
		currentStage = sandbox;
		
		ui = new UIStage();
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}

	boolean first = true;
	@Override
	public void render () {
		Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 0.7f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(first) { System.out.println(first = false);}
		currentStage.draw();
		currentStage.act();
		ui.draw();
		ui.act();
		ThreadManager.act(Gdx.graphics.getDeltaTime());
	}
	
	public static void switchToSimulation() {
		

		currentStage = simulation;
		simulation.cleanAndSetUp();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}
	@Override
	public void dispose () {
		sandbox.dispose();
		ui.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		
		sandbox.getViewport().update(width, height);
		ui.getViewport().update(width, height);
	}
}
