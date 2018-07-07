package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

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
