package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nsoft.nphysics.sandbox.SelectHandle;

public interface ClickIn {

	public default void addInput() {
		
		ClickIn pointer = this;
		addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				x = Gdx.input.getX();
				y = Gdx.input.getY();
				Vector3 coords = unproject(x, y);
				x = coords.x;
				y = coords.y;
				
				if(isInside(x, y)) { event.cancel(); handleClick(pointer);}
			}
		});
	}
	
	public Vector3 tmp = new Vector3();
	public default Vector3 unproject(float x,float y) {
		
		return getStage().getCamera().unproject(tmp.set(x, y,0));
	}
	
	public default boolean isInside(Vector3 v) {return isInside(v.x, v.y);}
	public default boolean isInside(Vector2 v) {return isInside(v.x, v.y);}
	public default void handleClick(ClickIn pointer) {getHandler().setSelected(pointer);}
	
	public boolean isInside(float x,float y);
	public void select();
	public void unselect();
	public SelectHandle getHandler();
	public boolean addListener(EventListener input);
	public Stage getStage();
}
