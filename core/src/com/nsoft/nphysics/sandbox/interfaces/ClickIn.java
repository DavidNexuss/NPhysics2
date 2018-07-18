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
		
		ClickIn object = this;
		addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if(isMouseInside()) { handleClick(object,event.getPointer()); click(event.getPointer()); event.cancel(); }
			}
		});
	}
	
	public Vector3 tmp = new Vector3();
	public default Vector3 unproject(float x,float y) {
		
		return getStage().getCamera().unproject(tmp.set(x, y,0));
	}
	
	public default boolean isSelected() {return getHandler().isSelected(this);}
	public default boolean isInside(Vector3 v) {return isInside(v.x, v.y);}
	public default boolean isInside(Vector2 v) {return isInside(v.x, v.y);}
	public default boolean isMouseInside() {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY()));
	}
	
	public default void handleClick(ClickIn object,int pointer) {
		if(getHandler() == null) return;
		getHandler().setSelected(object,pointer);
	}
	
	public boolean isInside(float x,float y);
	public default void click(int pointer) {}
	public void select(int pointer);
	public void unselect();
	public default void setHandler(SelectHandle s) {}
	
	public SelectHandle getHandler();
	public boolean addListener(EventListener input);
	public Stage getStage();
}
