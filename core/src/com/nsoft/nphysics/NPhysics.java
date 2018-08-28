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
/**
 * Clase principal del programa
 * @author David
 *
 */
public class NPhysics extends ApplicationAdapter {
	
	public static boolean useMultiThreading;
	public static Sandbox sandbox;
	public static UIStage ui;
	public static SimulationStage simulation;
	public static ThreadCase threads;
	public static GridStage currentStage;
	static NPhysics current;
	public static SpecificOSFunctions functions = new DefaultOSFunctions();
	public static boolean menu = false;
	
	
	public static ThreadCase getThreadManager(){ return threads;}
	
	public NPhysics() {
		
		this(null,false);
	}
	public NPhysics(ThreadCase threads,boolean useMultiThreading) {
		
		NPhysics.threads = threads;
		NPhysics.useMultiThreading = useMultiThreading; 

		MainTest.runTest();
	}
	/**
	 * Inicialitza el programa
	 */
	@Override
	public void create () {
		current = this;
		
		NDictionary.init();
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
	
	/**
	 * Bucle de renderització
	 */
	@Override
	public void render () {
		
		
		//Limpia el buffer de la imatge atraves de OpenGL
		Gdx.gl.glClearColor(currentStage.getBackgroundColor().r,currentStage.getBackgroundColor().g,currentStage.getBackgroundColor().b,currentStage.getBackgroundColor().a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		if(first) { System.out.println(first = false);}
		
		//Executa els bucles lògics de l'actual fase i la interfas, i executa draw calls.
		currentStage.draw(); 
		currentStage.act();
		ui.draw(); 			
		ui.act();			
		
		//Executa els subprocessos en espera (En cas de incompatibilitat amb Multi-threading)
		ThreadManager.act(Gdx.graphics.getDeltaTime());
		
	}
	
	public static void switchMenu() {
		
		menu = !menu;
		
		if(menu) ui.showStaticMenu();
		else ui.hideStaticMenu();
	}
	/**
	 * Cambia a la simulació
	 */
	public static void switchToSimulation() {
		
		if(!currentStage.isReady()) {
			
			AlertWindow.throwNewAlert(NDictionary.get("simulation-switch-error-title"), NDictionary.get("simulation-switch-error-msg"));
			return;
		}
		currentStage = simulation;
		sandbox.clean();
		simulation.setUp();
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}
	
	/**
	 * Cambia al sandbox
	 */
	public static void switchToSandbox() {
		
		currentStage = sandbox;
		simulation.clean();
		sandbox.setUp();
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}
	
	/**
	 * Allibera memòria, aquesta funció es crida al tancament del programa
	 */
	@Override
	public void dispose () {
		sandbox.dispose();
		ui.dispose();
	}
	
	/**
	 * Actualitza els viewports, funció cridada al cambi de tamany de la finestra
	 */
	@Override
	public void resize(int width, int height) {
		
		currentStage.updateViewport(width, height);
		ui.updateUILayout();
	}
}
