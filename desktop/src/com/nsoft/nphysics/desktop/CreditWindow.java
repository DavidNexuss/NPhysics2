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
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/package com.nsoft.nphysics.desktop;

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
