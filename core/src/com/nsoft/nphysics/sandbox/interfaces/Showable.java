package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public interface Showable {

	public default float getFadeDuration() { return 1f;}
	public default Interpolation getFadeInterpolation() { return Interpolation.exp5;}
	
	public default void setAlpha(float newAlpha) {
		
		getColor().a = newAlpha;
	}
	
	public default float getAlpha() {
		
		return getColor().a;
	}
	public default void show() {

		addAction(Actions.fadeIn(getFadeDuration(), getFadeInterpolation()));
	}
	
	public default void hide() {
	
		addAction(Actions.fadeOut(getFadeDuration(),getFadeInterpolation()));
	}
	
	public Color getColor();
	public void addAction(Action a);
}