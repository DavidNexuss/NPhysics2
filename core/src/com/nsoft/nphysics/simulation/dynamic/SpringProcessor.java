package com.nsoft.nphysics.simulation.dynamic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.nsoft.nphysics.Say;
import com.nsoft.nphysics.sandbox.ForceComponent;
import com.nsoft.nphysics.sandbox.PhysicalActor;
import com.nsoft.nphysics.sandbox.PositionVector;
import com.nsoft.nphysics.sandbox.SpringComponent;
import com.nsoft.nphysics.sandbox.Util;
import com.nsoft.nphysics.sandbox.drawables.Spring;
import com.nsoft.nphysics.simulation.dynamic.SimulationStage.Simulation;

public class SpringProcessor implements ForceProcessor,Say{

	SpringComponent component;
	private Simulation sim;
	private PolygonObject StaticObject,DynamicObject;
	private Vector2 anchorStatic,anchorDynamic;
	private float stableLenght;
	private Spring spring;
	
	public SpringProcessor(SpringComponent component,Simulation sim) {
		this.component = component;
		this.sim = sim;
		init();
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
	}
	@Override
	public void render() {
		
		spring.render();
	}
}
