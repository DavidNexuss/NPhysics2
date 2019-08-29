/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

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
