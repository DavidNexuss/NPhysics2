package com.nsoft.nphysics.simulation.dynamic;

import java.util.ArrayList;

import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.WaterComponent;
import com.nsoft.nphysics.sandbox.interfaces.RawJoint;

/**
 * Classe encarregada de mantenir punters a diferents llistes per fer de pont entre la fase
 * Sandbox i la simulació
 * @author David
 */
public class SimulationPackage {

	static ArrayList<PhysicalActor<?>> polygons = NPhysics.sandbox.polygonlist;
	public static ArrayList<RawJoint> rawJoints = new ArrayList<>();
	public static ArrayList<WaterComponent> waterComponents = new ArrayList<>();
	
	public static void update() {
		polygons = NPhysics.sandbox.polygonlist;
		rawJoints = new ArrayList<>();
		waterComponents = new ArrayList<>();
	}
}
