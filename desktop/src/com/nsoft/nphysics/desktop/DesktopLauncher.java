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

public class DesktopLauncher {
	
	public static void program(String[] arg) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.vSyncEnabled = false;
		config.backgroundFPS = 10;
		config.foregroundFPS = 60;
		if(arg.length != 0 && arg[0].equals("window")) {

			config.width = 1280;
			config.height = 720;
		}else {
			
			config.width = Toolkit.getDefaultToolkit().getScreenSize().width;
			config.height = Toolkit.getDefaultToolkit().getScreenSize().height;
			config.fullscreen = true;
		}
		
		System.out.println("wd");
		config.samples = 4;
		
		NPhysics.functions = new SpecificWindowsFunctions();
		new LwjglApplication(new NPhysics((task,delay)->{
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						Thread.sleep(delay);
						task.run();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		},true) ,config);	
	}
	
	public static void main (String[] arg) {
	
		try {
			JavaProcess.exec(CreditWindow.class);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		program(arg);
	}
}
