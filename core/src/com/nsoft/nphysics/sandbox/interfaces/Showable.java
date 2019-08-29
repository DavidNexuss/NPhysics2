/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics.sandbox.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nsoft.nphysics.ThreadManager;

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

		setVisible(true);
		addAction(Actions.fadeIn(getFadeDuration(), getFadeInterpolation()));
	}
	
	public default void hide() {
	
		addAction(Actions.fadeOut(getFadeDuration(),getFadeInterpolation()));
		ThreadManager.createTask(()->{setVisible(false);}, getFadeDuration());
	}
	
	public void setVisible(boolean v);
	public Color getColor();
	public void addAction(Action a);
}
