package com.nsoft.nphysics;

import static com.nsoft.nphysics.sandbox.Util.UNIT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GridStage extends DragStage{

	public static ShaderProgram gridShader;
	public static SpriteBatch gridBatch;
	public static Texture nullTexture;
	
	public GridStage(Viewport v) {
		
		super(v);
		initGridShader();
	}
	
	private void initGridShader() {
		
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
	
	@Override
	public void draw() {
		
		gridBatch.begin();
		gridShader.setUniformf("grid", UNIT/camera.zoom);
		gridShader.setUniformf("width", Gdx.graphics.getWidth());
		gridShader.setUniformf("height", Gdx.graphics.getHeight());
		gridShader.setUniformf("yoffset", camera.position.y/camera.zoom);
		gridShader.setUniformf("xoffset", camera.position.x/camera.zoom);
		gridBatch.draw(nullTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gridBatch.end();
		super.draw();
	}
}
