package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.nsoft.nphysics.sandbox.PolygonActor;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

public class SimulationPackage {

	static ArrayList<PolygonActor> polygons = PolygonActor.polygonlist;
	public static final ArrayList<RawJoint> rawJoints = new ArrayList<>();
}
