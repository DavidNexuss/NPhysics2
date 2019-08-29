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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage.Simulation;
/**
 * Classe per resoldre utilitzant el teorema de Bolzano
 * una situació estàtica on la suma de moments d'una
 * forcça igual a 0 necessàriament impliqui que la 
 * suma de forces també ho sigui.
 * @author David
 */
public class SolveJob implements Say{

	public static float waitTime = 0.001f; //Temps d'espera
	public static float exp = -14;
	Vector2 position; //Posició de la força a treballar
	float rad; //Angle de la força en radians
	PhysicalActor<?> obj; //L'objecte en qüestió
	ForceComponent f; //Força
	
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
		
		float a = -10000;	//Mínim
		float b = 10000;		//Máxim
		//Valors arbitraris prestablerts 
		//(podrien formar part d'una variable del programa) si fos necessari
		float c = -1; 			//Módul de la força
		
		float Epsilon = (float) Math.pow(10, exp); //Tolerància
		say(Epsilon);
		float it = 0;
		float oldc;
		do {
		
			oldc = c;
			c = (a+b) /2f;
			C = function(c);
			
			if(function(a) * C < 0) {
				b = c;
			}
			else if(function(b)* C < 0) {
				a = c;
			}
			
			say(it + ":{  " + c  + " " + C);
			it++;
		} while ((C > Epsilon || C < -Epsilon) && oldc != c);
		
		f.setForce(new Vector2(c * MathUtils.cos(rad),c * MathUtils.sin(rad)));
		f.setVar(false);
		
		say(c + " " + it + " " + C);
		return true;
	}
	
	PolygonObject var;
	
	/**
	 * Versió anàloga d'una funció matemàtica per calcular el
	 * moment d'una força. Utilitza el calcul del paquet de simulació
	 * dinàmica que utilitza una simulació de Box2D
	 * @param argument el nou paràmetre, el valor de la força
	 * @return la velocitat angular del objecte en qüestió
	 */
	private float function(float argument) {
		
		Simulation s = new Simulation(SimulationStage.initWorld());
		SimulationStage.initObjects(s, false);
		SimulationStage.initRawJoints(s, false);
		SimulationStage.initWater(s);
		var = s.objectsMap.get(obj);
		
		for (int i = 0; i < 10; i++) {
			
			var.b.applyForce(new Vector2(MathUtils.cos(rad) *argument,MathUtils.sin(rad) * argument), new Vector2(position), true);
			s.aplyForces();
			s.world.step(waitTime/10f, 8, 6); 
		}
		//waitTime es una variable definida pel programa per calcular la cantitat de temps a avançar
		
		Array<Body> b = new Array<>();
		s.world.getBodies(b);
		return var.b.getAngularVelocity();
	}
}
