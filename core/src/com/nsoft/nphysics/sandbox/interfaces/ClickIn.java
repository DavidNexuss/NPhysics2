package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public interface ClickIn<T extends Actor> {

	public default void addInput() {
		
		final T dis = (T)this;
		dis.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				x = Gdx.input.getX();
				y = Gdx.input.getY();
				Vector3 coords = dis.getStage().getCamera().unproject(new Vector3(x, y, 0));
				x = coords.x;
				y = coords.y;
				
				if(isInside(x, y)) { event.cancel(); select();}
			}
		});
	}
	
	public boolean isInside(float x,float y);
	
	public void select();
}
