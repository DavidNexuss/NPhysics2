package com.nsoft.nphysics.simulation.dsl;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import static com.nsoft.nphysics.simulation.dsl.Core.*;
public class DSL implements Dev{

	static ArrayList<Solid> solids;
	static ArrayList<Vertex> vertexs;
	
	Solid sol;
	
	boolean isXcomplete = true;
	boolean isYcomplete = true;
	boolean isMcomplete = true;
	
	int Xu,Yu;
	int relationu;
	ArrayList<Force> forces = new ArrayList<>();
	
	ArrayList<Force> xuforces = new ArrayList<>();
	ArrayList<Force> yuforces = new ArrayList<>();
	ArrayList<Force> uforces = new ArrayList<>();
	
	public DSL(Solid d) {
		
		sol = d;
		
		for (Force force : sol.extraForces) {
			
			forces.add(force);
			force.completeUsingTrigonometric();
			if(!force.hasX()) { isXcomplete = false; Xu++; xuforces.add(force);}
			if(!force.hasY()) { isYcomplete = false; Yu++; yuforces.add(force);}
			
			if(!force.hasX() || !force.hasY()) {
				
				uforces.add(force);
			}
		}
		
		if(isXcomplete || isYcomplete) {
			
			checkSum();
		}
		if(DEBUG) {
			
			say("Problem status:");
			if(uforces.size() > 0) {
				
				String fs = "";
				for (Force force : uforces) {
					
					fs += Dev.getid(force) + ", ";
				}

				say("Unknown forces: " + uforces.size() + " : " + fs);
			}

			say("isXcomplete: " + isXcomplete + ", Unknown X: " + Xu);
			say("isYcomplete: " + isYcomplete + ", Unknown Y: " + Yu);
		}
		
		
	}
	
	public void solve() {
		
		boolean usingMoments = useMoments();
		
		if(DEBUG) {
			
			say("Starting to solve...");
			say("UsingMoments: " + usingMoments);
		}
		
		if(usingMoments) {
			
			Force point = forces.get(0);
			int n = ((point.hasX() ? 0 : 1) + (point.hasY() ? 0 : 1));
			for (int i = 1; i < forces.size(); i++) {
				
				if(((forces.get(i).hasX() ? 0 : 1) + (forces.get(i).hasY() ? 0 : 1)) > n) {
					
					n = ((forces.get(i).hasX() ? 0 : 1) + (forces.get(i).hasY() ? 0 : 1));
					point = forces.get(i);
				}
			}
			
			float moment = 0;

			for (Force force : forces) {
				
				if(force != point && force.isDone()) {
					
					float angle = force.getVectorDist(point).angleRad();
					Vector2 unit = new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
					
					float fangle = force.getTemporalVector().angleRad(unit);
					float zangle = force.getTemporalVector().angleRad();
					
					float dist = force.getDist(point);
					float mod = force.mod();
					
					Vector2 distance = force.getVectorDist(point);
					moment += dist*Math.sin((zangle-fangle)*fangle/Math.abs(fangle))*mod * (distance.y < 0 ? -1 : 1);
					
				}
			}
			
			
			System.out.println(moment);
		}else {
					
			//SUM X
			
			float x = 0;
			float y = 0;
			
			for (Force force : forces) {
				
				if(!xuforces.contains(force)) {
					
					x+= force.getTemporalFloat()[0];
				}
				
				if(!yuforces.contains(force)) {
					
					y+= force.getTemporalFloat()[1];
				}
			}
			
			for (Force force : uforces) {
				
				force.setTempX(-x);
				force.end();
				force.setTempY(-y);
				force.end();
			}
			
			isXcomplete = true;
			isYcomplete = true;
		}
		
		if(DEBUG) {say("Solved");}
	}
	public boolean useMoments() {
		
		return Xu > 1 || Yu > 1;
	}
	public void checkSum() {
		
		if(!(isXcomplete && isYcomplete)) {
		
			System.err.println("Incomplete problem");
			return;
		}
		
		float x = sumX();
		float y = sumY();
		
		if(Math.abs(x) < 10e-5) x = 0;
		if(Math.abs(y) < 10e-5) y = 0;
		
		if(x == 0 && y == 0) {
			
			System.out.println("CheckSum Correct!");
		}else {
			
			System.out.println("Checksum Incorrect: ");
			System.out.println("X: " + x);
			System.out.println("Y: " + y);
		}
	}
	public float sum(Axis a) {
		
		if(a == Axis.X) {
				
			if(isXcomplete) {
				
				float sum = 0;
				
				for (Force force : forces) {
					
					sum+= force.mod() * Math.cos(force.getAngleRadians());
				}
				return sum;
			}else throw new IllegalStateException();
		}else {
			
			if(isYcomplete) {
				
				float sum = 0;
				
				for (Force force : forces) {
					
					sum+= force.mod() * Math.sin(force.getAngleRadians());
				}
				return sum;
			}else throw new IllegalStateException();
			
		}
		
	}
	
	public float sumX() {
		
		return sum(Axis.X);
	}
	
	public float sumY() {
		
		return sum(Axis.Y);
	}
	
	
	public void destroySolid(Solid d) {
		
		if(solids.contains(d))solids.remove(d);
		else throw new IllegalStateException();
		
		for (Vertex v : d.v) {
			
			vertexs.remove(v);
		}
	}
	
	/*
	public Vertex getNearestV(Vector2 pos) {
		
		float len = Float.MAX_VALUE;
		Vertex r = null;
		for (Solid solid : solids) {
			if(solid.closestPoint(a))
			
		}
	}*/
	
	static enum Axis{X,Y}
}
