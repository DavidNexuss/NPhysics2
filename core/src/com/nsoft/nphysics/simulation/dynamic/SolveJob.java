package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;

public class SolveJob {

	static int threshold = 10;
	World w;
	ForceComponent u;
	
	int n;
	public SolveJob(ForceComponent u) {
		
		this.u = u;
	}
	
	public boolean start() {
	
		
		int it = 0;
		float oldsump = 0;
		float sump = 1;
		while (sump > 0.1f) {
			
			if(u.variable == Variable.X) {
				u.setForce(new Vector2(n, u.getForce().y));
				System.out.println("x");
			}
			if(u.variable == Variable.Y) {
				u.setForce(new Vector2(u.getForce().x, n));
				System.out.println("y");
			}
			
			createWorld();
			addObjects();
			
			it++;
			sump = run();
			
			if(sump > oldsump) n += sump;
			if(sump < oldsump) n -= sump;
			
			oldsump = sump;
			System.out.println(it + " " + sump + " " + u.getForce());
			if(it > threshold) return false;
			
		}
		return true;
	}
	
	private void createWorld() {
		
		w = new World(SimulationStage.gravity, true);
	}
	
	PolygonObject var;
	private void addObjects() {
		
		for (PolygonActor d: SimulationPackage.polygons)  {
			
			PolygonObject o = new PolygonObject(d.getDefinition(),w);
			if(u.getPolygon() == d) var = o;
		}
	}
	
	public float run() {
		
		float t = 0;
		float elapsed = 0.2f;
		while(t < 2f) {
		
			var.b.applyForceToCenter(u.getForce(), true);
			w.step(elapsed, 10, 8);
			t+=elapsed;
		}
		
		Array<Body> b = new Array<>();
		w.getBodies(b);
		float sump = 0;
		for (Body body : b) {
			
			sump += body.getMass()*body.getLinearVelocity().len();
		}
		
		return sump;
	}
	
	
}
