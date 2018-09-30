package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NShapeRenderer extends ShapeRenderer{

	@Override
	public void circle(float x, float y, float radius) {
		circle(x, y, radius, Math.max(1, (int)(6 * (float)Math.cbrt(radius) * (1 + 1f/ NPhysics.currentStage.camera.zoom))));
	}
}
