package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nsoft.nphysics.sandbox.Util;

public abstract class DragStage extends Stage{


	float centerX;
	float centerY;
	
	float offsetX;
	float offsetY;

	boolean snapping = true;
	
	public DragStage(Viewport v) {
		
		super(v);
		initOrtographicCamera();
	}
	
	public DragStage(Viewport v,DragStage d) {
		
		super(v);
		initOrtographicCamera();
		
		centerX = d.centerX;
		centerY = d.centerY;
		
		offsetX = d.offsetX;
		offsetY = d.offsetY;
	}
	public OrthographicCamera camera;
	
	public void initOrtographicCamera() {
		
		camera = (OrthographicCamera)getCamera();
	}
	public void setCenter(float screenX,float screenY) {
		
		centerX = screenX*camera.zoom;
		centerY = (Gdx.graphics.getHeight() - screenY)*camera.zoom;
	}
	
	public abstract void updateMatrix();
	
	public Vector2 getUnproject() {return new Vector2(getUnprojectX(),getUnprojectY());}
	
	public static int snapGrid(float v) { return (int)Util.UNIT*Math.round(v/Util.UNIT); }
	
	public void setSnapping(boolean newSnapping) {snapping = newSnapping;}
	public void switchSnapping() {snapping = !snapping;}
	public boolean isSnapping() {return snapping;}
	
	public static int getProjectedX(boolean snap) { return snap ? snapGrid(Gdx.input.getX()) : Gdx.input.getX();}
	public static int getProjectedY(boolean snap) { return snap ? snapGrid(Gdx.graphics.getHeight() - Gdx.input.getY()) : Gdx.graphics.getHeight() - Gdx.input.getX();}
	
	public int getProjectedX() { return getProjectedX(snapping);}
	public int getProjectedY() { return getProjectedY(snapping);}
	
	public float getUnprojectX() {return getUnprojectX(snapping);}
	public float getUnprojectY() {return getUnprojectY(snapping);}
	
	public float getUnprojectX(boolean snap) {
		return snap ? snapGrid(unprojectX(Gdx.input.getX())) : unprojectX(Gdx.input.getX());
	}
	
	public float getUnprojectY(boolean snap) {
		return snap ? snapGrid(unprojectY(Gdx.input.getY())) : unprojectY(Gdx.input.getY());
	}
	
	public float unprojectX(float x) {return camera.unproject(new Vector3(x, 0, 0)).x;}
	public float unprojectY(float y) {return camera.unproject(new Vector3(0, y, 0)).y;}
	
	public void dragCamera(float screenX,float screenY) {
		
		float screeny = Gdx.graphics.getHeight() - screenY;
		
		
		((OrthographicCamera)getCamera()).translate(centerX - screenX*camera.zoom,centerY - screeny*camera.zoom);
		setCenter(screenX , screenY);
		getCamera().update();
		updateMatrix();
		offsetX = screenX;
		offsetY = screeny;
	}
}
