package com.nsoft.nphysics.simulation.dsl;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;

public class DSL implements Say{

	private ArrayList<Force> active;
	private ArrayList<Force> unknowns;
	private ArrayList<Force> forces;
	
	private Force lastAxis;
	
	public DSL() {
		
		forces = new ArrayList<>(); 
		unknowns = new ArrayList<>();
		active = new ArrayList<>();
	}
	
	public void addForce(Force force) {
		
		if(!force.isKnown(false)) {
			unknowns.add(force);
		}else active.add(force);
		
		forces.add(force);
		
		if(!force.isKnown(true)) {
			
			lastAxis = force;
		}
	}
	
	public int unknownXCount() {
		int c = 0;
		for (Force force : unknowns) {
			if(!force.isXKnown()) c++;
		}
		
		return c;
	}
	
	public int unknownYCount() {
		
		int c = 0;
		for (Force force : unknowns) {
			if(!force.isYKnown())c++;
		}
		return c;
	}
	
	public int unknownCount() {
		
		int c = 0;
		for (Force force : unknowns) {
			if(!force.isXKnown())c++;
			if(!force.isYKnown())c++;
		}
		
		return c;
	}
	public void solve() {
		
		int unknown = unknownCount();
		int unknownX = unknownXCount();
		int unknownY = unknownYCount();
		
		if(!(unknownX > 1) && !(unknownY > 1)) {
			
			say("Unsificient variables");
			say("X: " + unknownX);
			say("Y: " + unknownY);
		}
		say(unknown + " unknown variables, having " + unknown/2 + " unknown forces");
		
		if(unknown == 3 && (unknownY == 2 || unknownX == 2)) {
			
			float sum = 0;
			
			for (Force force : active) {
				
				float angle = new Vector2(force.getPositionVector()).sub(lastAxis.getPositionVector()).angle();
				Force prj = UtilDSL.getProjectedForce(force, -angle);
				sum += new Vector2(prj.getPositionVector()).sub(lastAxis.getPositionVector()).x * prj.getForceVector().y;
				System.out.println(sum);
			}
			
			sum = -sum;
			
			Force u = null;
			
			for (Force force : unknowns) {
				
				if(force.isXKnown() && !force.isYKnown() || !force.isXKnown() && force.isYKnown()) u = force;
			}
			
				
				float angle = new Vector2(u.getPositionVector()).sub(lastAxis.getPositionVector()).angle();
				Force prj = UtilDSL.getProjectedForce(u, -angle);
				float tempy = sum / (prj.getPositionVector().x - lastAxis.getPositionVector().x);
				
				if(u.getVariableType() == Variable.X) {
					
					prj.setForce((float) -(tempy / Math.tan(angle * MathUtils.degRad)), tempy);
					
				}else if (u.getVariableType() == Variable.Y) {
					
					prj.setForce((float) (tempy * Math.tan(angle * MathUtils.degRad)), tempy);
					
				}else throw new IllegalStateException();
				
				
				
				say(prj);
				prj = UtilDSL.getProjectedForce(prj, angle);
				u.setForce(prj.getForceVector().x, prj.getForceVector().y);
				say(u);
			
		}
	}
}
