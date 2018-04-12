package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Sandbox extends Stage {

	public static ShapeRenderer shaperenderer;
	public Sandbox() {
		super();
		
		//VARIABLE INIT:
		shaperenderer = new ShapeRenderer();
		shaperenderer.setColor(Color.BLACK);
		init();
	}
	
	//------------INIT-METHODS--------------
	private void init() {
		
		initdebug();
	}
	
	private void initdebug() {
		
		GameState.set(State.HOOK_FORCE_ARROW);
		ArrowActor.debug = new ArrowActor(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2));
		ArrowActor.hook(ArrowActor.debug);
		addActor(ArrowActor.debug);
	}
	
	
	//-----------LOOP-METHODS---------------
	@Override
	public void draw() {
		
		shaperenderer.begin(ShapeType.Filled);
		super.draw();
		shaperenderer.end();
	}
	
	@Override
	public void act() {

		super.act();
	}
	
	//--------DRAW-METHODS-----------------
	
	
	//---------------------INPUT--------------------------//
	
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		switch (GameState.current) {
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.unhook();
			break;

		default:
			break;
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		switch (GameState.current) {
		case HOOK_FORCE_ARROW2:
			
			ArrowActor.updateHook(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			break;

		default:
			break;
		}
		return super.mouseMoved(screenX, screenY);
	}
}
