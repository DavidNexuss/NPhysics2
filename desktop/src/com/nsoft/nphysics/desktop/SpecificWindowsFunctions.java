package com.nsoft.nphysics.desktop;

import java.awt.Toolkit;

import com.nsoft.nphysics.SpecificOSFunctions;

public class SpecificWindowsFunctions implements SpecificOSFunctions{

	@Override
	public void playSound(String name) {
		
		final Runnable runnable =
			     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty(name);
			if (runnable != null) runnable.run();
	}
}
