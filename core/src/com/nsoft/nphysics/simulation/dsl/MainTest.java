package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;

public class MainTest {

	public static boolean DEBUG = true;
	public static void runTest() {
		
		Force a = new Force(0, 0, Force.NULL, Force.NULL);
		Force g = new Force();
		g.setPolarPosition(5, 60);
		g.setForce(0, -98f);
		
		Force f = new Force();
		f.setPolarPosition(10, 60);
		f.setForce(0, Force.NULL);
		f.setVariable(Variable.Y);
		
		DSL d = new DSL();
		d.addForce(a);
		d.addForce(g);
		d.addForce(f);
		d.solve();
		
	}
}
