package com.nsoft.nphysics.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nsoft.nphysics.Credit;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.ThreadCase;
import com.nsoft.nphysics.sandbox.PrismaticComponent;

import java.awt.Toolkit;
import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
/**
 * Punt d'entrada del programa, l'objectiu es crear el context de OpenGL
 * amb la biblioteca LWJGL i passa l'execuci� del programa a la 
 * classe que actua com adaptador NPhysics.java o en el cas de la caratula
 * del programa Credit.java
 * 
 * @author David
 */
public class DesktopLauncher {
	
	public static void program(String[] arg) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true; //OpenGL ES 3.0
		config.vSyncEnabled = false; //Sincronitzaci� vertical (molt �til en la majoria de casos per� desactivada per debugueig)
		config.backgroundFPS = 10;
		config.foregroundFPS = 60; //Objectiu m�xim de FPS
		if(arg.length != 0 && arg[0].equals("window")) {

			config.width = 1280; //amplada de la finestra
			config.height = 720; //altura de la finestra
		}else {
			
			config.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			config.height = Toolkit.getDefaultToolkit().getScreenSize().height;
			config.fullscreen = true;
		}
		config.samples = 4; //Filtre MSAAx4 (Podriem aplicar tamb� un FXAA si la GPU
		//fos incompatible
		
		NPhysics.functions = new SpecificWindowsFunctions();
		new LwjglApplication(new NPhysics((task,delay)->{
			
			//Format de l'execuic� d'un fil paral�lel @see ThreadManager
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						Thread.sleep(delay);
						task.run();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		},true) ,config);	
		
	}
	
	public static void main (String[] arg) {
	
		//Execuci� d'un proc�s independent per executar la caratula
		try {
			JavaProcess.exec(CreditWindow.class);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		program(arg);
	}
}
