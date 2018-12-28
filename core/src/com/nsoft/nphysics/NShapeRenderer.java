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
