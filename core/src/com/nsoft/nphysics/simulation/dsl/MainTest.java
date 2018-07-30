package com.nsoft.nphysics.simulation.dsl;

import com.badlogic.gdx.math.Vector2;
import static com.nsoft.nphysics.simulation.dsl.Core.*;

import static com.nsoft.nphysics.simulation.dsl.Force.*;
public class MainTest {

	static DSL test;
	public static void runTest() {
		
		Solid a = new Solid(new Vector2(20, 20));
		
		
		a.extraForces.add(force(30, 20, NULL, NULL));
		a.extraForces.add(force(10, 20, 0, NULL));
		test = new DSL(a);
		test.solve();
		
		test.checkSum();
		
	}
}
