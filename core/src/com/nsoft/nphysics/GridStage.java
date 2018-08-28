package com.nsoft.nphysics;

import static com.nsoft.nphysics.sandbox.Util.UNIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Fase que implementa DragStage, defineix una fase amb cuadricula
 * @author David
 */
public class GridStage extends DragStage{

	public static ShaderProgram gridShader;
	public static SpriteBatch gridBatch;
	public static Texture nullTexture;

	private final Color backColor = new Color(1, 1, 1, 1);
	private final Color invertbackColor = new Color(1,1,1,1);
	
	public ShapeRenderer shapefill;	
	public ShapeRenderer shapeline;
	public ShapeRenderer shapepoint;
	
	public GridStage(Viewport v) {
		
		super(v);
		
		//VARIABLE INIT:
		shapefill = new ShapeRenderer();
		shapeline = new ShapeRenderer();
		shapepoint = new ShapeRenderer();
		
		invertColor();
	}
	
	public Color getBackgroundColor() { return backColor;}
	public Color getInvertedBackColor() {return invertbackColor;}
	public void setBackgroundColor(float r,float g,float b,float a) {
		
		backColor.set(r, g, b, a);
		invertColor();
	}
	private void invertColor() {
		
		invertbackColor.set(1f - backColor.r, 1f - backColor.g, 1f - backColor.b, 1f - backColor.a);
	}
	public static void initGridShader() {
		
		 String vertexShader = Gdx.files.internal("shaders/vertexShader").readString();
	     String fragmentShader = Gdx.files.internal("shaders/gridShader").readString();
	     
	     gridShader = new ShaderProgram(vertexShader, fragmentShader);
	     gridShader.pedantic = false;
	     System.out.println("Shader compiler log: " + gridShader.getLog());
	     
	     Pixmap p = new  Pixmap(1, 1, Format.RGB565);
	     nullTexture = new Texture(p);
	     gridBatch = new SpriteBatch();
	     gridBatch.setShader(gridShader);
	}

	final Vector3 tmp = new Vector3();
	final Vector3 mousecoord = new Vector3();
	@Override
	public void draw() {
		
		gridBatch.begin();
		camera.project(tmp);
		
		if(!NPhysics.menu) {
			
			mousecoord.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(mousecoord);
			mousecoord.set(snapGrid(mousecoord.x), snapGrid(mousecoord.y), 0);
		}
		gridShader.setUniformf("PX",(int)mousecoord.x);
		gridShader.setUniformf("PY", (int)mousecoord.y);
		
		
		camera.project(mousecoord);
		gridShader.setUniformf("grid", UNIT/camera.zoom);
		gridShader.setUniformf("xoffset", tmp.x);
		gridShader.setUniformf("yoffset", tmp.y);
		gridShader.setUniformf("X", Gdx.input.getX());
		gridShader.setUniformf("Y", Gdx.graphics.getHeight() - Gdx.input.getY());
		gridBatch.draw(nullTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridBatch.end();
		
		shapefill.begin(ShapeType.Filled);
		super.draw();
		shapefill.end();
		tmp.set(Vector3.Zero);
	}
	
	@Override
	public void updateMatrix() {
		
		shapefill.setProjectionMatrix(getCamera().combined);
		shapeline.setProjectionMatrix(getCamera().combined);
		shapepoint.setProjectionMatrix(getCamera().combined);
		getBatch().setProjectionMatrix(getCamera().combined);
		
	}
}
