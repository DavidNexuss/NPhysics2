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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
/**
 * Classe encarregada de manejar la càrrega dels elements de la UI com la Skin i VisUI.
 * @author David
 */
public class UILoader {

	public static Skin skin;
	public static final String SKINPATH = "skin/neutralizer-ui.json";
	public static final String DEFAULTSKINPATH = "default-skin/uiskin.json";
	
	public static void loadUI() {
		
		skin = new Skin(Gdx.files.internal(DEFAULTSKINPATH));
		VisUI.load();
	}
}
