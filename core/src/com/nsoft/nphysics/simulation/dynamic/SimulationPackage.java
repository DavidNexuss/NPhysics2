package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.Sandbox;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class SimulationPackage {

	static ArrayList<PolygonActor> polygons = NPhysics.sandbox.polygonlist;
	public static final ArrayList<RawJoint> rawJoints = new ArrayList<>();
	
	public static void update() {
		polygons = NPhysics.sandbox.polygonlist;
	}
}
