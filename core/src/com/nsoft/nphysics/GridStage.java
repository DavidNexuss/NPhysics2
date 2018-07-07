package com.nsoft.nphysics;

import static com.nsoft.nphysics.sandbox.Util.UNIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GridStage extends DragStage{

	public static ShaderProgram gridShader;
	public static SpriteBatch gridBatch;
	public static Texture nullTexture;
	
	public GridStage(Viewport v) {
		
		super(v);
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
	@Override
	public void draw() {
		
		gridBatch.begin();
		camera.unproject(tmp);
		gridShader.setUniformf("grid", UNIT/camera.zoom);
		gridShader.setUniformf("width", Gdx.graphics.getWidth());
		gridShader.setUniformf("height", Gdx.graphics.getHeight());
	//	gridShader.setUniformf("yoffset", camera.position.y/camera.zoom);
	//	gridShader.setUniformf("xoffset", camera.position.x/camera.zoom);
		gridShader.setUniformf("zoom",camera.zoom);
		gridShader.setUniformf("X", Gdx.input.getX());
		gridShader.setUniformf("Y", Gdx.graphics.getHeight() - Gdx.input.getY());
		
		gridShader.setUniformf("xoffset", tmp.x);
		gridShader.setUniformf("yoffset", tmp.y);
		gridBatch.draw(nullTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridBatch.end();
		super.draw();
		tmp.set(Vector3.Zero);
	}
}
