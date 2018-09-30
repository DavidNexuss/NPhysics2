package com.nsoft.nphysics;

import static com.nsoft.nphysics.sandbox.Util.UNIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
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
	private void invertColor() {
		
		invertbackColor.set(1f - backColor.r, 1f - backColor.g, 1f - backColor.b, 1f - backColor.a);
	}
	/**
	 * Inicialitza els shaders de la cuadricula
	 */
	public static void initGridShader() {
		
		 String vertexShader = Gdx.files.internal("shaders/vertexShader").readString();
	     String fragmentShader = Gdx.files.internal("shaders/gridShader").readString();
	     
	     gridShader = new ShaderProgram(vertexShader, fragmentShader);
	     ShaderProgram.pedantic = false; //Per problemes de compilació del shader es recomanable desactivar pedantic
	     System.out.println("Shader compiler log: " + gridShader.getLog());
	     
	     //Crea una textura buida sobre la que renderitza la cuadricula
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
		
		if(!NPhysics.menu && hasFocus()) {
			
			mousecoord.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(mousecoord);
			if(isSnapping())mousecoord.set(snapGrid(mousecoord.x), snapGrid(mousecoord.y), 0);
			camera.project(mousecoord);
		}
		gridShader.setUniformf("PX",(int)mousecoord.x);
		gridShader.setUniformf("PY", (int)mousecoord.y);
		
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
	
	public boolean hasFocus() {
		return focus;
	}
	public void setFocus(boolean newFocus) {
		focus = newFocus;
	}
}
