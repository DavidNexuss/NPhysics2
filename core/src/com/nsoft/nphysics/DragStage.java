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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.interfaces.Position;

/**
 * clase abstracta que defineix funcions i valors amb la utilitat de poder crear una fase amb
 * la capacitat de moure la camara lliurement
 * @author David
 */
public abstract class DragStage extends Stage{

	private Position axisPosition = new PositionVector(Vector2.Zero);
	
	private float centerX;
	private float centerY;
	
	private float offsetX;
	private float offsetY;

	private boolean snapping = true;
	
	private Group uiGroup;
	
	public float RotationRadAngle = 0;
	
	public DragStage(Viewport v) {
		
		super(v);
		initOrtographicCamera();
		
		uiGroup = new Group();
		NPhysics.ui.addActor(uiGroup);
	}
	
	public DragStage(Viewport v,DragStage d) {
		
		this(v);
		
		centerX = d.centerX;
		centerY = d.centerY;
		
		offsetX = d.offsetX;
		offsetY = d.offsetY;
	}
	/**
	 * Estableix la posició del eix del món
	 * @param p
	 */
	public void setAxisPosition(Position p) {axisPosition = p;}
	public Position getAxisPosition() {return axisPosition;}
	
	public Vector2 worldToRelative(Vector2 v) {
		
		return v.sub(axisPosition.getVector());
	}
	
	public Vector2 relativeToWorld(Vector2 v) {
		
		return v.add(axisPosition.getVector());
	}
	public OrthographicCamera camera;
	
	public void initOrtographicCamera() {
		
		camera = (OrthographicCamera)getCamera();
	}
	public void setCenter(float screenX,float screenY) {
		
		centerX = screenX*camera.zoom;
		centerY = (Gdx.graphics.getHeight() - screenY)*camera.zoom;
	}
	
	/**
	 * Actualitza les matrius
	 */
	public abstract void updateMatrix();
	
	public Vector2 getUnproject() {return new Vector2(getUnprojectX(),getUnprojectY());}
	public Vector2 getUnproject(boolean snap) {return new Vector2(getUnprojectX(snap),getUnprojectY(snap));}
	
	/**
	 * Obté la coordenada més pròxima a un punt de la cuadicula, mateix funcionament per la coordenada X que amb la Y
	 * @param v
	 * @return
	 */
	public static int snapGrid(float v) { return (int)Util.getUnit()*Math.round(v/Util.getUnit()); }
	
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
	
	public float getZoom() {
		
		return camera.zoom;
	}
	
	/**
	 * Canvia la camara del Viewport
	 * @param camera
	 */
	public void setCamera(Camera camera) {
		
		getViewport().setCamera(camera);
		initOrtographicCamera();
	}
	/**
	 * Mou la camara
	 * @param screenX nova coordenada X del cursor
	 * @param screenY nova coordenada Y del cursor
	 */
	public void dragCamera(float screenX,float screenY) {
		
		if(NPhysics.menu) return;
		float screeny = Gdx.graphics.getHeight() - screenY;
		
		
		((OrthographicCamera)getCamera()).translate(centerX - screenX*camera.zoom,centerY - screeny*camera.zoom);
		setCenter(screenX , screenY);
		getCamera().update();
		updateMatrix();
		offsetX = screenX;
		offsetY = screeny;
	}
	
	public abstract boolean removeGroups();
	/**
	 * Prepara la Stage per a fer el canvi
	 */
	public void setUp() { 
		
		if(removeGroups()) {
			uiGroup.clear(); 
		}else uiGroup.setVisible(true);
		updateMatrix(); }
	
	/**
	 * Limpia la Stage per a fer el canvi
	 */
	public void clean() {
		
		if(removeGroups()) {
			uiGroup.clear();
			clear();
		}else uiGroup.setVisible(false);
	}
	public boolean isReady() {return true;}
	public static float zoomVal = 1.2f;
	
	public void resetCamera() {
		
		camera.setToOrtho(false, Gdx.graphics.getWidth()/ 2, Gdx.graphics.getHeight() / 2);
		updateMatrix();
	}
	public void updateViewport(int width,int height) {
		
		getViewport().update(width, height);
		getCamera().update();
		updateMatrix();
	}
	/**
	 * @return El grup que interactua amb la Stage de la UI
	 */
	public Group getUiGroup() {
		return uiGroup;
	}
	@Override
	public boolean scrolled(int amount) {
		
		if(camera.zoom == 0 && amount < 0) return true;
		camera.zoom *= amount > 0 ? zoomVal : 1f/zoomVal;
		camera.update();
		updateMatrix();
		return true;
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		
		switch (keyCode) {
		case Keys.ESCAPE:

			Gdx.app.exit(); // Preferible a utilitzar System.exit(0);
			return true;
		case Keys.F1:
			
			NPhysics.switchMenu();
			return true;
		default:
			
			return super.keyDown(keyCode);
			
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	}
}
