package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public interface ClickIn {

	public default void addInput() {
		
		addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				x = Gdx.input.getX();
				y = Gdx.input.getY();
				Vector3 coords = getStage().getCamera().unproject(new Vector3(x, y, 0));
				x = coords.x;
				y = coords.y;
				
				if(isInside(x, y)) { event.cancel(); select();}
			}
		});
	}
	
	public boolean isInside(float x,float y);
	public void select();
	
	public boolean addListener(EventListener input);
	public Stage getStage();
}
