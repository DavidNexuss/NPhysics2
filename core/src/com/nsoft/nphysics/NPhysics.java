/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.nsoft.nphysics.sandbox.FastPolygonCreator;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.Point;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.ui.AlertWindow;
import com.nsoft.nphysics.sandbox.ui.UIStage;
import com.nsoft.nphysics.simulation.dynamic.SimulationPackage;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage;
/**
 * Clase principal del programa
 * Es el punt de partida del programa una vegada s'ha executat el marc
 * dependent de la plataforma.
 * 
 * @author David
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
	}
	/**
	 * Inicialitza el programa
	 */
	@Override
	public void create () {
		current = this;

		Options.init();
		NDictionary.init();
		UILoader.loadUI();
		ui = new UIStage();
		GridStage.initGridShader();
		
		sandbox = new Sandbox();

		currentStage = sandbox;
		
		sandbox.setBackgroundColor(0.8f, 0.9f, 1f, 1f); //Estableix el fons del programa
		sandbox.init();
		
		simulation = new SimulationStage(sandbox.getCamera());
		simulation.setBackgroundColor(0, 0, 0, 1); //Estableix el fons del programa
		//per la simulació
		
		//
		Gdx.input.setInputProcessor(new InputMultiplexer(ui,currentStage));
	}

	boolean first = true;
	
	/**
	 * Bucle de renderització executat 60 vegades cada segon per executar les draw calls
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
	/**
	 * Amaga o mostra el menu estàtic
	 */
	public static void switchMenu() {
		
		if(ui.canSwitch()) {
			
			menu = !menu;
			
			if(menu) ui.showStaticMenu();
			else ui.hideStaticMenu();
		}
	}
	/**
	 * Cambia a la simulació
	 */
	public static void switchToSimulation() {
		
		if(!currentStage.isReady()) {
			
			//Mostra un misatge d'alerta
			AlertWindow.throwNewAlert(NDictionary.get("simulation-switch-error-title"), NDictionary.get("simulation-switch-error-msg") + "\n" +sandbox.getLastErrorMessage());
			ThreadManager.createTask(()->UIStage.view.switchTab(0), 0.02f);
			return;
		}
		currentStage = simulation;
		sandbox.clean();
		simulation.setUp();
		updateInput();
	}
	
	/**
	 * Llimpia totes les llistes 
	 */
	public static void cleanWorld() {
		
		if(currentStage == simulation) switchToSandbox();
		
		sandbox = new Sandbox();
		sandbox.setBackgroundColor(0.8f, 0.9f, 1f, 1f);
		sandbox.init();

		currentStage = sandbox;
		Point.allpoints = new ArrayList<>();
		ForceComponent.list = new ArrayList<>();
		FastPolygonCreator.temp = null;
		
		SimulationPackage.update();
		updateInput();
		
		simulation.setCamera(sandbox.getCamera());
		UIStage.view.switchTab(0);
	}
	/**
	 * Canvia al sandbox
	 */
	public static void switchToSandbox() {
		
		currentStage = sandbox;
		simulation.clean();
		sandbox.setUp();
		updateInput();
	}
	
	private static void updateInput() {
		
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
