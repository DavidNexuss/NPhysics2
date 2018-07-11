package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nsoft.nphysics.sandbox.Util;

public abstract class DragStage extends Stage{


	public DragStage(Viewport v) {
		
		super(v);
		initOrtographicCamera();
	}
	float centerX;
	float centerY;
	
	float offsetX;
	float offsetY;
	
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
	
	public float getUnprojectX() {return getUnprojectX(false);}
	public float getUnprojectY() {return getUnprojectY(false);}
	
	public float getUnprojectX(boolean snap) {
		return unprojectX(snap ? snapGrid(Gdx.input.getX()) : Gdx.input.getX());
	}
	
	public float getUnprojectY(boolean snap) {
		return unprojectY(snap ? snapGrid(Gdx.input.getY()) : Gdx.input.getY());
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
