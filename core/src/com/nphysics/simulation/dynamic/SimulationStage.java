package com.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nsoft.nphysics.GridStage;
import com.nsoft.nphysics.sandbox.Polygon;

public class SimulationStage extends GridStage{

	static ArrayList<PolygonObject> objects;
	static World world;
	
	static ShapeRenderer fill;
	static ShapeRenderer line;
	
	public SimulationStage(Camera camera) {
		
		super(new ScreenViewport(camera));
		initShapeRenderer();
	}
	
	public void cleanAndSetUp() {
		
		clear();
		initWorld();
		initObjects();
	}
	private void initShapeRenderer() {
		
		fill = new ShapeRenderer();
		line = new ShapeRenderer();
	}
	private void initWorld() {
		
		world = new World(new Vector2(0, -9.8f), true);
	}
	private void initObjects() {
		
		objects = new ArrayList<>();
		for (Polygon d: SimulationPackage.polygons)  {
			
			PolygonObject o = new PolygonObject(d.getDefinition());
			objects.add(o);
			addActor(o);
		}
	}
	
	@Override
	public void draw() {
		
		fill.begin(ShapeType.Filled);
		fill.setProjectionMatrix(getCamera().combined);
		world.step(Gdx.graphics.getDeltaTime(), 8, 4);
		super.draw();
		fill.end();
	}

	@Override
	public void updateMatrix() {
		
		fill.setProjectionMatrix(getCamera().combined);
		line.setProjectionMatrix(getCamera().combined);
		getBatch().setProjectionMatrix(getCamera().combined);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		if(!super.touchDown(screenX, screenY, pointer, button)) {
			
			setCenter(screenX, screenY);
		}
		
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		if(!super.touchDragged(screenX, screenY, pointer)) {
			
			dragCamera(screenX, screenY);
		}

		return super.touchDragged(screenX, screenY, pointer);
	}
}
