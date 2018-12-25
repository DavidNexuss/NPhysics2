package com.nsoft.nphysics.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nsoft.nphysics.Credit;

public class CreditWindow {

	public static void credit() {
		
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.vSyncEnabled = false;
		config.backgroundFPS = 10;
		config.foregroundFPS = 60;
		config.width = 872;
		config.height = 542;
		config.forceExit = false;
		LwjglApplication app = new LwjglApplication(new Credit(), config);
		
		try {
			Thread.sleep(5000);
			app.exit();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		}

	public static void main(String[] args) {
		
		credit();
	}
}