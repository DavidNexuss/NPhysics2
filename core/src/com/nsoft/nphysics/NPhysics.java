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
import com.nsoft.nphysics.sandbox.ui.AlertWindow;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.simulation.dsl.MainTest;
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
		
		NPhysics.threads = threads;
		NPhysics.useMultiThreading = useMultiThreading; 

		MainTest.runTest();
	}
	
	@Override
	public void create () {
		current = this;
		
		Dictionary.init();
		UILoader.loadUI();
		ui = new UIStage();
		GridStage.initGridShader();
		
		sandbox = new Sandbox();
		sandbox.setBackgroundColor(0.8f, 0.9f, 1f, 1f);
		sandbox.init();
		
		simulation = new SimulationStage(sandbox.getCamera());
		simulation.setBackgroundColor(0, 0, 0, 1);
		
		currentStage = sandbox;
		
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}

	boolean first = true;
	@Override
	public void render () {
		Gdx.gl.glClearColor(currentStage.getBackgroundColor().r,currentStage.getBackgroundColor().g,currentStage.getBackgroundColor().b,currentStage.getBackgroundColor().a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(first) { System.out.println(first = false);}
		currentStage.draw();
		currentStage.act();
		ui.draw();
		ui.act();
		ThreadManager.act(Gdx.graphics.getDeltaTime());
	}
	
	public static void switchToSimulation() {
		
		if(!currentStage.isReady()) {
			
			AlertWindow.throwNewAlert(Dictionary.get("simulation-switch-error-title"), Dictionary.get("simulation-switch-error-msg"));
			return;
		}
		currentStage = simulation;
		sandbox.clean();
		simulation.setUp();
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}
	
	public static void switchToSandbox() {
		
		currentStage = sandbox;
		simulation.clean();
		sandbox.setUp();
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
