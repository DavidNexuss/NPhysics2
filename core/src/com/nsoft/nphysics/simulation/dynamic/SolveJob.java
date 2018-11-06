package com.nsoft.nphysics.simulation.dynamic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;

public class SolveJob implements Say{

	static int threshold = 10;
	final Vector2 position = new Vector2(3, 0);
	PhysicalActor<?> obj;
	public SolveJob(PhysicalActor<?> obj) {
		this.obj = obj;
	}
	
	public boolean start() {
	
		float C;
		int it = 100;
		
		float a = -1000;
		float b = 1000;
		float c;
		
		do {
		
			c = (a+b) /2f;
			
			C = function(c);
			
	//		say(C + " " + c);
			if(function(a) * C < 0) b = c;
			else if(function(b)* C < 0) a = c;
			
		} while (it-- > 0);
		
		say(c);
		return true;
	}
	
	private World createWorld() {
		
		return new World(SimulationStage.gravity, true);
	}
	
	PolygonObject var;
	
	private float function(float argument) {
		
		World w = createWorld();
		addObjects(w);
		var.b.applyForce(new Vector2(0, argument), new Vector2(position).add(var.b.getPosition()), true);
		
		w.step(1, 8, 6);
		
		Array<Body> b = new Array<>();
		w.getBodies(b);
		return var.b.getAngularVelocity();
	}
	private void addObjects(World world) {
		
		for (PhysicalActor<ObjectDefinition> d: SimulationPackage.polygons)  {
			
			PolygonObject o = new PolygonObject(d.getDefinition(),world);
			if(obj== d) var = o;
		}
	}
}
