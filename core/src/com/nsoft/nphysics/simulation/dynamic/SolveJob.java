package com.nsoft.nphysics.simulation.dynamic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage.Simulation;

public class SolveJob implements Say{

	public static float waitTime = 3f;
	static int threshold = 10;
	Vector2 position;
	float rad;
	PhysicalActor<?> obj;
	ForceComponent f;
	
	public SolveJob(PhysicalActor<?> obj,ForceComponent f) {
		this.obj = obj;
		this.position = f.getPosition().scl(1f/Util.METERS_UNIT());
		this.rad = f.getForce().angleRad();
		this.f = f;
	}
	public boolean start() {
	
		
		//S'executa bolzano en un rang inicial 
		//comprés entre a i b
		
		float C;			//Velocitat angular
		
		float a = -1000;	//Mínim
		float b = 1000;		//Máxim 
		float c; 			//Módul de la força
		
		float Epsilon = 0.000001f;
		
		float it = 0;
		do {
		
			c = (a+b) /2f;
			C = function(c);
			
			if(function(a) * C < 0) {
				b = c;
			}
			else if(function(b)* C < 0) {
				a = c;
			}
			
			it++;
		} while (C > Epsilon || C < -Epsilon);
		
		f.setForce(new Vector2(c * MathUtils.cos(rad),c * MathUtils.sin(rad)));
		f.setVar(false);
		
		say(c + " " + it + " " + C);
		return true;
	}
	
	PolygonObject var;
	
	private float function(float argument) {
		
		Simulation s = new Simulation(SimulationStage.initWorld());
		SimulationStage.initObjects(s, false);
		SimulationStage.initRawJoints(s, false);

		var = s.objectsMap.get(obj);
		var.b.applyForce(new Vector2(MathUtils.cos(rad) *argument,MathUtils.sin(rad) * argument), new Vector2(position), true);
		var.aplyForce();
		s.world.step(waitTime, 8, 6);
		
		Array<Body> b = new Array<>();
		s.world.getBodies(b);
		return var.b.getAngularVelocity();
	}
}
