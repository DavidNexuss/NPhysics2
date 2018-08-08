package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;
import com.nsoft.nphysics.simulation.dsl.Force.Variable;

public class MainTest {

	public static boolean DEBUG = true;
	public static void runTest() {
		
		Force a = new Force(0, 0, Force.NULL, Force.NULL);
		Force g = new Force();
		g.setPosition(3.5f, 1.5f);
		g.setForce(0, -205f);
		
		Force f = new Force();
		f.setPosition(1, 3);
		f.setForce(Force.NULL,20);
		f.setVariable(Variable.Y);
		
		DSL d = new DSL();
		d.addForce(a);
		d.addForce(g);
		d.addForce(f);
		d.solve();
		
	}
}
