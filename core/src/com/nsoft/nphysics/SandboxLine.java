package com.nsoft.nphysics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SandboxLine extends Actor{

	private Point A;
	private Point B;
	
	public SandboxLine() {
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Sandbox.shapeline.begin(ShapeType.Line);
		Sandbox.shapeline.line(A.getX(), A.getY(), B.getX(), B.getY());
		Sandbox.shapeline.end();
	}
	
	public boolean isComplete() {
		
		return A != null && B != null;
	}
	public void setPointA(Point p) {
		
		A = p;
	}
	
	public void setPointB(Point p) {
		
		B = p;
	}
	
}
