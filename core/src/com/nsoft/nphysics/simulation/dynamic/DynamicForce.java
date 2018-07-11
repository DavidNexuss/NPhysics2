package com.nsoft.nphysics.simulation.dynamic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.SimpleArrow;
import com.nsoft.nphysics.sandbox.Util;

public class DynamicForce {

	Vector2 origin;
	Vector2 force;
	Vector2 porigin;
	Vector2 pforce;
	boolean isRelative = true;
	SimpleArrow arrow;
	
	Vector2 getOrigin() {
		
		return origin;
	}
	
	Vector2 getForce() {
		
		return force;
	}
	
	Vector2 getPhysicalOrigin() {
		
		return isRelative ? porigin : origin;
	}
	
	Vector2 getPhysicalForce() {
		
		return isRelative ? pforce : force;
	}
	private Vector2 getStart() {
		
		return new Vector2(origin).scl(Util.UNIT);
	}
	
	private Vector2 getEnd() {
		
		return new Vector2(origin).add(force).scl(Util.UNIT);
	}
	
	void init() {
		
		arrow = new SimpleArrow(new Vector2(origin).scl(Util.UNIT), new Vector2(origin).add(force).scl(Util.UNIT));
		NPhysics.currentStage.addActor(arrow);
	}
	void update(Body b,Vector2 pivot) {
		
		if(isRelative) {
			
			porigin = Util.rotPivot(pivot, this.origin, b.getAngle());
			pforce = Util.rot(force, b.getAngle());
			Vector2 pend = Util.rotPivot(pivot, new Vector2(this.origin).add(this.force), b.getAngle());
			arrow.setStart(new Vector2(porigin).scl(Util.UNIT));
			arrow.setEnd(new Vector2(pend).scl(Util.UNIT));
			
		}else {
		
			arrow.setStart(getStart());
			arrow.setEnd(getEnd());
		}
		
		arrow.updateVertexArray();
	}
}
