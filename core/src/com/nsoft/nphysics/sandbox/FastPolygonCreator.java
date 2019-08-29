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

package com.nsoft.nphysics.sandbox;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.NPhysics;

/**
 * Classe encarregada de la creaci� dels cossos a la fase Sandbox.
 * Gestiona quins passos s'han de seguir davant l'input.
 * @see Sandbox
 * @author David
 */
public class FastPolygonCreator {

	
	public static PhysicalActor<?> temp;
	
	/**
	 * Dunci� cridada cada vegada que l'usuari clica a l'escenari per afegir un cos.
	 * Automatitza el proc�s de creaci� i maneig dels cossos.
	 * Crea un cos si no est� creat o l'anterior ja esta finalitzat,
	 * afegeix punts si el cos no esta finalitzat, en cas de que se seleccioni un punt ja existen
	 * es reutilitza i per �ltim finalitza un cos si es selecciona el primer punt d'aquest.
	 * En cas de que es tracte de la creaci� d'un cos especial on els punts no defineixen els limits d'aquest,
	 * per exemple {@link CircleActor} s'utilitza un proc�s individual.
	 * @param x posici� x del cursor desprojectada
	 * @param y posici� y del cursor desprojectada
	 */
	public static void handleClick(float x,float y) {
		
		if(GameState.is(GState.CREATE_FAST_POLYGON)) {
			
			if(temp == null || temp.isEnded()) create();
			
			Point p = Point.getPoint(x, y);

			temp.addPoint(p);
			if(temp.isEnded()) NPhysics.currentStage.addActor(temp);
		}else if(GameState.is(GState.CREATE_CIRCLE)) {
			
			if(temp == null || temp.isEnded()) {
				
				temp = new CircleActor(Point.getPoint(x, y));
				NPhysics.currentStage.addActor(temp);
				temp.setZIndex(0);
				
			}else {
				temp.addPoint(Point.getPoint(x, y));
			}
		}
	}
	
	/**
	 * Crea un nou cos poligonal i l'emmagatzema a una variable temporal
	 */
	private static void create() {
		
		temp = new PolygonActor();
	}
	
	/**
	 * Crea un cos poligonal en forma de circunfer�ncia utilitzant PoligonActor
	 * Aquest m�tode �s extremadament ineficient ja que hem de transformar una circunfer�ncia en
	 * un poligon regular d'un nombre molt alt de v�rtexs.
	 * Per crear un cos circular s'utilitza la variant {@link CircleActor}
	 * 
	 * Aquesta funci� nom�s t� un �s per debugueig
	 * @param center posici� mundial del centre de la circunfer�ncia
	 * @param radius radi de la circunfer�ncia
	 */
	public static void createCircle(Vector2 center,float radius) {
		
		create();
		
		Point start = Point.getPoint(center.x + radius, center.y);
		temp.addPoint(start);
		
		for (int i = 1; i < 360; i++) {
			
			Vector2 proj = new Vector2(center.x + radius,center.y);
			proj = Util.rotPivot(center, proj, i * MathUtils.degRad);
			Point p = new Point(proj.x, proj.y, false);
			NPhysics.currentStage.addActor(p);
			p.setVisible(false);
			temp.addPoint(p);
		}
		
		start.setVisible(false);
		temp.addPoint(start);
		
		NPhysics.currentStage.addActor(temp);
	}
	
	
}
