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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nsoft.nphysics.sandbox.Util;

/**
 * Fase que implementa DragStage, defineix una fase amb cuadricula
 * @author David
 */
public abstract class GridStage extends DragStage implements Say{

	public static ShaderProgram gridShader;
	public static SpriteBatch gridBatch;
	public static Texture nullTexture;

	private final Color backColor = new Color(1, 1, 1, 1);
	private final Color invertbackColor = new Color(1,1,1,1);
	
	private boolean focus = true;
	
	public NShapeRenderer shapefill;	
	public NShapeRenderer shapeline;
	public NShapeRenderer shapepoint;
	
	public GridStage(Viewport v) {
		
		super(v);
		
		//VARIABLE INIT:
		shapefill = new NShapeRenderer();
		shapeline = new NShapeRenderer();
		shapepoint = new NShapeRenderer();
		
		invertColor();
	}
	
	public Color getBackgroundColor() { return backColor;}
	public Color getInvertedBackColor() {return invertbackColor;}
	public void setBackgroundColor(float r,float g,float b,float a) {
		
		backColor.set(r, g, b, a);
		invertColor();
	}
	/**
	 * Inverteix el color del fons
	 */
	private void invertColor() {
		
		invertbackColor.set(1f - backColor.r, 1f - backColor.g, 1f - backColor.b, backColor.a);
	}
	/**
	 * Inicialitza els shaders de la cuadricula
	 */
	public static void initGridShader() {
		
		 String vertexShader = Gdx.files.internal("shaders/vertexShader.glsl").readString();
	     String fragmentShader = Gdx.files.internal("shaders/gridShader.glsl").readString();
	     
	     gridShader = new ShaderProgram(vertexShader, fragmentShader);
	     ShaderProgram.pedantic = false; //Per problemes de compilaci� del shader es recomanable desactivar pedantic
	     
	     System.out.println("Shader compiler log: " + gridShader.getLog());
	     
	     //Crea una textura buida sobre la que renderitza la cuadricula
	     Pixmap p = new  Pixmap(1, 1, Format.RGB565);
	     nullTexture = new Texture(p);
	     gridBatch = new SpriteBatch();
	     gridBatch.setShader(gridShader);
	}

	final Vector3 tmp = new Vector3();
	final Vector3 mousecoord = new Vector3();
	
	/**
	 * Executa les draw calls pertinents per renderitzar la cuadr�cula
	 */
	@Override
	public void draw() {	
		gridBatch.begin();
		camera.project(tmp);
		
		if(!NPhysics.menu && hasFocus()) {
			
			mousecoord.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(mousecoord);
			if(isSnapping())mousecoord.set(snapGrid(mousecoord.x), snapGrid(mousecoord.y), 0);
			camera.project(mousecoord);
		}
		//Establim valors a la VRAM de la GPU com a uniforms
		gridShader.setUniformf("PX",(int)mousecoord.x);
		gridShader.setUniformf("PY", (int)mousecoord.y);
		
		gridShader.setUniformf("grid", Util.getUnit() /camera.zoom);
		gridShader.setUniformf("xoffset", tmp.x);
		gridShader.setUniformf("yoffset", tmp.y);
		gridShader.setUniformf("X", Gdx.input.getX());
		gridShader.setUniformf("Y", Gdx.graphics.getHeight() - Gdx.input.getY());
		//Executem la draw call
		gridBatch.draw(nullTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridBatch.end();
		
		shapefill.begin(ShapeType.Filled);
		super.draw();
		shapefill.end();
		tmp.set(Vector3.Zero);
	}
	ScreenViewport vp = new ScreenViewport();
	@Override
	public void updateViewport(int width, int height) {
		
		super.updateViewport(width, height);
		
	//	gridBatch.setTransformMatrix(UIStage.stage.getCamera().combined);
	}
	@Override
	public void updateMatrix() {
		
		//Actualitza les matrius de projecci� de els diferents shaders
		shapefill.setProjectionMatrix(getCamera().combined);
		shapeline.setProjectionMatrix(getCamera().combined);
		shapepoint.setProjectionMatrix(getCamera().combined);
		getBatch().setProjectionMatrix(getCamera().combined);
		
	}
	
	public boolean hasFocus() {
		return focus;
	}
	public void setFocus(boolean newFocus) {
		focus = newFocus;
	}
}
