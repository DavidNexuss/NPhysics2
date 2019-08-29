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

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Versió modificada de {@link ShapeRenderer} per poder renderitzar cercles tenint en compte
 * paràmetres com el tamany del zoom apart del seu radi.
 * @author David
 *
 */
public class NShapeRenderer extends ShapeRenderer{

	@Override
	public void circle(float x, float y, float radius) {
		circle(x, y, radius, Math.max(1, segments(radius)));
	}
	
	/**
	 * Calcula el nombre de segments optim en funció del zoom del actual Escenari
	 * @param radius
	 * @return El nombre de segments necessaris
	 */
	public int segments(float radius) {
		
		int var = (int)(6 * (float)Math.cbrt(radius) * (1 + 1f/ NPhysics.currentStage.camera.zoom));
		if(var > 200) 
			return 200;
		else return var;
	}
}
