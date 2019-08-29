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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nsoft.nphysics.sandbox.SelectHandle;

/**
 * Interfície per poder manejar objectes amb la propietat de ser seleccionats
 * @author David
 */
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
	
	public default boolean isSelected() {return getHandler().isSelected(this);} /**@return si l'objecte esta seleccionat per el seu controlador o no*/
	public default boolean isLastSelected() { return getHandler().isLastSelected(this);} /**@return si l'objecte es el primer seleccionat per el seu controlador*/
	public default boolean isInside(Vector3 v) {return isInside(v.x, v.y);}
	public default boolean isInside(Vector2 v) {return isInside(v.x, v.y);}
	public default boolean isMouseInside() {
		
		return isInside(unproject(Gdx.input.getX(), Gdx.input.getY()));
	}
	
	public default void handleClick(ClickIn object,int pointer) {
		if(getHandler() == null) return;
		getHandler().setSelected(object,pointer);
	}
	/**
	 * Funció per saber si un punt cau dins del objecte, cal ser implementada per la classe, i hit hauria de seguir el mateix procediment
	 * @param x Coordenada en X del punter
	 * @param y Coordenada en Y del punter
	 * @return si el punt està dins o no
	 */
	public boolean isInside(float x,float y);
	public default void click(int pointer) {}
	public void select(int pointer);
	public void unselect();
	public default void setHandler(SelectHandle s) {}
	
	public SelectHandle getHandler();
	public boolean addListener(EventListener input);
	
	public Stage getStage(); //implementada per Actor
}
