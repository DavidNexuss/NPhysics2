package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Sandbox extends Stage {

	public static ShapeRenderer shapefill;
	public static ShapeRenderer shapeline;
	public static ShapeRenderer shapepoint;
	
	public static BitmapFont bitmapfont;
	
	public Sandbox() {
		super();
		
		//VARIABLE INIT:
		shapefill = new ShapeRenderer();
		shapeline = new ShapeRenderer();
		shapepoint = new ShapeRenderer();
		
		bitmapfont = new BitmapFont();
		init();
	}
	
	//------------INIT-METHODS--------------
	private void init() {
		
		initdebug();
	}
	
	private void initdebug() {
		/*
		Vector2 center = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		GameState.set(State.HOOK_FORCE_ARROW);
		ArrowActor.debug = new ArrowActor(new Vector2(center.x, center.y));
		ArrowActor.hook(ArrowActor.debug);
		addActor(ArrowActor.debug);
		
		Axis axis = new Axis(new Vector2(center.x, center.y), 0);
		addActor(axis);*/
		
	}
	
	
	//-----------LOOP-METHODS---------------
	@Override
	public void draw() {
		
		shapefill.begin(ShapeType.Filled);
		shapepoint.begin(ShapeType.Point);
		shapeline.begin(ShapeType.Line);
		
		super.draw();
		
		shapefill.end();
		shapepoint.end();
		shapeline.end();
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
