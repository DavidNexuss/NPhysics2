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

import java.awt.Toolkit;

import com.nsoft.nphysics.SpecificOSFunctions;

public class SpecificWindowsFunctions implements SpecificOSFunctions{

	@Override
	public void playSound(String name) {
		
		final Runnable runnable =
			     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty(name);
			if (runnable != null) runnable.run();
	}
	
	@Override
	public Thread getCurrentThread() {
		return Thread.currentThread();
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return getCurrentThread().getStackTrace();
	}
}
