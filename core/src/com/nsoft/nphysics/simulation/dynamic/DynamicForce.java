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

package com.nsoft.nphysics.simulation.dynamic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.ForceComponent.Type;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;

/**
 * Classe encarregada de renderitzar una fletxa que representi el vector de la força
 * @author David
 */
public class DynamicForce {

	Vector2 origin;
	Vector2 force;
	Vector2 porigin;
	Vector2 diff;
	Vector2 pforce;
	ForceComponent.Type type;
	
	ArrowLabel label;
	boolean isCentered = false;
	
	SimpleArrow arrow;
	
	boolean show = true;
	Vector2 getOrigin() {
		
		return origin;
	}
	
	Vector2 getForce() {
		
		return force;
	}
	
	Vector2 getPhysicalOrigin() {
		
		return type == Type.TRANS || type == Type.REL ? porigin : origin;
	}
	
	Vector2 getPhysicalForce() {
		
		return new Vector2(type == Type.TRANS || type == Type.REL ? pforce : force).scl(Util.NEWTON_FACTOR);
	}
	private Vector2 getStart() {
		
		return new Vector2(porigin).scl(Util.METERS_UNIT());
	}
	
	private Vector2 getEnd() {
		
		return new Vector2(pforce).add(porigin).scl(Util.METERS_UNIT());
	}
	
	void init(Type t) {
		
		arrow = new SimpleArrow(new Vector2(origin).scl(Util.METERS_UNIT()), new Vector2(origin).add(force).scl(Util.METERS_UNIT()));
		arrow.setColor(Color.RED);
		porigin = origin;
		pforce = force;
		type = t;
		
		if(NPhysics.currentStage == NPhysics.simulation) {
			NPhysics.currentStage.addActor(arrow);
			createLabel();
		}
		
	}
	
	void updateLabel() {
		
		label.setPosition(new Vector2(arrow.getStart()).add(new Vector2(60, 30)));
		label.setColor(show ? Color.RED : Color.RED.cpy().mul(1, 1, 1, PolygonObject.hidealpha));
		
		arrow.setColor(show ? Color.RED : Color.RED.cpy().mul(1, 1, 1, PolygonObject.hidealpha));
	}
	void update(Body b,Vector2 pivot,boolean usingPosition) {
		
		
		if(type == Type.WORLD) {
			
			arrow.setStart(getStart());
			arrow.setEnd(getEnd());
		}
		
		if(type == Type.TRANS) {
			
			porigin = new Vector2(diff).add(b.getPosition());
			pforce = force;
			
			arrow.setStart(getStart());
			arrow.setEnd(getEnd());
			
		}
		
		if(type == Type.REL) {
			
			porigin = Util.rotPivot(pivot, new Vector2(diff).add(b.getPosition()), b.getAngle());
			pforce = Util.rot(force, b.getAngle());
			Vector2 pend = Util.rotPivot(pivot, new Vector2(this.force).add(new Vector2(diff).add(b.getPosition())), b.getAngle());
			arrow.setStart(new Vector2(porigin).scl(Util.METERS_UNIT()));
			arrow.setEnd(new Vector2(pend).scl(Util.METERS_UNIT()));

		}
		
		arrow.updateVertexArray();
		
		
	}
	
	void createLabel() {
		
		label = new ArrowLabel(NPhysics.simulation.getUiGroup());
		label.setFloat(pforce.len()*10);
		label.conc("N");
		label.setColor(Color.RED);
	}
	
}
