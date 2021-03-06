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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.NPhysics;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.SpringComponent;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.drawables.SimpleArrow;
import com.nsoft.nphysics.sandbox.drawables.Spring;
import com.nsoft.nphysics.sandbox.ui.ArrowLabel;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage.Simulation;

public class SpringProcessor implements ForceProcessor,Say{

	SpringComponent component;
	
	SimpleArrow arrow;
	ArrowLabel label;
	
	private Simulation sim;
	private PolygonObject StaticObject,DynamicObject;
	private Vector2 anchorStatic,anchorDynamic;
	private float stableLenght;
	private Spring spring;
	
	public SpringProcessor(SpringComponent component,Simulation sim) {
		this.component = component;
		this.sim = sim;
		init();
		
		arrow = new SimpleArrow(new Vector2(), new Vector2());
		arrow.setColor(1f, 0f, 0f, 1f);
		arrow.setVisible(true);
		
		if(NPhysics.currentStage == NPhysics.simulation) {
			label = new ArrowLabel(NPhysics.simulation.getUiGroup());
			label.setColor(Color.RED);
			label.setVisible(true);
		}
	}

	private void init() {
		
		PhysicalActor<?> A = component.getPhysicalActorA();
		PhysicalActor<?> B = component.getPhysicalActorB();
		
		PolygonObject BA = sim.objectsMap.get(A);
		PolygonObject BB = sim.objectsMap.get(B);
		
		if(BA.b.getType() == BodyType.StaticBody) {
			
			StaticObject = BA;
			DynamicObject = BB;
			
			anchorStatic = component.getAnchorA().getVector();
			anchorDynamic = component.getAnchorB().getVector();
		}else if(BB.b.getType() == BodyType.StaticBody){
			
			StaticObject = BB;
			DynamicObject = BA;
			
			anchorStatic = component.getAnchorB().getVector();
			anchorDynamic = component.getAnchorA().getVector();
		}else {
			
			throw new IllegalStateException();
		}
		
		stableLenght = component.spring.getDefinedLenght();
		
		anchorStatic.scl(1f/Util.METERS_UNIT());
		anchorDynamic.scl(1f/Util.METERS_UNIT());
		
		anchorStatic.sub(StaticObject.getPhysicalCenter());
		anchorDynamic.sub(DynamicObject.getPhysicalCenter());
		
		spring = new Spring(stableLenght * Util.METERS_UNIT());
		spring.normal = new Color(1, 1, 1, 1);
	}
	
	final Vector2 anchorStaticTemp = new Vector2();
	final Vector2 anchorDynamicTemp = new Vector2();
	
	float lxv;
	@Override
	public void processForce() {
		
		Vector2 StaticObjectCenter = StaticObject.getPhysicalCenter();
		Vector2 DynamicObjectCenter = DynamicObject.getPhysicalCenter();
		
		anchorStaticTemp.set(anchorStatic).add(StaticObjectCenter);
		anchorDynamicTemp.set(anchorDynamic).add(DynamicObjectCenter);
		
		anchorStaticTemp.set(Util.rotPivot(StaticObjectCenter, anchorStaticTemp, StaticObject.b.getAngle()));
		anchorDynamicTemp.set(Util.rotPivot(DynamicObjectCenter, anchorDynamicTemp, DynamicObject.b.getAngle()));
		
		float dst = anchorDynamicTemp.dst(anchorStaticTemp);
		
		float xv = dst - stableLenght;
		xv *=-component.getKConstant();
		Vector2 direction = new Vector2(anchorDynamicTemp).sub(anchorStaticTemp).setLength(1);

		direction.scl(xv);
		DynamicObject.b.applyForce(direction, anchorDynamicTemp, true);
		
		PositionVector pvd = new PositionVector(anchorDynamicTemp.scl(Util.METERS_UNIT()));
		PositionVector pvs = new PositionVector(anchorStaticTemp.scl(Util.METERS_UNIT()));
		
		spring.addAnchorA(pvd);
		spring.addAnchorB(pvs);
		
		spring.updateSpring();
		
		Vector2 start = new Vector2(anchorDynamicTemp);
		Vector2 end = new Vector2(start).add(direction.scl(Util.NEWTONS_UNIT()));
		
		arrow.setStart(start);
		arrow.setEnd(end);
		
		arrow.updateVertexArray();
		
		lxv =xv;
	}

	final static float hidealpha = PolygonObject.hidealpha;
	@Override
	public void render() {
		
		if(NPhysics.currentStage == NPhysics.simulation) {
			
			boolean selected = NPhysics.simulation.currentSesion.selected == DynamicObject;

			arrow.setColor(Color.RED.cpy().mul(1, 1, 1, PolygonObject.hide && !selected ? hidealpha : 1));
			label.setColor(Color.RED.cpy().mul(1, 1, 1, PolygonObject.hide && !selected ? hidealpha : 1));
			spring.normal = Color.WHITE.cpy().mul(1, 1, 1, PolygonObject.hide && !selected ? hidealpha : 1);
			
			label.setFloat(lxv);
			label.setPosition(new Vector2(anchorDynamicTemp).add(20, 20));
			spring.render();
			arrow.draw(NPhysics.currentStage.getBatch(), 1);
		}
	}
}
